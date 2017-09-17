package pl.mwt.mtrs

import akka.actor.ActorSystem
import akka.http.scaladsl.server.HttpApp
import akka.http.scaladsl.settings.ServerSettings
import com.typesafe.config.ConfigFactory
import pl.mwt.mtrs.imdb.OmdbService
import pl.mwt.mtrs.storage.Stores
import pl.mwt.mtrs.svc._

object Application
    extends HttpApp
       with http.Api {

  private val cfg = ConfigFactory.load

  private implicit val actorSystem = ActorSystem()
  import actorSystem.dispatcher // executor

  private val stores = new Stores(cfg getConfig "db")

  private val titleService = new OmdbService(cfg getConfig "omdb")

  override def movieService = new MovieServiceImpl(stores.movieStore, titleService)

  def main(args: Array[String]): Unit = {
    val httpCfg = cfg getConfig "http"
    val host = httpCfg getString "host"
    val port = httpCfg getInt "port"

    startServer(host, port, ServerSettings(cfg), actorSystem)
  }
}
