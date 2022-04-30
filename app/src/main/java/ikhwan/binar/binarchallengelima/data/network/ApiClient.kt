package ikhwan.binar.binarchallengelima.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/"
        private const val BASE_USER_URL = "https://625a859b43fda1299a186b77.mockapi.io/themovie/"

        private val logging : HttpLoggingInterceptor
        get() {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            return httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
        }

        private val client = OkHttpClient.Builder().addInterceptor(ikhwan.binar.binarchallengelima.data.network.ApiClient.Companion.logging).build()

        val instance: ikhwan.binar.binarchallengelima.data.network.ApiService by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(ikhwan.binar.binarchallengelima.data.network.ApiClient.Companion.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ikhwan.binar.binarchallengelima.data.network.ApiClient.Companion.client)
                .build()
            retrofit.create(ikhwan.binar.binarchallengelima.data.network.ApiService::class.java)
        }

        val userInstance: ikhwan.binar.binarchallengelima.data.network.ApiService by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(ikhwan.binar.binarchallengelima.data.network.ApiClient.Companion.BASE_USER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ikhwan.binar.binarchallengelima.data.network.ApiClient.Companion.client)
                .build()
            retrofit.create(ikhwan.binar.binarchallengelima.data.network.ApiService::class.java)
        }

    }
}