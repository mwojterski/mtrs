## Movie Ticket reservation system

Manages ticket reservation for movies.

## Functionality

### Register a movie

Accepts a json with following structure to be able to register a movie before any reservation can happen.

    {
        "imdbId": "tt0111161",
        "availableSeats": 100,
        "screenId": "screen_123456"
    }    

Where:

* `imdbId` is IMDB movie identifier
* `availableSeats` the total seats available for this movie
* `screenId` is an externally managed identifier of information when and where the movie is screened.

### Reserve a seat at the movie

Allows to reserve a seat for the movie. It consumes a request with following json to reserve a single seat for the movie.

    {
        "imdbId": "tt0111161",
        "screenId": "screen_123456"
    }    

Where:

* `imdbId` is IMDB movie identifier
* `screenId` is an externally managed identifier of information when and where the movie is screened.

### Retrieve information about the movie

Allows to see how many seats are reserved for a movie as well as the movie title.
It consumes a request with `imdbId` and `screenId` and return following response:

    {
        "imdbId": "tt0111161",
        "screenId": "screen_123456",
        "movieTitle": "The Shawshank Redemption",
        "availableSeats": 100,
        "reservedSeats": 50
    }    

Where:

* `imdbId` is IMDB movie identifier
* `screenId` is an externally managed identifier of information when and where the movie is screened.
* `movieTitle` is the title of the movie
* `availableSeats` the total seats available for this movie
* `reservedSeats` the total number of reserved seats for a movie and screen.
