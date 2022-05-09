package ikhwan.binar.binarchallengelima.viewmodel

import androidx.lifecycle.*
import ikhwan.binar.binarchallengelima.data.datastore.DataStoreManager
import ikhwan.binar.binarchallengelima.model.detailmovie.GetDetailMovieResponse
import ikhwan.binar.binarchallengelima.data.utils.MainRepository
import ikhwan.binar.binarchallengelima.data.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MovieApiViewModel(private val mainRepository: MainRepository, private val pref: DataStoreManager) : ViewModel() {
    val apiKey = MutableLiveData<String>()

    val id = MutableLiveData<Int>()
    val data = MutableLiveData<GetDetailMovieResponse>()

    fun setBoolean(boolean: Boolean){
        viewModelScope.launch {
            pref.setViewHome(boolean)
        }
    }

    fun getBoolean() : LiveData<Boolean>{
        return pref.getBoolean().asLiveData()
    }

    fun getPopularMovie() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.getPopularMovie(apiKey.value!!)))
        }catch (e: Exception){
            emit(Resource.error(data = null, message = e.message?:"Error Occured!"))
        }
    }

    fun getNowPlaying() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.getNowPlayingMovie(apiKey.value!!)))
        }catch (e: Exception){
            emit(Resource.error(data = null, message = e.message?:"Error Occured!"))
        }
    }

    fun getDetailMovie(id : Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.getDetailMovie(id, apiKey.value!!)))
        }catch (e: Exception){
            emit(Resource.error(data = null, message = e.message?:"Error Occured!"))
        }
    }

    fun getCreditMovie(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.getCreditMovie(id, apiKey.value!!)))
        }catch (e: Exception){
            emit(Resource.error(data = null, message = e.message?:"Error Occured!"))
        }
    }

    fun getSimilarMovie(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.getSimilarMovie(id, apiKey.value!!)))
        }catch (e: Exception){
            emit(Resource.error(data = null, message = e.message?:"Error Occured!"))
        }
    }

}