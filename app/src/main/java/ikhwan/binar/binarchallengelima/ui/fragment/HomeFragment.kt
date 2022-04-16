package ikhwan.binar.binarchallengelima.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import ikhwan.binar.binarchallengelima.R
import ikhwan.binar.binarchallengelima.adapter.MovieAdapter
import ikhwan.binar.binarchallengelima.adapter.MovieLinearAdapter
import ikhwan.binar.binarchallengelima.adapter.NowPlayingAdapter
import ikhwan.binar.binarchallengelima.databinding.FragmentHomeBinding
import ikhwan.binar.binarchallengelima.model.nowplaying.GetNowPlayingResponse
import ikhwan.binar.binarchallengelima.model.nowplaying.ResultNow
import ikhwan.binar.binarchallengelima.model.popularmovie.ResultMovie
import ikhwan.binar.binarchallengelima.ui.viewmodel.ApiViewModel
import ikhwan.binar.binarchallengelima.ui.viewmodel.UserApiViewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ApiViewModel by activityViewModels()
    private val dbViewModel: UserApiViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar?.show()
        (activity as AppCompatActivity?)!!.supportActionBar?.title = "Welcome, Ikhwan!"
        val ai: ApplicationInfo = requireActivity().applicationContext.packageManager
            .getApplicationInfo(
                requireActivity().applicationContext.packageName,
                PackageManager.GET_META_DATA
            )
        val value = ai.metaData["apiKey"]
        viewModel.setApiKey(value.toString())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel.getListMovieData()
        viewModel.getListNowPlaying()

        dbViewModel.user.observe(viewLifecycleOwner) {
            (activity as AppCompatActivity?)!!.supportActionBar?.title = "Welcome, ${it.username}!"
        }

        viewModel.listData.observe(viewLifecycleOwner) {
            val cek = binding.switchRv.isChecked
            if (cek){
                showListLinear(it)
            }else{
                showList(it)
            }
            binding.switchRv.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
                if (isChecked){
                    showListLinear(it)
                }else{
                    showList(it)
                }
            })
        }
        viewModel.listNowData.observe(viewLifecycleOwner){
            showListNowPlay(it.resultNows, it)
        }

        viewModel.isSuccess.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.progressCircular.visibility = View.INVISIBLE
            }
        }

    }

    private fun showListLinear(it: List<ResultMovie>?) {
        binding.rvMovie.layoutManager = LinearLayoutManager(requireContext())
        val adapter = MovieLinearAdapter(object : MovieLinearAdapter.OnClickListener {
            override fun onClickItem(data: ResultMovie) {
                viewModel.setId(data.id)
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_homeFragment_to_detailFragment)
            }
        })
        adapter.submitData(it)
        binding.rvMovie.adapter = adapter
    }

    private fun showListNowPlay(it: List<ResultNow>?, getNowPlayingResponse: GetNowPlayingResponse) {
        binding.rvNow.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val adapter = NowPlayingAdapter(object : NowPlayingAdapter.OnClickListener{
            override fun onClickItem(data: ResultNow) {
                viewModel.setId(data.id)
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_homeFragment_to_detailFragment)
            }
        })
        adapter.submitData(it)
        binding.rvNow.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.profile -> {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_homeFragment_to_profileFragment)
                true
            }
            else -> true
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showList(data: List<ResultMovie>?) {
        binding.rvMovie.layoutManager = GridLayoutManager(requireContext(), 2)
        val adapter = MovieAdapter(object : MovieAdapter.OnClickListener {
            override fun onClickItem(data: ResultMovie) {
                viewModel.setId(data.id)
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_homeFragment_to_detailFragment)
            }
        })
        adapter.submitData(data)
        binding.rvMovie.adapter = adapter
    }


    companion object {
        const val PREF_USER = "user_preference"
        const val EMAIL = "email"
    }

}