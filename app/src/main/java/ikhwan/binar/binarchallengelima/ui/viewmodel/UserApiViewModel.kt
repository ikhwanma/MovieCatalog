package ikhwan.binar.binarchallengelima.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import ikhwan.binar.binarchallengelima.model.users.GetUserResponseItem
import ikhwan.binar.binarchallengelima.model.users.PostUserResponse
import ikhwan.binar.binarchallengelima.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserApiViewModel : ViewModel(){

    private val _listUsers = MutableLiveData<List<GetUserResponseItem>>()
    val listUsers: LiveData<List<GetUserResponseItem>> = _listUsers

    private val _user = MutableLiveData<GetUserResponseItem>()
    val user: LiveData<GetUserResponseItem> = _user

    fun setUser(user: GetUserResponseItem){
        _user.postValue(user)
    }

    private val _toastRegisterMessage = MutableLiveData<String>()
    val toastRegisterMessage: LiveData<String> = _toastRegisterMessage

    private val _registerStatus = MutableLiveData<Boolean?>()
    val registerStatus: LiveData<Boolean?> = _registerStatus

    private val _updateStatus = MutableLiveData<Boolean?>()
    val updateStatus: LiveData<Boolean?> = _updateStatus

    fun getAllUsers(){
        ApiClient.userInstance.getAllUsers()
            .enqueue(object :Callback<List<GetUserResponseItem>>{
                override fun onResponse(
                    call: Call<List<GetUserResponseItem>>,
                    response: Response<List<GetUserResponseItem>>
                ) {
                    if (response.isSuccessful){
                        _listUsers.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<List<GetUserResponseItem>>, t: Throwable) {

                }

            })
    }

    fun registerUser(user : PostUserResponse){
        _registerStatus.postValue(null)
        var cek = true
        val x = listUsers.value
        for (data in x!!){
            if (user.email == data.email){
                cek = false
            }
        }
        if (cek){
            ApiClient.userInstance.addUsers(user)
                .enqueue(object : Callback<GetUserResponseItem>{
                    override fun onResponse(
                        call: Call<GetUserResponseItem>,
                        response: Response<GetUserResponseItem>
                    ) {
                        if (response.isSuccessful){
                            _registerStatus.postValue(true)
                        }else{
                            _registerStatus.postValue(false)
                            _toastRegisterMessage.postValue(response.message())
                        }
                    }

                    override fun onFailure(call: Call<GetUserResponseItem>, t: Throwable) {
                        _registerStatus.postValue(false)
                        _toastRegisterMessage.postValue(t.message)
                    }
                })
        }else{
            _toastRegisterMessage.postValue("User dengan email ${user.email} sudah terdaftar")
            _registerStatus.postValue(false)
        }
    }

    fun updateUser(user: PostUserResponse, id: String){
        ApiClient.userInstance.updateUser(user, id)
            .enqueue(object : Callback<GetUserResponseItem>{
                override fun onResponse(
                    call: Call<GetUserResponseItem>,
                    response: Response<GetUserResponseItem>
                ) {
                    if (response.isSuccessful){
                        _updateStatus.postValue(true)
                    }
                }

                override fun onFailure(call: Call<GetUserResponseItem>, t: Throwable) {
                    _updateStatus.postValue(false)
                }

            })
    }
}