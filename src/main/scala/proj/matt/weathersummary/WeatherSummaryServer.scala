package proj.matt.weathersummary

import cats.effect.Async
import com.comcast.ip4s._
import fs2.io.net.Network
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger

object WeatherSummaryServer {

  def run[F[_] : Async : Network]: F[Nothing] = {
    for {
      client <- EmberClientBuilder.default[F].build
      weatherSummary = WeatherSummary.impl[F](client)

      httpApp = WeatherSummaryRoutes.weatherSummaryRoutes[F](weatherSummary).orNotFound

      finalHttpApp = Logger.httpApp(logHeaders = true, logBody = true)(httpApp)

      _ <-
        EmberServerBuilder.default[F]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(finalHttpApp)
          .build
    } yield ()
  }.useForever
}
