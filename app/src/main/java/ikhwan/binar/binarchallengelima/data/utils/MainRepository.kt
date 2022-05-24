package ikhwan.binar.binarchallengelima.data.utils

import androidx.lifecycle.LiveData
import ikhwan.binar.binarchallengelima.data.helper.ApiHelper
import ikhwan.binar.binarchallengelima.data.room.Favorite
import ikhwan.binar.binarchallengelima.data.room.FavoriteDao
import ikhwan.binar.binarchallengelima.model.users.PostUserResponse
import javax.inject.Inject

class MainRepository@Inject constructor(private val apiHelper: ApiHelper, private val favoriteDao: FavoriteDao) {


    suspend fun getPopularMovie(apiKey: String) = apiHelper.getPopularMovies(apiKey)
    suspend fun getNowPlayingMovie(apiKey: String) = apiHelper.getNowPlayingMovies(apiKey)
    suspend fun getDetailMovie(id: Int, apiKey: String) = apiHelper.getDetailMovie(id, apiKey)
    suspend fun getCreditMovie(id: Int, apiKey: String) = apiHelper.getCreditMovie(id, apiKey)
    suspend fun getSimilarMovie(id: Int, apiKey: String) = apiHelper.getSimilarMovie(id, apiKey)

    suspend fun getUser(email: String) = apiHelper.getUser(email)
    suspend fun addUsers(user: PostUserResponse) = apiHelper.addUsers(user)
    suspend fun updateUser(user: PostUserResponse, id: String) = apiHelper.updateUser(user, id)

    fun addFavorite(favorite: Favorite) = favoriteDao.addFavorite(favorite)
    fun deleteFavorite(favorite: Favorite) = favoriteDao.deleteFavorite(favorite)
    fun getFavorite(email: String) : LiveData<List<Favorite>> = favoriteDao.getFavorite(email)
}