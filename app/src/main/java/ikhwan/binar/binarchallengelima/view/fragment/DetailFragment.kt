package ikhwan.binar.binarchallengelima.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import ikhwan.binar.binarchallengelima.data.utils.Status
import ikhwan.binar.binarchallengelima.view.adapter.CastAdapter
import ikhwan.binar.binarchallengelima.view.adapter.SimiliarAdapter
import ikhwan.binar.binarchallengelima.databinding.FragmentDetailBinding
import ikhwan.binar.binarchallengelima.model.credit.Cast
import ikhwan.binar.binarchallengelima.model.popularmovie.ResultMovie
import ikhwan.binar.binarchallengelima.view.dialogfragment.ShowImageDialogFragment
import ikhwan.binar.binarchallengelima.viewmodel.MovieApiViewModel
import ikhwan.binar.binarchallengelima.viewmodel.ViewModelFactory
import jp.wasabeef.glide.transformations.BlurTransformation

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelMovie: MovieApiViewModel
    private lateinit var pref: DataStoreManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity?)!!.supportActionBar?.hide()


        pref = DataStoreManager(requireContext())

        viewModelMovie = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(ApiHelper(ApiClient.instance), pref)
        )[MovieApiViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelMovie.id.observe(viewLifecycleOwner) { id ->
            viewModelMovie.getDetailMovie(id).observe(viewLifecycleOwner){
                when(it.status){
                    Status.ERROR -> {
                        Log.d("errMessage", it.message!!)
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        binding.progressCircular.visibility = View.GONE
                        setDetail(it.data!!)
                    }
                    Status.LOADING -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            viewModelMovie.getCreditMovie(id).observe(viewLifecycleOwner){
                when(it.status){
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        showList(it.data!!.cast)
                    }
                    Status.LOADING -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            viewModelMovie.getSimilarMovie(id).observe(viewLifecycleOwner){
                when(it.status){
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        showListSimilar(it.data!!.resultMovies)
                    }
                    Status.LOADING -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
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
        }
    }

    private fun showListSimilar(it: List<ResultMovie>?) {
        binding.rvSimiliar.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        val adapter = SimiliarAdapter(object : SimiliarAdapter.OnClickListener{
            override fun onClickItem(data: ResultMovie) {
                viewModelMovie.id.postValue(data.id)
                Navigation.findNavController(requireView()).navigate(R.id.action_detailFragment_self)
            }

        })
        adapter.submitData(it)
        binding.rvSimiliar.adapter = adapter
    }

    private fun showList(cast: List<Cast>) {
        binding.rvCast.layoutManager =
            LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        val adapter = CastAdapter(object : CastAdapter.OnClickListener{
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