package ikhwan.binar.binarchallengelima.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ikhwan.binar.binarchallengelima.database.User
import ikhwan.binar.binarchallengelima.database.UserDatabase
import ikhwan.binar.binarchallengelima.model.popularmovie.GetPopularMovieResponse
import ikhwan.binar.binarchallengelima.model.popularmovie.ResultMovie
import ikhwan.binar.binarchallengelima.network.ApiClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HomeViewModel : ViewModel(){

    private var userDatabase: UserDatabase? = null

    fun setUserDb(userDatabase: UserDatabase) {
        this.userDatabase = userDatabase
    }

    private val _listData = MutableLiveData<List<ResultMovie>>()
    val listData: LiveData<List<ResultMovie>> = _listData

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

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

    fun getUser(email: String){
        GlobalScope.async {
            val result = userDatabase?.userDao()?.getUserRegistered(email)
            _user.postValue(result)
        }
    }
}