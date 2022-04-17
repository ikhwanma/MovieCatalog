package ikhwan.binar.binarchallengelima.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ikhwan.binar.binarchallengelima.model.credit.GetCreditResponse
import ikhwan.binar.binarchallengelima.model.detailmovie.GetDetailMovieResponse
import ikhwan.binar.binarchallengelima.model.nowplaying.GetNowPlayingResponse
import ikhwan.binar.binarchallengelima.model.nowplaying.ResultNow
import ikhwan.binar.binarchallengelima.model.popularmovie.GetPopularMovieResponse
import ikhwan.binar.binarchallengelima.model.popularmovie.ResultMovie
import ikhwan.binar.binarchallengelima.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiViewModel : ViewModel() {
    private val _apiKey = MutableLiveData<String>()
    private val apiKey: LiveData<String> = _apiKey

    fun setApiKey(key: String){
        _apiKey.value = key
    }

    private val _listData = MutableLiveData<List<ResultMovie>>()
    val listData: LiveData<List<ResultMovie>> = _listData

    private val _listNowData = MutableLiveData<GetNowPlayingResponse>()
    val listNowData: LiveData<GetNowPlayingResponse> = _listNowData

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _failMessage = MutableLiveData<String>()
    val failMessage: LiveData<String> = _failMessage

    private val _id = MutableLiveData<Int>()
    val id: LiveData<Int> = _id

    fun setId(ids: Int){
        _id.postValue(ids)
    }

    private val _data = MutableLiveData<GetDetailMovieResponse>()
    val data: LiveData<GetDetailMovieResponse> = _data

    private val _cast = MutableLiveData<GetCreditResponse>()
    val cast: LiveData<GetCreditResponse> = _cast

    private val _similiar = MutableLiveData<List<ResultMovie>>()
    val similiar: LiveData<List<ResultMovie>> = _similiar

    fun getListMovieData() {
        ApiClient.instance.getPopularMovie(apiKey.value!!)
            .enqueue(object : Callback<GetPopularMovieResponse> {
                override fun onResponse(
                    call: Call<GetPopularMovieResponse>,
                    response: Response<GetPopularMovieResponse>
                ) {
                    val body = response.body()
                    val code = response.code()
                    if (code == 200){
                        _listData.postValue(body?.resultMovies)
                        _isSuccess.postValue(true)
                    }else{
                        _failMessage.postValue(response.message())
                    }
                }
                override fun onFailure(call: Call<GetPopularMovieResponse>, t: Throwable) {
                    _failMessage.postValue(t.message)
                }
            })
    }

    fun getListNowPlaying(){
        ApiClient.instance.getNowPlayingMovie(apiKey.value!!)
            .enqueue(object :Callback<GetNowPlayingResponse>{
                override fun onResponse(
                    call: Call<GetNowPlayingResponse>,
                    response: Response<GetNowPlayingResponse>
                ) {
                    if (response.isSuccessful){
                        _listNowData.postValue(response.body())
                    }else{
                        _failMessage.postValue(response.message())
                    }
                }

                override fun onFailure(call: Call<GetNowPlayingResponse>, t: Throwable) {
                    _failMessage.postValue(t.message)
                }

            })
    }

    fun getData(id : Int){
        ApiClient.instance.getDetailMovie(id,apiKey.value!!)
            .enqueue(object : Callback<GetDetailMovieResponse>{
                override fun onResponse(
                    call: Call<GetDetailMovieResponse>,
                    response: Response<GetDetailMovieResponse>
                ) {
                    val code = response.code()
                    if (code == 200){
                        _data.postValue(response.body())
                        _isSuccess.postValue(true)
                    }
                }

                override fun onFailure(call: Call<GetDetailMovieResponse>, t: Throwable) {

                }

            })
    }

    fun getCast(id: Int){
        _isSuccess.postValue(false)
        ApiClient.instance.getCreditMovie(id,apiKey.value!!)
            .enqueue(object : Callback<GetCreditResponse>{
                override fun onResponse(
                    call: Call<GetCreditResponse>,
                    response: Response<GetCreditResponse>
                ) {
                    if (response.isSuccessful){
                        val body = response.body()
                        _cast.postValue(body!!)
                    }
                }

                override fun onFailure(call: Call<GetCreditResponse>, t: Throwable) {

                }

            })
    }

    fun getSimiliar(id: Int){
        ApiClient.instance.getSimiliarMovie(id, apiKey.value!!)
            .enqueue(object : Callback<GetPopularMovieResponse>{
                override fun onResponse(
                    call: Call<GetPopularMovieResponse>,
                    response: Response<GetPopularMovieResponse>
                ) {
                    if (response.isSuccessful){
                        val body = response.body()
                        _similiar.postValue(body?.resultMovies)
                    }
                }

                override fun onFailure(call: Call<GetPopularMovieResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }
}