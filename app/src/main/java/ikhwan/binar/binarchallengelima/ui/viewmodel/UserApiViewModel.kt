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
    val updateStatus = MutableLiveData<Boolean?>()
    val listUsers = SingleLiveEvent<List<GetUserResponseItem>>()

    val toastRegisterMessage = SingleLiveEvent<String>()
    val toastLoginMessage = SingleLiveEvent<String>()
    val loginStatus = SingleLiveEvent<Boolean>()
    val registerCheck = SingleLiveEvent<Boolean>()
    val loginCheck = SingleLiveEvent<Boolean>()


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
                    } else {
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
        ApiClient.userInstance.addUsers(user)
            .enqueue(object : Callback<GetUserResponseItem> {
                override fun onResponse(
                    call: Call<GetUserResponseItem>,
                    response: Response<GetUserResponseItem>
                ) {
                    if (!response.isSuccessful)
                        toastRegisterMessage.postValue(response.message())

                }

                override fun onFailure(call: Call<GetUserResponseItem>, t: Throwable) {
                    toastRegisterMessage.postValue(t.message)
                }
            })
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