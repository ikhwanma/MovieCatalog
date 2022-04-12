package ikhwan.binar.binarchallengelima.network

import ikhwan.binar.binarchallengelima.model.GetPopularMovieResponse
import ikhwan.binar.binarchallengelima.model.Result
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("/3/movie/popular?api_key=63be5170b074455a7fba3a528aeea4ce")
    fun getPopularMovie() : Call<GetPopularMovieResponse>

}