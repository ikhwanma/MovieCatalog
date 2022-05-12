package ikhwan.binar.binarchallengelima.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ikhwan.binar.binarchallengelima.data.datastore.DataStoreManager
import ikhwan.binar.binarchallengelima.data.helper.ApiHelper
import ikhwan.binar.binarchallengelima.data.room.FavoriteDao
import ikhwan.binar.binarchallengelima.data.utils.MainRepository
import java.lang.IllegalArgumentException

class ViewModelFactory(private val apiHelper: ApiHelper, private val pref: DataStoreManager, private val favoriteDao: FavoriteDao) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieApiViewModel::class.java)) {
            return MovieApiViewModel(MainRepository(apiHelper, favoriteDao), pref) as T
        }
        if (modelClass.isAssignableFrom(UserApiViewModel::class.java)) {
            return UserApiViewModel(MainRepository(apiHelper, favoriteDao), pref) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}