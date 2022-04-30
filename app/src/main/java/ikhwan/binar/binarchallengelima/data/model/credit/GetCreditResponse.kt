package ikhwan.binar.binarchallengelima.data.model.credit


import com.google.gson.annotations.SerializedName

data class GetCreditResponse(
    @SerializedName("cast")
    val cast: List<ikhwan.binar.binarchallengelima.data.model.credit.Cast>,
    @SerializedName("crew")
    val crew: List<ikhwan.binar.binarchallengelima.data.model.credit.Crew>,
    @SerializedName("id")
    val id: Int
)