package ikhwan.binar.binarchallengelima.view.fragment

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import ikhwan.binar.binarchallengelima.R
import ikhwan.binar.binarchallengelima.data.helper.ApiHelper
import ikhwan.binar.binarchallengelima.data.model.users.GetUserResponseItem
import ikhwan.binar.binarchallengelima.data.network.ApiClient
import ikhwan.binar.binarchallengelima.data.utils.Status
import ikhwan.binar.binarchallengelima.data.utils.Status.*
import ikhwan.binar.binarchallengelima.view.adapter.MovieAdapter
import ikhwan.binar.binarchallengelima.view.adapter.MovieLinearAdapter
import ikhwan.binar.binarchallengelima.view.adapter.NowPlayingAdapter
import ikhwan.binar.binarchallengelima.databinding.FragmentHomeBinding
import ikhwan.binar.binarchallengelima.viewmodel.MovieApiViewModel
import ikhwan.binar.binarchallengelima.viewmodel.UserApiViewModel
import ikhwan.binar.binarchallengelima.viewmodel.ViewModelFactory
import java.util.*


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModelMovie: MovieApiViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userSharedPreferences: SharedPreferences
    private lateinit var viewModelUser: UserApiViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar?.show()
        (activity as AppCompatActivity?)!!.supportActionBar?.title = ""
        viewModelUser = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(ApiHelper(ApiClient.userInstance))
        )[UserApiViewModel::class.java]
        viewModelMovie = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(ApiHelper(ApiClient.instance))
        )[MovieApiViewModel::class.java]

        sharedPreferences =
            requireActivity().getSharedPreferences(PREF_VIEW, Context.MODE_PRIVATE)
        userSharedPreferences =
            requireActivity().getSharedPreferences(LoginFragment.PREF_USER, Context.MODE_PRIVATE)
        val email = userSharedPreferences.getString(LoginFragment.EMAIL, "")
        viewModelUser.getAllUsers().observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    for (data in it.data!!) {
                        if (email == data.email) {
                            (activity as AppCompatActivity?)!!.supportActionBar?.title =
                                "Welcome, ${
                                    data.username.replaceFirstChar { userData ->
                                        if (userData.isLowerCase()) userData.titlecase(
                                            Locale.getDefault()
                                        ) else userData.toString()
                                    }
                                }!"
                            viewModelUser.user.postValue(data)
                            break
                        }
                    }
                }
                ERROR -> Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                LOADING -> Log.d("loadingMsg", "Loading")
            }
        }

        val ai: ApplicationInfo = requireActivity().applicationContext.packageManager
            .getApplicationInfo(
                requireActivity().applicationContext.packageName,
                PackageManager.GET_META_DATA
            )
        val values = ai.metaData["apiKey"]
        viewModelMovie.apiKey.value = values.toString()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val cek = sharedPreferences.getBoolean(CEK, false)
        binding.switchRv.isChecked = cek

        viewModelMovie.getPopularMovie().observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    if (cek) {
                        showListLinear(it.data!!.resultMovies)
                    } else {
                        showList(it.data!!.resultMovies)
                    }
                    binding.switchRv.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            sharedPreferences.edit().putBoolean(CEK, true).apply()
                            showListLinear(it.data.resultMovies)
                        } else {
                            sharedPreferences.edit().putBoolean(CEK, false).apply()
                            showList(it.data.resultMovies)
                        }
                    }
                    binding.progressCircular.visibility = View.GONE
                }
                ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                LOADING -> {
                    Log.d("loadingMsg", "Loading")
                }
            }
        }
        viewModelMovie.getNowPlaying().observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    showListNowPlay(it.data!!.resultNows)
                }
                ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                LOADING -> {
                    Log.d("loadingMsg", "Loading")
                }
            }
        }
    }

    private fun showListLinear(it: List<ikhwan.binar.binarchallengelima.data.model.popularmovie.ResultMovie>?) {
        binding.rvMovie.layoutManager = LinearLayoutManager(requireContext())
        val adapter = MovieLinearAdapter(object : MovieLinearAdapter.OnClickListener {
            override fun onClickItem(data: ikhwan.binar.binarchallengelima.data.model.popularmovie.ResultMovie) {
                viewModelMovie.id.postValue(data.id)
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_homeFragment_to_detailFragment)
            }
        })
        adapter.submitData(it)
        binding.rvMovie.adapter = adapter
    }

    private fun showListNowPlay(it: List<ikhwan.binar.binarchallengelima.data.model.nowplaying.ResultNow>?) {
        binding.rvNow.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val adapter = NowPlayingAdapter(object : NowPlayingAdapter.OnClickListener {
            override fun onClickItem(data: ikhwan.binar.binarchallengelima.data.model.nowplaying.ResultNow) {
                viewModelMovie.id.postValue(data.id)
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_homeFragment_to_detailFragment)
            }
        })
        adapter.submitData(it)
        binding.rvNow.adapter = adapter
    }

    private fun showList(data: List<ikhwan.binar.binarchallengelima.data.model.popularmovie.ResultMovie>?) {
        binding.rvMovie.isNestedScrollingEnabled = false
        binding.rvMovie.layoutManager = GridLayoutManager(requireContext(), 2)
        val adapter = MovieAdapter(object : MovieAdapter.OnClickListener {
            override fun onClickItem(data: ikhwan.binar.binarchallengelima.data.model.popularmovie.ResultMovie) {
                viewModelMovie.id.postValue(data.id)
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_homeFragment_to_detailFragment)
            }
        })
        adapter.submitData(data)
        binding.rvMovie.adapter = adapter
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

    companion object {
        const val PREF_VIEW = "view_preference"
        const val CEK = "cek"
    }

}