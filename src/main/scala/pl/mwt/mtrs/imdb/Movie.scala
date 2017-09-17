package pl.mwt.mtrs.imdb

import spray.json.DefaultJsonProtocol

private[imdb]
case class Movie(Title: Option[String])

private[imdb]
object Movie {
  import DefaultJsonProtocol._

  implicit val movieFormat = jsonFormat1(Movie.apply)
}

