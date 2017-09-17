package pl.mwt.mtrs.storage
import io.getquill.{PostgresAsyncContext, SnakeCase}
import pl.mwt.mtrs.data.{MovieDef, MovieId}

import scala.concurrent.{ExecutionContext, Future}

private[storage]
class DbMovieStore(ctx: PostgresAsyncContext[SnakeCase])
                  (implicit ec: ExecutionContext)
  extends MovieStore {

  import ctx._

  override def add(movieDef: MovieDef, title: String) = {
    val movie = newMovie(movieDef, title)

    run {
      movies.insert(lift(movie))
            .ifNotExists
    }
    .isSuccess
  }

  override def compareAndSetReservedSeats(movieId : MovieId, currentSeats: Int, newSeats: Int) =
    run {
      movies.filter { movie =>
              movieIdEq(movieId)(movie) &&
              movie.reservedSeats == lift(currentSeats)
            }
            .update(_.reservedSeats -> lift(newSeats))
    }
    .isSuccess

  override def findMovie(movieId: MovieId) =
    run {
      movies.filter(movieIdEq(movieId)(_))
    }
    .headOption

  override def findTitle(imdbId: String) =
    run {
      movies.filter(_.imdbId == lift(imdbId))
            .map(_.movieTitle)
            .take(1)
    }
    .headOption

  private def newMovie(movieDef: MovieDef, title: String) =
    Movie(imdbId         = movieDef.imdbId,
          screenId       = movieDef.screenId,
          availableSeats = movieDef.availableSeats,
          movieTitle     = title)

  private def movies =
    quote { query[Movie] }

  private def movieIdEq(movieId: MovieId) =
    quote { mv: Movie =>
      mv.imdbId   == lift(movieId.imdbId) &&
      mv.screenId == lift(movieId.screenId)
    }

  private implicit class DoNothingOnConflict[I <: Insert[_]](ins: I) {
    def ifNotExists = quote(infix"$ins ON CONFLICT DO NOTHING".as[I])
  }

  private implicit class ActionResult(res: Future[Long]) {
    def isSuccess = res map (_ != 0)
  }

  private implicit class UniqueResult[A](res: Future[Seq[A]]) {
    def headOption = res map (_.headOption)
  }
}
