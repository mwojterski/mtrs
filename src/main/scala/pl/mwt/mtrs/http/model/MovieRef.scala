package pl.mwt.mtrs.http.model

import pl.mwt.mtrs.data.MovieId

private[http]
case class MovieRef(imdbId: String,
                    screenId: String)
  extends MovieId {

  Checks checkImdbAndScreen this
}

object MovieRef {
  import spray.json.DefaultJsonProtocol._

  implicit val movieRefFormat = jsonFormat2(MovieRef.apply)
}
