package ikhwan.binar.binarchallengelima.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert
    fun registerUser(user: User):Long

    @Query("SELECT * FROM User WHERE User.email = :email")
    fun getUserRegistered(email:String): User

    @Update
    fun updateUser(user: User) : Int
}