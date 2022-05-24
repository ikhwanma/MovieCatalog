package ikhwan.binar.binarchallengelima.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert
    fun addFavorite(favorite: Favorite): Long

    @Delete
    fun deleteFavorite(favorite: Favorite) : Int

    @Query("SELECT * FROM Favorite WHERE Favorite.email = :email")
    fun getFavorite(email : String) : LiveData<List<Favorite>>
}