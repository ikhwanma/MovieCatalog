package ikhwan.binar.binarchallengelima.data.utils

import ikhwan.binar.binarchallengelima.data.helper.ApiHelper
import ikhwan.binar.binarchallengelima.data.model.users.PostUserResponse

class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun getPopularMovie(apiKey : String) = apiHelper.getPopularMovies(apiKey)
    suspend fun getNowPlayingMovie(apiKey : String) = apiHelper.getNowPlayingMovies(apiKey)
    suspend fun getDetailMovie(id : Int, apiKey : String) = apiHelper.getDetailMovie(id, apiKey)
    suspend fun getCreditMovie(id: Int, apiKey : String) = apiHelper.getCreditMovie(id, apiKey)
    suspend fun getSimilarMovie(id: Int, apiKey : String) = apiHelper.getSimilarMovie(id, apiKey)

    suspend fun getAllUsers() = apiHelper.getAllUsers()
    suspend fun addUsers(user : PostUserResponse) = apiHelper.addUsers(user)
    suspend fun updateUser(user : PostUserResponse, id:String) = apiHelper.updateUser(user, id)
}