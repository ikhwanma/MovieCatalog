package ikhwan.binar.binarchallengelima.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ikhwan.binar.binarchallengelima.data.datastore.DataStoreManager
import ikhwan.binar.binarchallengelima.data.network.ApiService
import ikhwan.binar.binarchallengelima.data.room.FavoriteDao
import ikhwan.binar.binarchallengelima.data.room.FavoriteDatabase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MovieModule {
    private const val BASE_URL = "https://api.themoviedb.org/"
    private const val BASE_USER_URL = "https://625a859b43fda1299a186b77.mockapi.io/themovie/"

    private val logging : HttpLoggingInterceptor
        get() {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            return httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
        }
    private val client = OkHttpClient.Builder().addInterceptor(logging).build()

    @Provides
    @Singleton
    @Named("User")
    fun providesRetrofitUser(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_USER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    @Named("Movie")
    fun providesInstanceMovie(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase (@ApplicationContext appContext: Context): FavoriteDatabase {
        return Room.databaseBuilder(
            appContext,
            FavoriteDatabase::class.java,
            "Favorite.db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesInstanceRoom(favoriteDatabase: FavoriteDatabase): FavoriteDao = favoriteDatabase.favoriteDao()

    @Provides
    @Singleton
    fun providesPref(@ApplicationContext appContext: Context) : DataStoreManager =  DataStoreManager(appContext)
}