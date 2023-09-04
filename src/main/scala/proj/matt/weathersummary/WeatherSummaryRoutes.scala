package proj.matt.weathersummary

import cats.effect.Sync
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.impl.QueryParamDecoderMatcher

object WeatherSummaryRoutes {

  private object LatitudeQueryParamMatcher extends QueryParamDecoderMatcher[Double]("lat")

  private object LongitudeQueryParamMatcher extends QueryParamDecoderMatcher[Double]("lon")

  def weatherSummaryRoutes[F[_] : Sync](WS: WeatherSummary[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "weather-summary" :?
        LatitudeQueryParamMatcher(lat) +& LongitudeQueryParamMatcher(lon) =>
        for {
          summary <- WS.get(lat, lon)
          resp <- Ok(summary)
        } yield resp
    }
  }
}
