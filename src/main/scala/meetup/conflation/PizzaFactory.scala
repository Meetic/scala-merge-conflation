package meetup.conflation

import akka.actor.ActorSystem
import meetup.conflation.actor.{ConflationActor, ProcessActor, SourceActor}
import scala.concurrent.duration._

object PizzaFactory extends App {

  implicit val system = ActorSystem("pizza-factory")

  val idRangeStart = 1000
  val idRangeEnd = 1800
  val sourceDelay = 2 millis

  // [ Source ] -> [ Conflation ] -> [ Process ]

  val processActor = system.actorOf(ProcessActor.props(), "Process")
  val conflationActor = system.actorOf(ConflationActor.props(processActor), "Conflate")
  system.actorOf(SourceActor.props(conflationActor, idRangeStart, idRangeEnd, sourceDelay), "Source")
}
