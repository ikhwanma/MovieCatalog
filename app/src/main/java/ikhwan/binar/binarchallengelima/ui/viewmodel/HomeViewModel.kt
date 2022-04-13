package ikhwan.binar.binarchallengelima.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ikhwan.binar.binarchallengelima.model.GetPopularMovieResponse
import ikhwan.binar.binarchallengelima.model.Result
import ikhwan.binar.binarchallengelima.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel(){

    private val _listData = MutableLiveData<List<Result>>()
    val listData: LiveData<List<Result>> = _listData

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
                        _listData.postValue(body?.results)
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