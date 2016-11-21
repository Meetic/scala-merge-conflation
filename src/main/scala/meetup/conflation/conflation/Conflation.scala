package meetup.conflation.conflation

import scala.collection.mutable
import scala.concurrent.duration._

trait Conflation[E, G] {

  val delay = 10 seconds
  val tickInterval = 2 seconds

  def buffer: mutable.HashMap[String, Conflated[E]]

  def bufferEvent(event: E): Unit

  def onTick(): Unit

  def mergeEvents(events: Seq[E]): G
}

case class Conflated[E](events: mutable.ListBuffer[E], time: Long)