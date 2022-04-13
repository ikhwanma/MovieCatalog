package ikhwan.binar.binarchallengelima.network

import ikhwan.binar.binarchallengelima.model.credit.GetCreditResponse
import ikhwan.binar.binarchallengelima.model.detailmovie.GetDetailMovieResponse
import ikhwan.binar.binarchallengelima.model.popularmovie.GetPopularMovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("/3/movie/popular?api_key=63be5170b074455a7fba3a528aeea4ce")
    fun getPopularMovie() : Call<GetPopularMovieResponse>

    @GET("/3/movie/{movie_id}?api_key=63be5170b074455a7fba3a528aeea4ce")
    fun getDetailMovie( @Path("movie_id") movieId : Int) : Call<GetDetailMovieResponse>

    @GET("/3/movie/{movie_id}/credits?api_key=63be5170b074455a7fba3a528aeea4ce")
    fun getCreditMovie(
        @Path("movie_id") movieId: Int
    ) : Call<GetCreditResponse>

}