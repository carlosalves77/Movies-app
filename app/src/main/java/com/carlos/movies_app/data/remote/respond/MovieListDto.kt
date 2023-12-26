package com.carlos.movies_app.data.remote.respond

data class MovieListDto(
    val page: Int,
    val result: List<MovieDto>,
    val total_pages: Int,
    val total_results: Int
)
