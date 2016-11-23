package meetup.conflation.actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import meetup.conflation.actor.ConflationActor.Tick
import meetup.conflation.conflation.{Conflated, Conflation}
import meetup.conflation.event._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object ConflationActor {

  case object Tick
  def props(target: ActorRef): Props = Props(new ConflationActor(target))
}

class ConflationActor(target: ActorRef) extends Actor with ActorLogging with Conflation[PriceChange, MergedPriceChange] {

  override val buffer = mutable.HashMap[String, Conflated[PriceChange]]()

  override def bufferEvent(event: PriceChange): Unit = {
    buffer
      .getOrElseUpdate(event.providerId, Conflated(ListBuffer(), System.currentTimeMillis()))
      .events.append(event)
  }

  override def mergeEvents(events: Seq[PriceChange]): MergedPriceChange = {
    val prices: (Option[BigDecimal], Option[BigDecimal], Option[BigDecimal]) = (None, None, None)
    val (tomatoPrice, mushroomPrice, flourPrice) = events.foldRight(prices) {
      case (TomatoPriceChange(_, price, _, _), (None, mushroom, flour)) => (Some(price), mushroom, flour)
      case (MushroomPriceChange(_, price, _, _), (tomato, None, flour)) => (tomato, Some(price), flour)
      case (FlourPriceChange(_, price, _, _), (tomato, mushroom, None)) => (tomato, mushroom, Some(price))
      case (_, all) => all
    }
    MergedPriceChange(events.head.providerId, tomatoPrice, mushroomPrice, flourPrice, events.last.time)
  }

  override def sendMergedEvents(): Unit = {
    log.info(s"Processing conflated events (${buffer.size})")
    val threshold = System.currentTimeMillis() - delay.toMillis
    buffer.retain {
      case (_, conflated) if conflated.time < threshold =>
        target ! mergeEvents(conflated.events)
        false

      case _ => true
    }
  }

  override def receive: Receive = {
    case event: PriceChange => bufferEvent(event)
    case Tick => sendMergedEvents()
  }

  import context.dispatcher
  context.system.scheduler.schedule(tickInterval, tickInterval, self, Tick)
}
