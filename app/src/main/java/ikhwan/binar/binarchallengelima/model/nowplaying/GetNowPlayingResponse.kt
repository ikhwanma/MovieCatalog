package ikhwan.binar.binarchallengelima.model.nowplaying


import com.google.gson.annotations.SerializedName

data class GetNowPlayingResponse(
    @SerializedName("dates")
    val dates: Dates,
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val resultNows: List<ResultNow>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)