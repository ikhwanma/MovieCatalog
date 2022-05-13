package ikhwan.binar.binarchallengelima.view.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ikhwan.binar.binarchallengelima.R
import ikhwan.binar.binarchallengelima.data.datastore.DataStoreManager
import ikhwan.binar.binarchallengelima.data.helper.ApiHelper
import ikhwan.binar.binarchallengelima.model.detailmovie.GetDetailMovieResponse
import ikhwan.binar.binarchallengelima.data.network.ApiClient
import ikhwan.binar.binarchallengelima.data.room.Favorite
import ikhwan.binar.binarchallengelima.data.room.FavoriteDatabase
import ikhwan.binar.binarchallengelima.data.utils.Status
import ikhwan.binar.binarchallengelima.view.adapter.CastAdapter
import ikhwan.binar.binarchallengelima.view.adapter.SimiliarAdapter
import ikhwan.binar.binarchallengelima.databinding.FragmentDetailBinding
import ikhwan.binar.binarchallengelima.model.credit.Cast
import ikhwan.binar.binarchallengelima.model.popularmovie.ResultMovie
import ikhwan.binar.binarchallengelima.view.dialogfragment.ShowImageDialogFragment
import ikhwan.binar.binarchallengelima.viewmodel.MovieApiViewModel
import ikhwan.binar.binarchallengelima.viewmodel.UserApiViewModel
import ikhwan.binar.binarchallengelima.viewmodel.ViewModelFactory
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.coroutines.*

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelMovie: MovieApiViewModel
    private lateinit var viewModelUser: UserApiViewModel
    private lateinit var pref: DataStoreManager

    private var favoriteDatabase: FavoriteDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity?)!!.supportActionBar?.hide()

        favoriteDatabase = FavoriteDatabase.getInstance(requireContext())

        pref = DataStoreManager(requireContext())

        viewModelUser = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(ApiClient.userInstance),
                pref,
                FavoriteDatabase.getInstance(requireContext())!!
                    .favoriteDao()
            )
        )[UserApiViewModel::class.java]
        viewModelMovie = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(ApiClient.instance),
                pref,
                FavoriteDatabase.getInstance(requireContext())!!
                    .favoriteDao()
            )
        )[MovieApiViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelMovie.id.observe(viewLifecycleOwner) { id ->
            viewModelMovie.getDetailMovie(id).observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        binding.progressCircular.visibility = View.GONE
                        setDetail(it.data!!)
                    }
                    Status.LOADING -> {

                    }
                }
            }
            viewModelMovie.getCreditMovie(id).observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        showList(it.data!!.cast)
                    }
                    Status.LOADING -> {

                    }
                }
            }

            viewModelMovie.getSimilarMovie(id).observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        showListSimilar(it.data!!.resultMovies)
                    }
                    Status.LOADING -> {
                    }
                }
            }
        }
    }

    private fun setDetail(data: GetDetailMovieResponse) {
        val imgUrl = "https://image.tmdb.org/t/p/w500/${data.posterPath}"
        val date = data.releaseDate.split("-").toTypedArray()
        val genre = data.genres
        val txtTagline = "\"${data.tagline}\""
        var txtGenre = ""
        for ((index, i) in genre.withIndex()) {
            txtGenre += if (index != genre.size - 1) {
                "${i.name}, "
            } else {
                i.name
            }
        }
        binding.apply {
            Glide.with(requireView())
                .load(imgUrl)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(10, 3)))
                .into(imgBackgroundDetail)
            Glide.with(requireView()).load(imgUrl).into(imgMovie)
            imgMovie.setOnClickListener {
                ShowImageDialogFragment(imgUrl).show(requireActivity().supportFragmentManager, null)
            }
            tvMovie.text = data.title
            tvMovie.append(" (${date[0]})")
            tvGenre.text = txtGenre
            tvOverview.text = data.overview
            tvRating.text = data.voteAverage.toString()
            tvTagline.text = txtTagline
            when (data.voteAverage) {
                in 7.0..10.0 -> {
                    tvRating.setTextColor(Color.parseColor("#21d07a"))
                    cvRatingYellow.visibility = View.INVISIBLE
                    cvRatingRed.visibility = View.INVISIBLE
                }
                in 4.0..7.0 -> {
                    tvRating.setTextColor(Color.parseColor("#FFFB00"))
                    cvRatingYellow.visibility = View.VISIBLE
                }
                else -> {
                    tvRating.setTextColor(Color.parseColor("#db2360"))
                    cvRatingRed.visibility = View.VISIBLE
                }
            }
            viewModelUser.user.observe(viewLifecycleOwner){ user ->
                viewModelUser.getFavorite().observe(viewLifecycleOwner){ favorite ->
                    var cek = false
                    var dataFav: Favorite? = null
                    for (fav in favorite){
                        if (fav.idMovie == data.id && fav.email == user.email){
                            cek = true
                            dataFav = fav
                            break
                        }
                    }
                    if (cek){
                        btnFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite_24)
                    }else{
                        btnFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24)
                    }

                    val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.bounce_anim)

                    btnFavorite.setOnClickListener {
                        if (cek){
                            if (favorite.size == 1){
                                btnFavorite.startAnimation(animation)
                                CoroutineScope(Dispatchers.Main).launch {
                                    viewModelUser.deleteFavorite(dataFav!!)
                                }
                                viewModelMovie.listFavorite.postValue(null)
                                Toast.makeText(requireContext(), "Removed fom favorite", Toast.LENGTH_SHORT).show()
                            }else{
                                btnFavorite.startAnimation(animation)
                                CoroutineScope(Dispatchers.Main).launch {
                                    viewModelUser.deleteFavorite(dataFav!!)
                                }
                                Toast.makeText(requireContext(), "Removed fom favorite", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            btnFavorite.startAnimation(animation)
                            val favor = Favorite(null, user.email, data.id)
                            CoroutineScope(Dispatchers.Main).launch {
                                viewModelUser.addFavorite(favor)
                            }
                            Toast.makeText(requireContext(), "Added to favorite", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun showListSimilar(it: List<ResultMovie>?) {
        binding.rvSimiliar.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val adapter = SimiliarAdapter(object : SimiliarAdapter.OnClickListener {
            override fun onClickItem(data: ResultMovie) {
                viewModelMovie.id.postValue(data.id)
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_detailFragment_self)
            }

        })
        adapter.submitData(it)
        binding.rvSimiliar.adapter = adapter
    }

    private fun showList(cast: List<Cast>) {
        binding.rvCast.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val adapter = CastAdapter(object : CastAdapter.OnClickListener {
            override fun onClickItem(data: Cast) {
                val baseUrlImg = "https://image.tmdb.org/t/p/w500/"
                val imgUrl = baseUrlImg + data.profilePath

                ShowImageDialogFragment(imgUrl).show(requireActivity().supportFragmentManager, null)
            }

        })
        adapter.submitData(cast)
        binding.rvCast.adapter = adapter
    }

}