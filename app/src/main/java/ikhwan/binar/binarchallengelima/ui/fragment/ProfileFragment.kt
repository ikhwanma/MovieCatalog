package ikhwan.binar.binarchallengelima.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import ikhwan.binar.binarchallengelima.R
import ikhwan.binar.binarchallengelima.databinding.FragmentProfileBinding
import ikhwan.binar.binarchallengelima.ui.viewmodel.UserApiViewModel


class ProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewSharedPreferences: SharedPreferences

    private val viewModel : UserApiViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar?.title = "Edit Profile"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        sharedPreferences =
            requireActivity().getSharedPreferences(LoginFragment.PREF_USER, Context.MODE_PRIVATE)
        viewSharedPreferences = requireActivity().getSharedPreferences(HomeFragment.PREF_VIEW, Context.MODE_PRIVATE)

        viewModel.user.observe(viewLifecycleOwner){
            binding.apply {
                inputNama.setText(it.username)
                inputAlamat.setText(it.address)
                inputDate.setText(it.birthDate)
                inputEmail.setText(it.email)
                inputEmail.isEnabled = false
                inputFullName.setText(it.fullName)
                inputPassword.setText(it.password)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                AlertDialog.Builder(requireContext()).setTitle("Logout")
                    .setMessage("Yakin Ingin Logout?")
                    .setIcon(R.mipmap.ic_launcher_round)
                    .setPositiveButton("Ya"){p0,p1->
                        sharedPreferences.edit().clear().apply()
                        viewSharedPreferences.edit().clear().apply()
                        val navOptions = NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build()
                        Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_loginFragment,null, navOptions)
                    }.setNegativeButton("Tidak"){p0,p1->

                    }
                    .show()

                true
            }
            else -> true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {

        }
    }


}