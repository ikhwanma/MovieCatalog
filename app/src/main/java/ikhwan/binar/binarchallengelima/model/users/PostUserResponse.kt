package ikhwan.binar.binarchallengelima.model.users

import com.google.gson.annotations.SerializedName

data class PostUserResponse(
    @SerializedName("address")
    val address: String,
    @SerializedName("birthDate")
    val birthDate: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("username")
    val username: String
)