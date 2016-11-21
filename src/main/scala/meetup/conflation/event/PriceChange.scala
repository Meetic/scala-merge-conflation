package meetup.conflation.event

import java.time.ZonedDateTime

sealed trait PriceChange {
  def providerId: String
}

case class TomatoPriceChange
(
  providerId: String,
  price: BigDecimal,
  source: String,
  time: ZonedDateTime
) extends PriceChange

case class MushroomPriceChange
(
  providerId: String,
  price: BigDecimal,
  source: String,
  time: ZonedDateTime
) extends PriceChange

case class FlourPriceChange
(
  providerId: String,
  price: BigDecimal,
  source: String,
  time: ZonedDateTime
) extends PriceChange