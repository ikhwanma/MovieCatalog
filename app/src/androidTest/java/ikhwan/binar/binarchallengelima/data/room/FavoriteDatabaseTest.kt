package ikhwan.binar.binarchallengelima.data.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteDatabaseTest : TestCase(){
    private lateinit var mDb:FavoriteDatabase
    private lateinit var favoriteDao: FavoriteDao

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        mDb = Room.inMemoryDatabaseBuilder(context, FavoriteDatabase::class.java).build()
        favoriteDao = mDb.favoriteDao()
    }

    @After
    public override fun tearDown() {
        mDb.close()
    }

    @Test
    fun favoriteDao() {
        val result = favoriteDao.getFavorite("ikhwan@gmail.com")
    }
}