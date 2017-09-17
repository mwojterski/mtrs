package pl.mwt.mtrs.storage

import pl.mwt.mtrs.data.MovieInfo

private[storage]
case class Movie(imdbId        : String,
                 screenId      : String,
                 movieTitle    : String,
                 availableSeats: Int,
                 reservedSeats : Int = 0)
  extends MovieInfo
