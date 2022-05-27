package ikhwan.binar.binarchallengelima.view.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ikhwan.binar.binarchallengelima.R
import ikhwan.binar.binarchallengelima.data.room.Favorite
import ikhwan.binar.binarchallengelima.databinding.FragmentProfileBinding
import ikhwan.binar.binarchallengelima.view.adapter.FavoriteAdapter
import ikhwan.binar.binarchallengelima.view.dialogfragment.ShowImageUserDialogFragment
import ikhwan.binar.binarchallengelima.viewmodel.MovieApiViewModel
import ikhwan.binar.binarchallengelima.viewmodel.UserApiViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModelMovie: MovieApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val viewModelUser: UserApiViewModel by hiltNavGraphViewModels(R.id.nav_main)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity?)!!.supportActionBar?.show()
        (activity as AppCompatActivity?)!!.supportActionBar?.title = "Profile"

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

            viewModelUser.getFavorite(user.email).observe(viewLifecycleOwner){fav->
                Log.d("favoriteEmail", "tesFav $fav")
                if (fav.isNullOrEmpty()){
                    binding.imgWarn.visibility = View.VISIBLE
                }else{
                    binding.imgWarn.visibility = View.INVISIBLE
                    showList(fav)
                }
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

    private fun showList(listFavorite: List<Favorite>?) {
        Log.d("listEmp", "list ${listFavorite.toString()}")
        binding.rvFavorite.isNestedScrollingEnabled = false
        binding.rvFavorite.layoutManager = GridLayoutManager(requireContext(), 3)
        val adapter = FavoriteAdapter(object : FavoriteAdapter.OnClickListener {
            override fun onClickItem(data: Favorite) {
                viewModelMovie.id.postValue(data.idMovie)
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