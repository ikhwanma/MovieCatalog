package ikhwan.binar.binarchallengelima.data.helper

import ikhwan.binar.binarchallengelima.data.model.users.GetUserResponseItem
import ikhwan.binar.binarchallengelima.data.model.users.PostUserResponse
import ikhwan.binar.binarchallengelima.data.network.ApiService
import retrofit2.Call
import retrofit2.http.Body

class ApiHelper(private val apiService: ApiService) {
    suspend fun getPopularMovies(apiKey : String) = apiService.getPopularMovie(apiKey)
    suspend fun getNowPlayingMovies(apiKey : String) = apiService.getNowPlayingMovie(apiKey)
    suspend fun getDetailMovie(id: Int, apiKey : String) = apiService.getDetailMovie(id, apiKey)
    suspend fun getCreditMovie(id: Int, apiKey : String) = apiService.getCreditMovie(id, apiKey)
    suspend fun getSimilarMovie(id: Int, apiKey : String) = apiService.getSimilarMovie(id, apiKey)

    suspend fun getAllUsers() = apiService.getAllUsers()
    suspend fun addUsers(user : PostUserResponse) = apiService.addUsers(user)
    suspend fun updateUser(user : PostUserResponse, id:String) = apiService.updateUser(user, id)
}