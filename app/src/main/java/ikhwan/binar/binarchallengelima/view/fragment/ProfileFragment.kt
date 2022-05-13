package ikhwan.binar.binarchallengelima.view.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import ikhwan.binar.binarchallengelima.R
import ikhwan.binar.binarchallengelima.data.datastore.DataStoreManager
import ikhwan.binar.binarchallengelima.data.helper.ApiHelper
import ikhwan.binar.binarchallengelima.data.network.ApiClient
import ikhwan.binar.binarchallengelima.data.room.FavoriteDatabase
import ikhwan.binar.binarchallengelima.data.utils.Status.*
import ikhwan.binar.binarchallengelima.databinding.FragmentProfileBinding
import ikhwan.binar.binarchallengelima.model.detailmovie.GetDetailMovieResponse
import ikhwan.binar.binarchallengelima.view.adapter.FavoriteAdapter
import ikhwan.binar.binarchallengelima.view.dialogfragment.ShowImageUserDialogFragment
import ikhwan.binar.binarchallengelima.viewmodel.MovieApiViewModel
import ikhwan.binar.binarchallengelima.viewmodel.UserApiViewModel
import ikhwan.binar.binarchallengelima.viewmodel.ViewModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModelUser: UserApiViewModel
    private lateinit var viewModelMovie: MovieApiViewModel
    private lateinit var pref: DataStoreManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity?)!!.supportActionBar?.show()
        (activity as AppCompatActivity?)!!.supportActionBar?.title = "Profile"

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
        setHasOptionsMenu(true)

        viewModelUser.user.observe(viewLifecycleOwner) { user ->
            binding.apply {
                if (user.fullName != "") {
                    tvName.text = user.fullName
                }
                val txtUname = "@${user.username}"
                tvUsername.text = txtUname
            }

            viewModelUser.getFavorite().observe(viewLifecycleOwner) { favorite ->
                val listIdMovie: MutableList<Int> = emptyList<Int>().toMutableList()
                Log.d("listIdMHG", favorite.toString())
                for (fav in favorite) {
                    if (fav.email == user.email) {
                        listIdMovie.add(fav.idMovie!!)

                    }
                }
                getFavorite(listIdMovie)
            }
        }

        viewModelUser.getImage().observe(viewLifecycleOwner) {
            if (it != "") {
                binding.imgUser.setImageBitmap(convertStringToBitmap(it))
                binding.imgUser.setOnClickListener {
                    ShowImageUserDialogFragment().show(
                        requireActivity().supportFragmentManager,
                        null
                    )
                }
            }
        }
    }

    private fun getFavorite(listIdMovie: MutableList<Int>?) {
        val listFavorite: MutableList<GetDetailMovieResponse> =
            emptyList<GetDetailMovieResponse>().toMutableList()
        for (id in listIdMovie!!) {
            viewModelMovie.getDetailMovie(id).observe(viewLifecycleOwner) {
                when (it.status) {
                    SUCCESS -> {
                        listFavorite.add(it.data!!)
                        viewModelMovie.listFavorite.postValue(listFavorite)
                    }
                    ERROR -> Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    LOADING -> Log.d("loading", it.message.toString())
                }
            }
        }

        viewModelMovie.listFavorite.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.imgWarn.visibility = View.INVISIBLE
                showList(it)
            }else{
                binding.imgWarn.visibility = View.VISIBLE
            }
        }
    }

    private fun showList(listFavorite: List<GetDetailMovieResponse>?) {
        Log.d("listEmp", "list ${listFavorite.toString()}")
        binding.rvFavorite.isNestedScrollingEnabled = false
        binding.rvFavorite.layoutManager = GridLayoutManager(requireContext(), 3)
        val adapter = FavoriteAdapter(object : FavoriteAdapter.OnClickListener {
            override fun onClickItem(data: GetDetailMovieResponse) {
                viewModelMovie.id.postValue(data.id)
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_profileFragment2_to_detailFragment)
            }
        })
        adapter.submitData(listFavorite)
        binding.rvFavorite.adapter = adapter
    }

    private fun convertStringToBitmap(string: String?): Bitmap? {
        val byteArray1: ByteArray = Base64.decode(string, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(
            byteArray1, 0,
            byteArray1.size
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                AlertDialog.Builder(requireContext()).setTitle("Logout")
                    .setMessage("Are you sure?")
                    .setIcon(R.mipmap.ic_launcher_round)
                    .setPositiveButton("Yes") { _, _ ->
                        viewModelUser.setImage("")
                        viewModelUser.setEmail("")
                        viewModelMovie.setBoolean(false)
                        viewModelMovie.listFavorite.postValue(null)
                        val navOptions =
                            NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build()
                        Navigation.findNavController(requireView()).navigate(
                            R.id.action_profileFragment2_to_loginFragment,
                            null,
                            navOptions
                        )
                    }.setNegativeButton("No") { _, _ ->

                    }
                    .show()

                true
            }
            R.id.edit_profile -> {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_profileFragment2_to_profileFragment)
                true
            }
            else -> true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}