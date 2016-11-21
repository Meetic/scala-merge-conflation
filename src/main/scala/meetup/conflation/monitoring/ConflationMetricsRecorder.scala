package meetup.conflation.monitoring

import kamon.metric.instrument.{InstrumentFactory, Time}
import kamon.metric.{EntityRecorderFactory, GenericEntityRecorder}

class ConflationMetricsRecorder(instrumentFactory: InstrumentFactory) extends GenericEntityRecorder(instrumentFactory) {
  protected val in = counter("in")
  protected val out = counter("out")
  protected val merged = histogram("merged")
  protected val buffer = histogram("buffer")

  def recordIn(): Unit = {
    in.increment()
  }
  def recordOut(): Unit = {
    out.increment()
  }
  def recordMerge(count: Int): Unit = {
    merged.record(count.toLong)
  }
  def recordBufferSize(size: Int): Unit = {
    buffer.record(size)
  }

}

object ConflationMetricsRecorder extends EntityRecorderFactory[ConflationMetricsRecorder] {
  override def category: String = "conflation"
  override def createRecorder(instrumentFactory: InstrumentFactory): ConflationMetricsRecorder =
    new ConflationMetricsRecorder(instrumentFactory)
}
