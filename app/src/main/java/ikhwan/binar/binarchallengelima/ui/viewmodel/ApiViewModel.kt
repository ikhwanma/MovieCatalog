package ikhwan.binar.binarchallengelima.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ikhwan.binar.binarchallengelima.model.credit.GetCreditResponse
import ikhwan.binar.binarchallengelima.model.detailmovie.GetDetailMovieResponse
import ikhwan.binar.binarchallengelima.model.nowplaying.GetNowPlayingResponse
import ikhwan.binar.binarchallengelima.model.popularmovie.GetPopularMovieResponse
import ikhwan.binar.binarchallengelima.model.popularmovie.ResultMovie
import ikhwan.binar.binarchallengelima.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiViewModel : ViewModel() {
    val apiKey = MutableLiveData<String>()

    val listData = MutableLiveData<List<ResultMovie>>()

    val listNowData = MutableLiveData<GetNowPlayingResponse>()
    val isSuccess = MutableLiveData<Boolean>()
    val id = MutableLiveData<Int>()
    val failMessage = MutableLiveData<String?>()
    val data = MutableLiveData<GetDetailMovieResponse>()
    val cast = MutableLiveData<GetCreditResponse>()
    val similar = MutableLiveData<List<ResultMovie>>()
    val detailFailMessage = MutableLiveData<String?>()

    fun getListMovieData() {
        failMessage.postValue(null)
        ApiClient.instance.getPopularMovie(apiKey.value!!)
            .enqueue(object : Callback<GetPopularMovieResponse> {
                override fun onResponse(
                    call: Call<GetPopularMovieResponse>,
                    response: Response<GetPopularMovieResponse>
                ) {
                    val body = response.body()
                    val code = response.code()
                    if (code == 200){
                        listData.postValue(body?.resultMovies)
                        isSuccess.postValue(true)
                    }else{
                        failMessage.postValue(response.message())
                    }
                }
                override fun onFailure(call: Call<GetPopularMovieResponse>, t: Throwable) {
                    failMessage.postValue(t.message)
                }
            })
    }

    fun getListNowPlaying(){
        failMessage.postValue(null)
        ApiClient.instance.getNowPlayingMovie(apiKey.value!!)
            .enqueue(object :Callback<GetNowPlayingResponse>{
                override fun onResponse(
                    call: Call<GetNowPlayingResponse>,
                    response: Response<GetNowPlayingResponse>
                ) {
                    if (response.isSuccessful){
                        listNowData.postValue(response.body())
                    }else{
                        failMessage.postValue(response.message())
                    }
                }

                override fun onFailure(call: Call<GetNowPlayingResponse>, t: Throwable) {
                    failMessage.postValue(t.message)
                }

            })
    }

    fun getData(id : Int){
        detailFailMessage.postValue(null)
        ApiClient.instance.getDetailMovie(id,apiKey.value!!)
            .enqueue(object : Callback<GetDetailMovieResponse>{
                override fun onResponse(
                    call: Call<GetDetailMovieResponse>,
                    response: Response<GetDetailMovieResponse>
                ) {
                    val code = response.code()
                    if (code == 200){
                        data.postValue(response.body())
                        isSuccess.postValue(true)
                    }
                    else{
                        detailFailMessage.postValue(response.message())
                    }
                }

                override fun onFailure(call: Call<GetDetailMovieResponse>, t: Throwable) {
                    detailFailMessage.postValue(t.message)
                }

            })
    }

    fun getCast(id: Int){
        detailFailMessage.postValue(null)
        isSuccess.postValue(false)
        ApiClient.instance.getCreditMovie(id,apiKey.value!!)
            .enqueue(object : Callback<GetCreditResponse>{
                override fun onResponse(
                    call: Call<GetCreditResponse>,
                    response: Response<GetCreditResponse>
                ) {
                    if (response.isSuccessful){
                        val body = response.body()
                        cast.postValue(body!!)
                    }else{
                        detailFailMessage.postValue(response.message())
                    }
                }

                override fun onFailure(call: Call<GetCreditResponse>, t: Throwable) {
                    detailFailMessage.postValue(t.message)
                }

            })
    }

    fun getSimilar(id: Int){
        detailFailMessage.postValue(null)
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

            })
    }
}