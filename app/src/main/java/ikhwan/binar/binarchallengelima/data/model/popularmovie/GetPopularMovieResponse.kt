package ikhwan.binar.binarchallengelima.data.model.popularmovie


import com.google.gson.annotations.SerializedName

data class GetPopularMovieResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val resultMovies: List<ikhwan.binar.binarchallengelima.data.model.popularmovie.ResultMovie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)