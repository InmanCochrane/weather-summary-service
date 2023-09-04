package proj.matt.weathersummary

import cats.effect.Concurrent
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf

object NOAAEntities {

  final case class NOAAPointProperties(forecastGridData: String) extends AnyVal

  final case class NOAAPoint(properties: NOAAPointProperties)

  object NOAAPoint {
    implicit val noaaPointPropertiesDecoder: Decoder[NOAAPointProperties] = deriveDecoder[NOAAPointProperties]
    implicit val noaaPointDecoder: Decoder[NOAAPoint] = deriveDecoder[NOAAPoint]

    implicit def noaaPointEntityDecoder[F[_] : Concurrent]: EntityDecoder[F, NOAAPoint] = jsonOf
  }

  final case class NOAAGeneralMeasurementValue(
    validTime: String,
    value: Double
  ) {
    def asTwoDecimalString: String = {
      val asS = value.toString
      val (lead, follow) = asS.splitAt(asS.indexOf(".") + 1)
      lead + follow.take(2)
    }
  }

  trait NOAAGeneralMeasurementValueContainer {
    val uom: String
    val values: Seq[NOAAGeneralMeasurementValue]
  }

  final case class NOAAForecastTemperature(
    uom: String,
    values: Seq[NOAAGeneralMeasurementValue]
  ) extends NOAAGeneralMeasurementValueContainer {
    private val uomSymbol = uom match {
      case "wmoUnit:degC" => "C"
      case "wmoUnit:degF" => "F"
      case _ => "?"
    }

    val nearestTemperature: String = s"${values.head.asTwoDecimalString}°$uomSymbol"
  }

  sealed abstract class Coverage(val value: String) extends Product
    with Serializable {
    override def toString: String = value
  }

  private object Coverage {
    private case object Areas extends Coverage("areas")

    private case object Brief extends Coverage("brief")

    private case object Chance extends Coverage("chance")

    private case object Definite extends Coverage("definite")

    private case object Few extends Coverage("few")

    private case object Frequent extends Coverage("frequent")

    private case object Intermittent extends Coverage("intermittent")

    private case object Isolated extends Coverage("isolated")

    private case object Likely extends Coverage("likely")

    private case object Numerous extends Coverage("numerous")

    private case object Occasional extends Coverage("occasional")

    private case object Patchy extends Coverage("patchy")

    private case object Periods extends Coverage("periods")

    private case object Scattered extends Coverage("scattered")

    private case object SlightChance extends Coverage("slight_chance") {
      override def toString: String = "slight chance"
    }

    private case object Widespread extends Coverage("widespread ")

    private case object ErrorAsText extends Coverage("<parsing error>")

    def fromString(s: String): Coverage = s match {
      case Areas.value => Areas
      case Brief.value => Brief
      case Chance.value => Chance
      case Definite.value => Definite
      case Few.value => Few
      case Frequent.value => Frequent
      case Intermittent.value => Intermittent
      case Isolated.value => Isolated
      case Likely.value => Likely
      case Numerous.value => Numerous
      case Occasional.value => Occasional
      case Patchy.value => Patchy
      case Periods.value => Periods
      case Scattered.value => Scattered
      case SlightChance.value => SlightChance
      case Widespread.value => Widespread
      case _ => ErrorAsText
    }
  }

  sealed abstract class Weather(val value: String) extends Product
    with Serializable {
    override def toString: String = value
  }

  private object Weather {
    private case object BlowingDust extends Weather("blowing_dust") {
      override def toString: String = "blowing dust"
    }

    private case object BlowingSand extends Weather("blowing_sand") {
      override def toString: String = "blowing sand"
    }

    private case object BlowingSnow extends Weather("blowing_snow") {
      override def toString: String = "blowing snow"
    }

    private case object Drizzle extends Weather("drizzle")

    private case object Fog extends Weather("fog")

    private case object FreezingFog extends Weather("freezing_fog") {
      override def toString: String = "freezing fog"
    }

    private case object FreezingDrizzle extends Weather("freezing_drizzle") {
      override def toString: String = "freezing drizzle"
    }

    private case object FreezingRain extends Weather("freezing_rain") {
      override def toString: String = "freezing rain"
    }

    private case object FreezingSpray extends Weather("freezing_spray") {
      override def toString: String = "freezing spray"
    }

    private case object Frost extends Weather("frost")

    private case object Hail extends Weather("hail")

    private case object Haze extends Weather("haze")

    private case object IceCrystals extends Weather("ice_crystals") {
      override def toString: String = "ice crystals"
    }

    private case object IceFog extends Weather("ice_fog") {
      override def toString: String = "ice fog"
    }

    private case object Rain extends Weather("rain")

    private case object RainShowers extends Weather("rain_showers") {
      override def toString: String = "rain showers"
    }

    private case object Sleet extends Weather("sleet")

    private case object Smoke extends Weather("smoke")

    private case object Snow extends Weather("snow")

    private case object SnowShowers extends Weather("snow_showers") {
      override def toString: String = "snow showers"
    }

    private case object Thunderstorms extends Weather("thunderstorms")

    private case object VolcanicAsh extends Weather("volcanic_ash") {
      override def toString: String = "volcanic ash"
    }

    private case object WaterSpouts extends Weather("water_spouts") {
      override def toString: String = "water spouts"
    }

    private case object ErrorAsText extends Weather("<parsing error>")

    def fromString(s: String): Weather = s match {
      case BlowingDust.value => BlowingDust
      case BlowingSand.value => BlowingSand
      case BlowingSnow.value => BlowingSnow
      case Drizzle.value => Drizzle
      case Fog.value => Fog
      case FreezingFog.value => FreezingFog
      case FreezingDrizzle.value => FreezingDrizzle
      case FreezingRain.value => FreezingRain
      case FreezingSpray.value => FreezingSpray
      case Frost.value => Frost
      case Hail.value => Hail
      case Haze.value => Haze
      case IceCrystals.value => IceCrystals
      case IceFog.value => IceFog
      case Rain.value => Rain
      case RainShowers.value => RainShowers
      case Sleet.value => Sleet
      case Smoke.value => Smoke
      case Snow.value => Snow
      case SnowShowers.value => SnowShowers
      case Thunderstorms.value => Thunderstorms
      case VolcanicAsh.value => VolcanicAsh
      case WaterSpouts.value => WaterSpouts
      case _ => ErrorAsText
    }
  }

  sealed abstract class Intensity(val value: String) extends Product
    with Serializable {
    override def toString: String = value
  }

  private object Intensity {
    private case object VeryLight extends Intensity("very_light") {
      override def toString: String = "very light"
    }

    private case object Light extends Intensity("light")

    private case object Moderate extends Intensity("moderate")

    private case object Heavy extends Intensity("heavy")

    private case object ErrorAsText extends Intensity("<parsing     error>")

    def fromString(s: String): Intensity = s match {
      case VeryLight.value => VeryLight
      case Light.value => Light
      case Moderate.value => Moderate
      case Heavy.value => Heavy
      case _ => ErrorAsText
    }
  }

  sealed abstract class Attribute(val value: String) extends Product
    with Serializable {
    override def toString: String = value
  }

  private object Attribute {
    private case object DamagingWind extends Attribute("damaging_wind") {
      override def toString: String = "damaging wind"
    }

    private case object DryThunderstorms extends Attribute("dry_thunderstorms") {
      override def toString: String = "dry      thunderstorms"
    }

    private case object Flooding extends Attribute("flooding")

    private case object GustyWind extends Attribute("gusty_wind") {
      override def toString: String = "gusty    wind"
    }

    private case object HeavyRain extends Attribute("heavy_rain") {
      override def toString: String = "heavy    rain"
    }

    private case object LargeHail extends Attribute("large_hail") {
      override def toString: String = "large    hail"
    }

    private case object SmallHail extends Attribute("small_hail") {
      override def toString: String = "small    hail"
    }

    private case object Tornadoes extends Attribute("tornadoes")

    private case object ErrorAsText extends Attribute("<parsing error>")


    def fromString(s: String): Attribute = s match {
      case DamagingWind.value => DamagingWind
      case DryThunderstorms.value => DryThunderstorms
      case Flooding.value => Flooding
      case GustyWind.value => GustyWind
      case HeavyRain.value => HeavyRain
      case LargeHail.value => LargeHail
      case SmallHail.value => SmallHail
      case Tornadoes.value => Tornadoes
      case _ => ErrorAsText
    }
  }


  final case class NOAAWeatherSummaryValue(
    coverage: Option[String],
    weather: Option[String],
    intensity: Option[String],
    attributes: Option[Seq[String]]
  ) {
    private val validatedCoverage: Option[Coverage] = coverage
      .map(Coverage.fromString)
    private val validatedWeather: Option[Weather] = weather
      .map(Weather.fromString)
    private val validatedIntensity: Option[Intensity] = intensity
      .map(Intensity.fromString)
    private val validatedAttributes: Option[Seq[Attribute]] = attributes
      .map(_.map(Attribute.fromString))

    val description: String = validatedCoverage match {
      case Some(value) =>
        val base = s"${value.toString.capitalize} of ${
          validatedIntensity
            .getOrElse("unknown intensity of")
        } ${validatedWeather.getOrElse("unknown weather")}."
        if (validatedAttributes.isDefined) {
          base + s"Be alert for ${validatedAttributes.map(_.mkString(","))}."
        } else {
          base
        }
      case None => "Clear"
    }
  }

  final case class NOAAWeatherSummaryContainer(
    validTime: String,
    value: Seq[NOAAWeatherSummaryValue]
  )

  final case class NOAAForecastWeather(values: Seq[NOAAWeatherSummaryContainer]) {
    val nearestDescription: String = values.head.value.head.description

  }

  final case class NOAAForecastProperties(
    temperature: NOAAForecastTemperature,
    minTemperature: NOAAForecastTemperature,
    maxTemperature: NOAAForecastTemperature,
    weather: NOAAForecastWeather
  )

  final case class NOAAForecast(properties: NOAAForecastProperties) {
    val currentTemperature: String = properties.temperature.nearestTemperature
    val high: String = properties.maxTemperature.nearestTemperature
    val low: String = properties.minTemperature.nearestTemperature
    val currentCharacteristics: String = properties.weather.nearestDescription
  }

  object NOAAForecast {
    implicit val noaaGeneralMeasurementValueDecoder: Decoder[NOAAGeneralMeasurementValue] = deriveDecoder[NOAAGeneralMeasurementValue]
    implicit val noaaWeatherSummaryValue: Decoder[NOAAWeatherSummaryValue] = deriveDecoder[NOAAWeatherSummaryValue]
    implicit val noaaWeatherSummaryContainerDecoder: Decoder[NOAAWeatherSummaryContainer] = deriveDecoder[NOAAWeatherSummaryContainer]
    implicit val noaaForecastWeatherDecoder: Decoder[NOAAForecastWeather] = deriveDecoder[NOAAForecastWeather]
    implicit val noaaForecastTemperatureDecoder: Decoder[NOAAForecastTemperature] = deriveDecoder[NOAAForecastTemperature]
    implicit val noaaForecastPropertiesDecoder: Decoder[NOAAForecastProperties] = deriveDecoder[NOAAForecastProperties]
    implicit val noaaForecastDecoder: Decoder[NOAAForecast] = deriveDecoder[NOAAForecast]

    implicit def forecastEntityDecoder[F[_] : Concurrent]: EntityDecoder[F, NOAAForecast] = jsonOf
  }

  final case class NOAAError(e: Throwable) extends RuntimeException(e)
}
