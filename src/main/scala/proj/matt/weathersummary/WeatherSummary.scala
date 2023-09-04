package proj.matt.weathersummary

import cats.effect.Concurrent
import cats.implicits._
import io.circe.Encoder
import io.circe.generic.semiauto._
import org.http4s.Method._
import org.http4s._
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.implicits._
import proj.matt.weathersummary.NOAAEntities._

trait WeatherSummary[F[_]] {
  def get(latitude: Double, longitude: Double): F[WeatherSummary.Summary]
}

object WeatherSummary {
  def apply[F[_]](implicit ev: WeatherSummary[F]): WeatherSummary[F] = ev

  final case class Summary(message: String) extends AnyVal

  private object Summary {
    def from(noaaForecast: NOAAForecast): Summary = {
      val message =
        s"""${noaaForecast.currentCharacteristics}
           |The current temperature is ${noaaForecast.currentTemperature}.
           |High of ${noaaForecast.high}. Low of ${noaaForecast.low}."""
          .stripMargin
      new Summary(message)
    }

    //noinspection ScalaUnusedSymbol (it is used)
    implicit val summaryEncoder: Encoder[Summary] = deriveEncoder[Summary]
  }

  final case class SummaryError(e: Throwable) extends RuntimeException

  def impl[F[_] : Concurrent](C: Client[F]): WeatherSummary[F] = new WeatherSummary[F] {
    val dsl: Http4sClientDsl[F] = new Http4sClientDsl[F] {}

    import dsl._

    def get(
      latitude: Double,
      longitude: Double
    ): F[Summary] = {
      C
        .expect[NOAAPoint] {
          GET(uri"https://api.weather.gov/points/" / s"$latitude,$longitude")
        }
        .map { noaaPoint =>
          Uri.fromString(noaaPoint.properties.forecastGridData) match {
            case Left(parseFailure) => throw SummaryError(parseFailure)
            case Right(forecastUrl) => forecastUrl
          }
        }
        .flatMap { noaaForecastUrl =>
          C.expect[NOAAForecast](GET(noaaForecastUrl))
            .map(Summary.from)
        }
        .adaptError { case t => NOAAError(t) }
    }
  }

}
