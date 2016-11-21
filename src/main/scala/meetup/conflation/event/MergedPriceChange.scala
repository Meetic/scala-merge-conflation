package meetup.conflation.event

import java.time.ZonedDateTime

case class MergedPriceChange
(
  providerId: String,
  tomatoPrice: Option[BigDecimal],
  mushroomPrice: Option[BigDecimal],
  flourPrice: Option[BigDecimal],
  time: ZonedDateTime
)
