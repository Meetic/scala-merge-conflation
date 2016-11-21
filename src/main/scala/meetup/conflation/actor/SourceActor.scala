package meetup.conflation.actor

import java.time.ZonedDateTime

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import meetup.conflation.actor.SourceActor.ProviderPrices
import meetup.conflation.event._

import scala.collection.mutable
import scala.concurrent.duration.FiniteDuration
import scala.util.Random

class SourceActor(target: ActorRef, idRangeStart: Int, idRangeEnd: Int, delay: FiniteDuration) extends Actor with ActorLogging {

  log.info(s"Starting Source with ids in range $idRangeStart -> $idRangeEnd")

  var providerPrices = new mutable.HashMap[Int, ProviderPrices]()

  val possibleIds = Range(idRangeStart, idRangeStart + idRangeEnd).toArray
  val possibleSources = List("wall_street_nyc", "tokyo", "london_city", "paris", "beijing")
  val possibleChanges = (Range(0, 15).map(_ -> "TomatoPriceChange") ++ Range(15, 20).map(_ -> "MushroomPriceChange") ++ Range(20, 30).map(_ -> "FlourPriceChange")).toMap

  Iterator.continually().map { id =>
    val providerId = possibleIds(Random.nextInt(idRangeEnd - idRangeStart))
    val prices = providerPrices.getOrElseUpdate(providerId, ProviderPrices(providerId))
    possibleChanges(Random.nextInt(possibleChanges.size)) match {
      case "TomatoPriceChange" => TomatoPriceChange(
        providerId.toString, prices.randomTomatoPriceChange, possibleSources(Random.nextInt(possibleSources.size)), ZonedDateTime.now()
      )
      case "MushroomPriceChange" => MushroomPriceChange(
        providerId.toString, prices.randomMushroomPriceChange, possibleSources(Random.nextInt(possibleSources.size)), ZonedDateTime.now()
      )
      case "FlourPriceChange" => FlourPriceChange(
        providerId.toString, prices.randomFlourPriceChange, possibleSources(Random.nextInt(possibleSources.size)), ZonedDateTime.now()
      )
    }
  }.foreach { event =>
    Thread.sleep(delay.toMillis)
    target ! event
  }

  override def receive: Receive = PartialFunction.empty

}

object SourceActor {

  def props(target: ActorRef, idRangeStart: Int, idRangeEnd: Int, delay: FiniteDuration): Props =
    Props(new SourceActor(target, idRangeStart, idRangeEnd, delay))

  case class ProviderPrices
  (
    id: Int,
    private var tomatoPrice: BigDecimal = BigDecimal("22.25"),
    private var mushroomPrice: BigDecimal = BigDecimal("50.35"),
    private var flourPrice: BigDecimal = BigDecimal("18.05")
  ) {
    def randomPriceChange(current: BigDecimal): BigDecimal = {
      if (current >= 0) {
        current + (if (Random.nextBoolean()) BigDecimal("0.05") else BigDecimal("-0.05"))
      }
      else {
        current + BigDecimal("0.05")
      }
    }

    def randomTomatoPriceChange: BigDecimal = {
      tomatoPrice = randomPriceChange(tomatoPrice)
      tomatoPrice
    }

    def randomMushroomPriceChange: BigDecimal = {
      mushroomPrice = randomPriceChange(mushroomPrice)
      mushroomPrice
    }

    def randomFlourPriceChange: BigDecimal = {
      flourPrice = randomPriceChange(flourPrice)
      flourPrice
    }
  }
}