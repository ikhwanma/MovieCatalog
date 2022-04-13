package ikhwan.binar.binarchallengelima.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ikhwan.binar.binarchallengelima.model.credit.GetCreditResponse
import ikhwan.binar.binarchallengelima.model.detailmovie.GetDetailMovieResponse
import ikhwan.binar.binarchallengelima.model.popularmovie.ResultMovie
import ikhwan.binar.binarchallengelima.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private var id: Int? = null

    fun setId(id : Int){
        this.id = id
    }

    private val _data = MutableLiveData<GetDetailMovieResponse>()
    val data: LiveData<GetDetailMovieResponse> = _data

    private val _cast = MutableLiveData<GetCreditResponse>()
    val cast: LiveData<GetCreditResponse> = _cast

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun getData(){
        ApiClient.instance.getDetailMovie(id!!)
            .enqueue(object : Callback<GetDetailMovieResponse>{
                override fun onResponse(
                    call: Call<GetDetailMovieResponse>,
                    response: Response<GetDetailMovieResponse>
                ) {
                    val body = response.body()
                    val code = response.code()
                    if (code == 200){
                        _data.postValue(body!!)
                        _isSuccess.postValue(true)
                    }
                }

                override fun onFailure(call: Call<GetDetailMovieResponse>, t: Throwable) {

                }

            })
    }

    fun getCast(){
        ApiClient.instance.getCreditMovie(id!!)
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
}