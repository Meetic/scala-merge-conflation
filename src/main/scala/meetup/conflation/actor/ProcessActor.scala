package meetup.conflation.actor

import akka.actor.{Actor, ActorLogging, Props}
import meetup.conflation.event.ChangeEvent

object ProcessActor {

  def props(): Props = Props(new ProcessActor)
}

class ProcessActor extends Actor with ActorLogging {

  override def receive: Receive = {
    case event: ChangeEvent =>
      log.info(s"Computing Pizza price from $event")
  }
}
