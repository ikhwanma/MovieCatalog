package ikhwan.binar.binarchallengelima.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

import ikhwan.binar.binarchallengelima.adapter.MovieAdapter
import ikhwan.binar.binarchallengelima.databinding.FragmentHomeBinding
import ikhwan.binar.binarchallengelima.model.GetPopularMovieResponse
import ikhwan.binar.binarchallengelima.model.Result
import ikhwan.binar.binarchallengelima.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchData()
    }

    private fun fetchData() {
        ApiClient.instance.getPopularMovie()
            .enqueue(object : Callback<GetPopularMovieResponse> {
                override fun onResponse(
                    call: Call<GetPopularMovieResponse>,
                    response: Response<GetPopularMovieResponse>
                ) {
                    val body = response.body()
                    val code = response.code()
                    if (code == 200){
                        showList(body?.results)
                    }
                }

                override fun onFailure(call: Call<GetPopularMovieResponse>, t: Throwable) {
                    Log.d("onFailure", "gagal")
                }


            })
    }

    private fun showList(data: List<Result>?) {
        binding.rvMovie.layoutManager = GridLayoutManager(requireContext(), 2)
        val adapter = MovieAdapter()
        adapter.submitData(data)
        binding.rvMovie.adapter = adapter
    }

}