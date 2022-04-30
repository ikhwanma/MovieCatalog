package ikhwan.binar.binarchallengelima.data.model.detailmovie


import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)