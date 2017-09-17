package pl.mwt.mtrs.imdb
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.coding.Gzip
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, Uri}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import com.typesafe.config.Config
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.Future

class OmdbService(cfg: Config)
                 (implicit actorSystem: ActorSystem)
    extends MovieTitleService
       with StrictLogging {

  private val omdbUri = Uri(cfg getString "uri")
  private val apiKey = cfg getString "apiKey"

  private val http = Http()
  private implicit val materializer = ActorMaterializer()

  import actorSystem.dispatcher

  override def getTitle(imdbId: String) =
    http
      .singleRequest(request(imdbId))
      .map(Gzip decodeMessage _)
      .flatMap(handleResponse)

  private def request(id: String) =
    HttpRequest(uri = uri(id))

  private def uri(id: String) =
    omdbUri withQuery Query (
      "apiKey" -> apiKey,
      "i" -> id
    )

  private def handleResponse(response: HttpResponse) =
    if (response.status.isSuccess)
      parseMovieTitle(response)

    else {
      response discardEntityBytes()
      logger warn("Received failed response: {}", response)

      // todo: perhaps it'd be better to differentiate fail from unknown id
      Future successful None
    }

  private def parseMovieTitle(response: HttpResponse) = {
    import SprayJsonSupport._

    Unmarshal(response)
      .to[Movie]
      .map(_.Title)
  }
}
