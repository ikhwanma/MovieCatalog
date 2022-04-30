package ikhwan.binar.binarchallengelima.data.model.nowplaying


import com.google.gson.annotations.SerializedName

data class GetNowPlayingResponse(
    @SerializedName("dates")
    val dates: ikhwan.binar.binarchallengelima.data.model.nowplaying.Dates,
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val resultNows: List<ikhwan.binar.binarchallengelima.data.model.nowplaying.ResultNow>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)