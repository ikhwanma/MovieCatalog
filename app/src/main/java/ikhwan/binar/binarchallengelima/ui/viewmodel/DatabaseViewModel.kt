package ikhwan.binar.binarchallengelima.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ikhwan.binar.binarchallengelima.database.User
import ikhwan.binar.binarchallengelima.database.UserDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class DatabaseViewModel : ViewModel(){
    private var userDatabase: UserDatabase? = null

    fun setUserDb(userDatabase: UserDatabase) {
        this.userDatabase = userDatabase
    }

    private val _toastRegisterMessage = MutableLiveData<String>()
    val toastRegisterMessage: LiveData<String> = _toastRegisterMessage

    private val _toastLoginMessage = MutableLiveData<String>()
    val toastLoginMessage: LiveData<String> = _toastLoginMessage

    private val _registerStatus = MutableLiveData<Boolean>()
    val registerStatus: LiveData<Boolean> = _registerStatus

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> = _loginStatus

    fun userRegister(user: User, email: String) {
        GlobalScope.async {
            _registerStatus.postValue(false)
            val cekUser = userDatabase?.userDao()?.getUserRegistered(email)
            if (cekUser != null) {
                _toastRegisterMessage.postValue("User dengan email ${user.email} sudah terdaftar")
            } else {
                val result = userDatabase?.userDao()?.registerUser(user)
                if (result != 0.toLong()) {
                    _toastRegisterMessage.postValue("Sukses mendaftarkan ${user.email}, silakan mencoba untuk login")
                    _registerStatus.postValue(true)
                } else {
                    _toastRegisterMessage.postValue("Gagal mendaftarkan ${user.email}, silakan coba lagi")
                }
            }
        }
    }

    fun loginUser(email: String, password: String) {
        _loginStatus.postValue(false)
        GlobalScope.async {
            val user = userDatabase?.userDao()?.getUserRegistered(email)
            if (user != null) {
                if (email == user.email && password == user.password) {
                    _loginStatus.postValue(true)
                } else {
                    _toastLoginMessage.postValue("Password yang anda masukkan salah")
                }
            } else {
                _toastLoginMessage.postValue("Akun dengan email $email belum terdaftar")
            }
        }
    }

    fun getUser(email: String){
        GlobalScope.async {
            val result = userDatabase?.userDao()?.getUserRegistered(email)
            _user.postValue(result!!)
        }
    }
}