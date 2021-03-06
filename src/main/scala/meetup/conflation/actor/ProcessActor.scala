package meetup.conflation.actor

import akka.actor.{Actor, ActorLogging, Props}
import meetup.conflation.event.PriceChange

object ProcessActor {

  def props(): Props = Props(new ProcessActor)
}

class ProcessActor extends Actor with ActorLogging {

  override def receive: Receive = {
    case event: PriceChange =>
      log.info(s"Computing Pizza price from $event")
  }
}
