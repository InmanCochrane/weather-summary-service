package proj.matt.weathersummary

import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple {
  val run: IO[Nothing] = WeatherSummaryServer.run[IO]
}
