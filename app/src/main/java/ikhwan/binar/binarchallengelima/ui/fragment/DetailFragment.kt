package ikhwan.binar.binarchallengelima.ui.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ikhwan.binar.binarchallengelima.adapter.CastAdapter
import ikhwan.binar.binarchallengelima.databinding.FragmentDetailBinding
import ikhwan.binar.binarchallengelima.model.credit.Cast
import ikhwan.binar.binarchallengelima.model.popularmovie.ResultMovie
import ikhwan.binar.binarchallengelima.ui.viewmodel.DetailViewModel
import jp.wasabeef.glide.transformations.BlurTransformation

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar?.hide()
        val data = arguments?.getParcelable<ResultMovie>(EXTRA_DATA) as ResultMovie
        viewModel.setId(data.id)

        viewModel.getData()
        viewModel.getCast()

        viewModel.isSuccess.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.apply {
                    rvView.visibility = View.VISIBLE
                    progressCircular.visibility = View.INVISIBLE
                }
            } else {
                binding.apply {
                    rvView.visibility = View.INVISIBLE
                    progressCircular.visibility = View.VISIBLE
                }
            }
        }
        viewModel.data.observe(viewLifecycleOwner) {
            val imgUrl = "https://image.tmdb.org/t/p/w500/${it.posterPath}"
            val date = it.releaseDate.split("-").toTypedArray()
            val genre = it.genres
            val txtTagline = "\"${it.tagline}\""
            var txtGenre = ""
            for ((index, i) in genre.withIndex()) {
                txtGenre += if (index != genre.size - 1) {
                    "${i.name}, "
                } else {
                    i.name
                }
            }
            binding.apply {
                Glide.with(view)
                    .load(imgUrl)
                    .apply(RequestOptions.bitmapTransform(BlurTransformation(10, 3)))
                    .into(imgBackgroundDetail)
                Glide.with(view).load(imgUrl).into(imgMovie)
                tvMovie.text = it.title
                tvMovie.append(" (${date[0]})")
                tvGenre.text = txtGenre
                tvOverview.text = it.overview
                tvRating.text = it.voteAverage.toString()
                tvTagline.text = txtTagline
                when (it.voteAverage) {
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

        viewModel.cast.observe(viewLifecycleOwner) {
            showList(it.cast)
        }
    }

    private fun showList(cast: List<Cast>) {
        binding.rvCast.layoutManager =
            LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        val adapter = CastAdapter()
        adapter.submitData(cast)
        binding.rvCast.adapter = adapter
    }


    companion object {
        const val EXTRA_DATA = "extra_data"
    }

}