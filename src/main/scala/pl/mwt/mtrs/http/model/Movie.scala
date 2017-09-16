package pl.mwt.mtrs.http.model

import pl.mwt.mtrs.data.MovieDef

private[http]
case class Movie(imdbId        : String,
                 availableSeats: Int,
                 screenId      : String)
    extends MovieDef {

  Checks checkImdbAndScreen this
  require(availableSeats > 0, "availableSeats must be a positive number")
}

object Movie {
  import spray.json.DefaultJsonProtocol._

  implicit val movieFormat = jsonFormat3(Movie.apply)
}