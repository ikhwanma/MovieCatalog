package ikhwan.binar.binarchallengelima.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import ikhwan.binar.binarchallengelima.data.model.users.GetUserResponseItem
import ikhwan.binar.binarchallengelima.data.model.users.PostUserResponse
import ikhwan.binar.binarchallengelima.data.utils.MainRepository
import ikhwan.binar.binarchallengelima.data.utils.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class UserApiViewModel(private val mainRepository: MainRepository) : ViewModel() {

    val user = MutableLiveData<GetUserResponseItem>()
    val loginStatus = MutableLiveData<Boolean>()
    val registerCheck = MutableLiveData<Boolean>()


    fun getAllUsers() = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.getAllUsers()))
        }catch (e: Exception){
            emit(Resource.error(data = null, message = e.message?:"Error Occured!"))
        }
    }

    fun registerUser(user: PostUserResponse) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.addUsers(user)))
        }catch (e: Exception){
            emit(Resource.error(data = null, message = e.message?:"Error Occured!"))
        }
    }

    fun updateUser(user: PostUserResponse, id: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.updateUser(user, id)))
        }catch (e: Exception){
            emit(Resource.error(data = null, message = e.message?:"Error Occured!"))
        }
    }
}