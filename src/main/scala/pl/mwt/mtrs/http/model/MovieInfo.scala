package pl.mwt.mtrs.http.model

import pl.mwt.mtrs.data

import scala.language.implicitConversions

private[http]
case class MovieInfo(imdbId        : String,
                     screenId      : String,
                     movieTitle    : String,
                     availableSeats: Int,
                     reservedSeats : Int)
    extends data.MovieInfo

private[http]
object MovieInfo {
  import spray.json.DefaultJsonProtocol._

  implicit val movieInfoFormat = jsonFormat5(MovieInfo.apply)

  implicit def fromInfo(info: data.MovieInfo) =
    MovieInfo(imdbId         = info.imdbId,
              screenId       = info.screenId,
              movieTitle     = info.movieTitle,
              availableSeats = info.availableSeats,
              reservedSeats  = info.reservedSeats)
}
