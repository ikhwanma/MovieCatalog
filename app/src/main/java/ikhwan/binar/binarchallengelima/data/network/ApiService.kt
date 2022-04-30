package ikhwan.binar.binarchallengelima.data.network

import ikhwan.binar.binarchallengelima.data.model.credit.GetCreditResponse
import ikhwan.binar.binarchallengelima.data.model.detailmovie.GetDetailMovieResponse
import ikhwan.binar.binarchallengelima.data.model.nowplaying.GetNowPlayingResponse
import ikhwan.binar.binarchallengelima.data.model.popularmovie.GetPopularMovieResponse
import ikhwan.binar.binarchallengelima.data.model.popularmovie.ResultMovie
import ikhwan.binar.binarchallengelima.data.model.users.GetUserResponseItem
import ikhwan.binar.binarchallengelima.data.model.users.PostUserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    //=====================Movie========================
    @GET("/3/movie/popular")
    suspend fun getPopularMovie(
        @Query("api_key") api_key: String
    ): GetPopularMovieResponse

    @GET("/3/movie/{movie_id}")
    suspend fun getDetailMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") api_key: String
    ): GetDetailMovieResponse

    @GET("/3/movie/{movie_id}/credits")
    suspend fun getCreditMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") api_key: String
    ): GetCreditResponse


    @GET("/3/movie/now_playing")
    suspend fun getNowPlayingMovie(
        @Query("api_key") api_key: String
    ): GetNowPlayingResponse

    @GET("/3/movie/{movie_id}/similar")
    suspend fun getSimilarMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") api_key: String
    ): GetPopularMovieResponse

    //=====================User========================
    @GET("/users")
    fun getAllUsers() : Call<List<GetUserResponseItem>>

    @POST("/users")
    fun addUsers(
        @Body user : PostUserResponse
    ): Call<GetUserResponseItem>

    @PUT("/users/{id}")
    fun updateUser(
        @Body user : PostUserResponse, @Path("id") id:String
    ) : Call<GetUserResponseItem>
}