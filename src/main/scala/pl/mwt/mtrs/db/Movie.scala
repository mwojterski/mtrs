package pl.mwt.mtrs.db

case class Movie(imdbId: String,
                 screenId: String,
                 title: String,
                 availableSeats: Short,
                 reservedSeats: Short)
