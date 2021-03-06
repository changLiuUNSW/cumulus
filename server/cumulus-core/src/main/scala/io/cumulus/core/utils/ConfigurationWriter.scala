package io.cumulus.core.utils

import java.io.File
import scala.concurrent.Future

import akka.stream.{IOResult, Materializer}
import akka.stream.scaladsl.{FileIO, Source}
import akka.util.ByteString
import com.typesafe.config.ConfigRenderOptions
import play.api.Configuration

case class ConfigurationWriter(configuration: Configuration) {

  def write(file: File)(implicit m: Materializer): Future[IOResult] = {
    file.getParentFile.mkdir()

    Source(configuration.entrySet)
      .map {
        case (key, value) =>
          s"$key = ${value.render(ConfigRenderOptions.concise())}"
      }
      .prepend(Source(List("# Note: this file is generated by Cumulus and should not be manually modified")))
      .map(s => ByteString(s + "\n"))
      .runWith(FileIO.toPath(file.toPath))
  }

}
