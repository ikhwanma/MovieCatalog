package ikhwan.binar.binarchallengelima.data.helper

import ikhwan.binar.binarchallengelima.data.network.ApiService

class ApiHelper(private val apiService: ApiService) {
    suspend fun getPopularMovies(apiKey : String) = apiService.getPopularMovie(apiKey)
    suspend fun getNowPlayingMovies(apiKey : String) = apiService.getNowPlayingMovie(apiKey)
    suspend fun getDetailMovie(id: Int, apiKey : String) = apiService.getDetailMovie(id, apiKey)
    suspend fun getCreditMovie(id: Int, apiKey : String) = apiService.getCreditMovie(id, apiKey)
    suspend fun getSimilarMovie(id: Int, apiKey : String) = apiService.getSimilarMovie(id, apiKey)
}