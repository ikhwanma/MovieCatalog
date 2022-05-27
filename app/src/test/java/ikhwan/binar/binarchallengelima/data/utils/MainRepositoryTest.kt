package ikhwan.binar.binarchallengelima.data.utils

import ikhwan.binar.binarchallengelima.data.helper.ApiHelper
import ikhwan.binar.binarchallengelima.data.network.ApiService
import ikhwan.binar.binarchallengelima.data.room.Favorite
import ikhwan.binar.binarchallengelima.data.room.FavoriteDao
import ikhwan.binar.binarchallengelima.model.credit.GetCreditResponse
import ikhwan.binar.binarchallengelima.model.detailmovie.GetDetailMovieResponse
import ikhwan.binar.binarchallengelima.model.nowplaying.GetNowPlayingResponse
import ikhwan.binar.binarchallengelima.model.popularmovie.GetPopularMovieResponse
import ikhwan.binar.binarchallengelima.model.users.GetUserResponse
import ikhwan.binar.binarchallengelima.model.users.GetUserResponseItem
import ikhwan.binar.binarchallengelima.model.users.PostUserResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class MainRepositoryTest {
    private lateinit var service : ApiService
    private lateinit var apiHelper: ApiHelper
    private lateinit var repository: MainRepository
    private lateinit var favoriteDao: FavoriteDao

    private val detail = 634649
    private val fav = Favorite(1, "b", detail, "d")
    private val email = "ikhwan@gmail.com"
    private val user = PostUserResponse("a", "b", "c", "d", "e", "f")

    @Before
    fun setUp(){
        service = mockk()
        favoriteDao = mock(FavoriteDao::class.java)
        apiHelper = ApiHelper(service, service)
        repository = MainRepository(apiHelper, favoriteDao)
    }

    @Test
    fun getPopularMovie() : Unit = runBlocking {
        val response = mockk<GetPopularMovieResponse>()
        every {
            runBlocking {
                service.getPopularMovie(API_KEY)
            }
        } returns response

        repository.getPopularMovie(API_KEY)

        verify {
            runBlocking {
                service.getPopularMovie(API_KEY)
            }
        }
    }

    @Test
    fun getNowPlayingMovie(): Unit = runBlocking {
        val response = mockk<GetNowPlayingResponse>()

        every {
            runBlocking {
                service.getNowPlayingMovie(API_KEY)
            }
        } returns response

        repository.getNowPlayingMovie(API_KEY)

        verify {
            runBlocking {
                service.getNowPlayingMovie(API_KEY)
            }
        }
    }

    @Test
    fun getDetailMovie() : Unit = runBlocking{
        val response = mockk<GetDetailMovieResponse>()

        every {
            runBlocking {
                service.getDetailMovie(detail, API_KEY)
            }
        } returns response

        repository.getDetailMovie(detail, API_KEY)

        verify {
            runBlocking {
                service.getDetailMovie(detail, API_KEY)
            }
        }
    }

    @Test
    fun getCreditMovie() : Unit = runBlocking{
        val response = mockk<GetCreditResponse>()

        every {
            runBlocking {
                service.getCreditMovie(detail, API_KEY)
            }
        } returns response

        repository.getCreditMovie(detail, API_KEY)

        verify {
            runBlocking {
                service.getCreditMovie(detail, API_KEY)
            }
        }
    }

    @Test
    fun getSimilarMovie() : Unit = runBlocking{
        val response = mockk<GetPopularMovieResponse>()

        every {
            runBlocking {
                service.getSimilarMovie(detail, API_KEY)
            }
        } returns response

        repository.getSimilarMovie(detail, API_KEY)

        verify {
            runBlocking {
                service.getSimilarMovie(detail, API_KEY)
            }
        }
    }

    @Test
    fun getUser() : Unit = runBlocking{
        val response = mockk<GetUserResponse>()

        every {
            runBlocking {
                service.getUser(email)
            }
        } returns response

        repository.getUser(email)

        verify {
            runBlocking {
                service.getUser(email)
            }
        }
    }

    @Test
    fun addUsers() : Unit = runBlocking{
        val response = mockk<GetUserResponseItem>()

        every {
            runBlocking {
                service.addUsers(user)
            }
        } returns response

        repository.addUsers(user)

        verify {
            runBlocking {
                service.addUsers(user)
            }
        }
    }

    @Test
    fun updateUser() : Unit = runBlocking{
        val response = mockk<GetUserResponseItem>()

        every {
            runBlocking {
                service.updateUser(user, "80")
            }
        } returns response

        repository.updateUser(user, "80")

        verify {
            runBlocking {
                service.updateUser(user, "80")
            }
        }
    }

    @Test
    fun addFavorite() {
        favoriteDao.addFavorite(fav)
    }

    @Test
    fun deleteFavorite() {
        favoriteDao.deleteFavorite(fav)
    }

    @Test
    fun getFavorite() {
        favoriteDao.getFavorite(email)
    }

    companion object {
        private const val API_KEY = "63be5170b074455a7fba3a528aeea4ce"
    }
}