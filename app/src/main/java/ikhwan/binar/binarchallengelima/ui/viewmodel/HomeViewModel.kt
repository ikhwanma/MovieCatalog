package ikhwan.binar.binarchallengelima.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ikhwan.binar.binarchallengelima.model.popularmovie.GetPopularMovieResponse
import ikhwan.binar.binarchallengelima.model.popularmovie.ResultMovie
import ikhwan.binar.binarchallengelima.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel(){

    private val _listData = MutableLiveData<List<ResultMovie>>()
    val listData: LiveData<List<ResultMovie>> = _listData

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _failMessage = MutableLiveData<String>()
    val failMessage: LiveData<String> = _failMessage

    fun fetchData() {
        ApiClient.instance.getPopularMovie()
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
}