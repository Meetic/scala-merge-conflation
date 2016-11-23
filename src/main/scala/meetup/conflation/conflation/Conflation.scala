package meetup.conflation.conflation

import scala.collection.mutable
import scala.concurrent.duration._

trait Conflation[E, G] {

  // The delay during which messages will be conflated
  val delay = 10 seconds

  // The interval at which the buffer will be checked for expired events
  val tickInterval = 2 seconds

  // The buffer will hold events during the conflation delay. The key here is the providerId (String)
  def buffer: mutable.HashMap[String, Conflated[E]]

  // Method to add an event to the conflation buffer
  def bufferEvent(event: E): Unit

  // Method to flush expired events from the conflation buffer and send them to the target actor
  def sendMergedEvents(): Unit

  // Method used by sendMergedEvents() to merge the Seq of buffered events for a providerId
  def mergeEvents(events: Seq[E]): G
}

case class Conflated[E](events: mutable.ListBuffer[E], time: Long)