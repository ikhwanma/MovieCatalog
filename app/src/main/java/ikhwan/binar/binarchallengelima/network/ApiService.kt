package ikhwan.binar.binarchallengelima.network

import ikhwan.binar.binarchallengelima.model.credit.GetCreditResponse
import ikhwan.binar.binarchallengelima.model.detailmovie.GetDetailMovieResponse
import ikhwan.binar.binarchallengelima.model.nowplaying.GetNowPlayingResponse
import ikhwan.binar.binarchallengelima.model.popularmovie.GetPopularMovieResponse
import ikhwan.binar.binarchallengelima.model.users.GetUserResponseItem
import ikhwan.binar.binarchallengelima.model.users.PostUserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    //=====================Movie========================
    @GET("/3/movie/popular")
    fun getPopularMovie(
        @Query("api_key") api_key: String
    ): Call<GetPopularMovieResponse>

    @GET("/3/movie/{movie_id}")
    fun getDetailMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") api_key: String
    ): Call<GetDetailMovieResponse>

    @GET("/3/movie/{movie_id}/credits")
    fun getCreditMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") api_key: String
    ): Call<GetCreditResponse>


    @GET("/3/movie/now_playing")
    fun getNowPlayingMovie(
        @Query("api_key") api_key: String
    ): Call<GetNowPlayingResponse>

    //=====================User========================
    @GET("/users")
    fun getAllUsers() : Call<List<GetUserResponseItem>>

    @POST("/users")
    fun addUsers(
        @Body user : PostUserResponse
    ): Call<GetUserResponseItem>
}