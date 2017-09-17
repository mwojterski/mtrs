package pl.mwt.mtrs.http.model

import pl.mwt.mtrs.data

private[http]
case class MovieId(imdbId  : String,
                   screenId: String)
  extends data.MovieId {

  Checks checkImdbAndScreen this
}

private[http]
object MovieId {
  import spray.json.DefaultJsonProtocol._

  implicit val movieIdFormat = jsonFormat2(MovieId.apply)
}
