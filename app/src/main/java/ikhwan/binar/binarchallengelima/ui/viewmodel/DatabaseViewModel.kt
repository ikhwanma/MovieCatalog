package ikhwan.binar.binarchallengelima.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ikhwan.binar.binarchallengelima.database.User
import ikhwan.binar.binarchallengelima.database.UserDatabase

class DatabaseViewModel(application: Application): ViewModel() {
    private val userDatabase : UserDatabase = UserDatabase.getInstance(application)!!

    fun registerUser(user: User) : Long = userDatabase.userDao().registerUser(user)
    fun getUserRegistered(email: String) : LiveData<User> = userDatabase.userDao().getUserRegistered(email)
    fun updateUser(user: User) : Int = userDatabase.userDao().updateUser(user)
}