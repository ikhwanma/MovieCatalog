package ikhwan.binar.binarchallengelima.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ikhwan.binar.binarchallengelima.data.helper.ApiHelper
import ikhwan.binar.binarchallengelima.data.utils.MainRepository
import java.lang.IllegalArgumentException

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieApiViewModel::class.java)){
            return MovieApiViewModel(MainRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}