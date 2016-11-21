package meetup.conflation.event

import java.time.ZonedDateTime

sealed trait ChangeEvent {
  def providerId: String
}

case class TomatoPriceChange
(
  providerId: String,
  price: BigDecimal,
  source: String,
  time: ZonedDateTime
) extends ChangeEvent

case class MushroomPriceChange
(
  providerId: String,
  price: BigDecimal,
  source: String,
  time: ZonedDateTime
) extends ChangeEvent

case class FlourPriceChange
(
  providerId: String,
  price: BigDecimal,
  source: String,
  time: ZonedDateTime
) extends ChangeEvent