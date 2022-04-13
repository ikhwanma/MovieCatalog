package ikhwan.binar.binarchallengelima.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ikhwan.binar.binarchallengelima.database.UserDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class LoginViewModel : ViewModel() {
    private var userDatabase: UserDatabase? = null

    fun setUserDb(userDatabase: UserDatabase) {
        this.userDatabase = userDatabase
    }

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> = _loginStatus

    fun loginUser(email: String, password: String) {
        _loginStatus.postValue(false)
        GlobalScope.async {
            val user = userDatabase?.userDao()?.getUserRegistered(email)
            if (user != null) {
                if (email == user.email && password == user.password) {
                    _loginStatus.postValue(true)
                } else {
                    _toastMessage.postValue("Password yang anda masukkan salah")
                }
            } else {
                _toastMessage.postValue("Akun dengan email $email belum terdaftar")
            }
        }
    }
}