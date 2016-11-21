package meetup.conflation.monitoring

import akka.actor.Actor
import kamon.Kamon
import meetup.conflation.monitoring.InstrumentedConflation.log
import org.slf4j.{Logger, LoggerFactory}

object InstrumentedConflation {

  val log: Logger = LoggerFactory.getLogger(classOf[InstrumentedConflation])
}

trait InstrumentedConflation {
  this: Actor =>

  val recorder = Kamon.metrics.entity(ConflationMetricsRecorder, self.path.toStringWithoutAddress)
  Kamon.metrics.shouldTrack(self.path.toStringWithoutAddress, ConflationMetricsRecorder.category)

  log.info(s"Instrumenting for ${self.path.toStringWithoutAddress}")
}
