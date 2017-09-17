package pl.mwt.mtrs.http.model

import pl.mwt.mtrs.data

private[model]
object Checks {

  def checkImdbAndScreen(movieId: data.MovieId): Unit = {
    notEmpty(movieId.imdbId, "imdbId")
    notEmpty(movieId.screenId, "screenId")
  }

  private def notEmpty(field: String, name: String): Unit =
    require(!field.isEmpty, s"$name is empty")

}
