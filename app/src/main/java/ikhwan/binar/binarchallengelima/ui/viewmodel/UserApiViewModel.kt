package ikhwan.binar.binarchallengelima.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ikhwan.binar.binarchallengelima.helper.SingleLiveEvent
import ikhwan.binar.binarchallengelima.model.users.GetUserResponseItem
import ikhwan.binar.binarchallengelima.model.users.PostUserResponse
import ikhwan.binar.binarchallengelima.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserApiViewModel : ViewModel() {

    val user = MutableLiveData<GetUserResponseItem>()
    val loginStatus = SingleLiveEvent<Boolean>()
    val updateStatus = MutableLiveData<Boolean?>()
    val toastRegisterMessage = MutableLiveData<String>()
    val toastLoginMessage = SingleLiveEvent<String>()

    val listUsers = SingleLiveEvent<List<GetUserResponseItem>>()
    val loginCheck = SingleLiveEvent<Boolean>()
    val registerStatus = SingleLiveEvent<Boolean>()


    fun getAllUsers() {
        ApiClient.userInstance.getAllUsers()
            .enqueue(object : Callback<List<GetUserResponseItem>> {
                override fun onResponse(
                    call: Call<List<GetUserResponseItem>>,
                    response: Response<List<GetUserResponseItem>>
                ) {
                    if (response.isSuccessful) {
                        listUsers.postValue(response.body())
                        loginStatus.postValue(true)
                    }else{
                        loginStatus.postValue(false)
                        toastLoginMessage.postValue(response.message())
                    }
                }

                override fun onFailure(call: Call<List<GetUserResponseItem>>, t: Throwable) {
                    loginStatus.postValue(false)
                    toastLoginMessage.postValue(t.message)
                }

            })
    }

    fun registerUser(user: PostUserResponse) {
        var cek = false
        val x = listUsers.value
        for (data in x!!) {
            if (user.email == data.email) {
                cek = true
            }
        }
        registerUserCek(cek, user)
    }

    private fun registerUserCek(cek: Boolean, user: PostUserResponse) {
        if (!cek) {
            ApiClient.userInstance.addUsers(user)
                .enqueue(object : Callback<GetUserResponseItem> {
                    override fun onResponse(
                        call: Call<GetUserResponseItem>,
                        response: Response<GetUserResponseItem>
                    ) {
                        if (response.isSuccessful) {
                            registerStatus.postValue(true)
                        } else {
                            registerStatus.postValue(false)
                            toastRegisterMessage.postValue(response.message())
                        }
                    }

                    override fun onFailure(call: Call<GetUserResponseItem>, t: Throwable) {
                        registerStatus.postValue(false)
                        toastRegisterMessage.postValue(t.message)
                    }
                })
        } else {
            toastRegisterMessage.postValue("User dengan email ${user.email} sudah terdaftar")
            registerStatus.value = false
        }
    }

    fun updateUser(user: PostUserResponse, id: String) {
        ApiClient.userInstance.updateUser(user, id)
            .enqueue(object : Callback<GetUserResponseItem> {
                override fun onResponse(
                    call: Call<GetUserResponseItem>,
                    response: Response<GetUserResponseItem>
                ) {
                    if (response.isSuccessful) {
                        updateStatus.postValue(true)
                    }
                }

                override fun onFailure(call: Call<GetUserResponseItem>, t: Throwable) {
                    updateStatus.postValue(false)
                }

            })
    }
}