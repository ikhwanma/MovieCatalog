package ikhwan.binar.binarchallengelima.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import ikhwan.binar.binarchallengelima.data.model.credit.GetCreditResponse
import ikhwan.binar.binarchallengelima.data.model.detailmovie.GetDetailMovieResponse
import ikhwan.binar.binarchallengelima.data.model.nowplaying.GetNowPlayingResponse
import ikhwan.binar.binarchallengelima.data.model.popularmovie.GetPopularMovieResponse
import ikhwan.binar.binarchallengelima.data.model.popularmovie.ResultMovie
import ikhwan.binar.binarchallengelima.data.network.ApiClient
import ikhwan.binar.binarchallengelima.data.utils.MainRepository
import ikhwan.binar.binarchallengelima.data.utils.Resource
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MovieApiViewModel(private val mainRepository: MainRepository) : ViewModel() {
    val apiKey = MutableLiveData<String>()

    val isSuccess = MutableLiveData<Boolean>()
    val id = MutableLiveData<Int>()
    val data = MutableLiveData<GetDetailMovieResponse>()
    val cast = MutableLiveData<GetCreditResponse>()
    val similar = MutableLiveData<List<ResultMovie>>()
    val detailFailMessage = MutableLiveData<String?>()

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


    fun getSimilar(id: Int){
        /*detailFailMessage.postValue(null)
        ApiClient.instance.getSimiliarMovie(id, apiKey.value!!)
            .enqueue(object : Callback<GetPopularMovieResponse>{
                override fun onResponse(
                    call: Call<GetPopularMovieResponse>,
                    response: Response<GetPopularMovieResponse>
                ) {
                    if (response.isSuccessful){
                        val body = response.body()
                        similar.postValue(body?.resultMovies)
                    }else{
                        detailFailMessage.postValue(response.message())
                    }
                }

                override fun onFailure(call: Call<GetPopularMovieResponse>, t: Throwable) {
                    detailFailMessage.postValue(t.message)
                }

            })*/
    }
}