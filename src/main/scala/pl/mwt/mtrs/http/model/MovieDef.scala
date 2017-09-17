package pl.mwt.mtrs.http.model

import pl.mwt.mtrs.data

private[http]
case class MovieDef(imdbId        : String,
                    availableSeats: Int,
                    screenId      : String)
    extends data.MovieDef {

  Checks checkImdbAndScreen this
  require(availableSeats > 0, "availableSeats must be a positive number")
}

private[http]
object MovieDef {
  import spray.json.DefaultJsonProtocol._

  implicit val movieFormat = jsonFormat3(MovieDef.apply)
}