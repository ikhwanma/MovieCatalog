package ikhwan.binar.binarchallengelima.model.popularmovie


import com.google.gson.annotations.SerializedName

data class GetPopularMovieResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val resultMovies: List<ResultMovie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)