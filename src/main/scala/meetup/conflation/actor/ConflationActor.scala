package meetup.conflation.actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import meetup.conflation.event.PriceChange

object ConflationActor {

  def props(target: ActorRef): Props = Props(new ConflationActor(target))
}

class ConflationActor(target: ActorRef) extends Actor with ActorLogging {

  override def receive: Receive = {
    case event: PriceChange => target ! event
  }
}
