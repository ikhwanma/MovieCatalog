package ikhwan.binar.binarchallengelima.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import ikhwan.binar.binarchallengelima.R
import ikhwan.binar.binarchallengelima.databinding.FragmentProfileBinding
import ikhwan.binar.binarchallengelima.model.users.PostUserResponse
import ikhwan.binar.binarchallengelima.ui.viewmodel.UserApiViewModel


class ProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewSharedPreferences: SharedPreferences

    private lateinit var id : String

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
            this.id = it.id
        }
        binding.btnUpdate.setOnClickListener(this)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                AlertDialog.Builder(requireContext()).setTitle("Logout")
                    .setMessage("Yakin Ingin Logout?")
                    .setIcon(R.mipmap.ic_launcher_round)
                    .setPositiveButton("Ya"){ _, _ ->
                        sharedPreferences.edit().clear().apply()
                        viewSharedPreferences.edit().clear().apply()
                        val navOptions = NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build()
                        Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_loginFragment,null, navOptions)
                    }.setNegativeButton("Tidak"){ _, _ ->

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
            R.id.btn_update -> {
                cekInput()
            }
        }
    }

    private fun cekInput() {
        val address: String
        val birthDate: String
        val email: String
        val fullName: String
        val password: String
        val username: String
        binding.apply {
            address = inputAlamat.text.toString()
            birthDate = inputDate.text.toString()
            email = inputEmail.text.toString()
            fullName = inputFullName.text.toString()
            password = inputPassword.text.toString()
            username = inputNama.text.toString()

            if (username.isEmpty()){
                inputDate.error = "Username tidak boleh kosong"
                return
            }
            if (fullName.isEmpty()){
                inputFullName.error = "Nama lengkap tidak boleh kosong"
                return
            }
            if (address.isEmpty()){
                inputAlamat.error = "Alamat tidak boleh kosong"
                return
            }
            if (password.isEmpty()){
                inputPassword.error = "Password tidak boleh kosong"
                return
            }
            if (password.length < 6){
                inputPassword.error = "Password minimal 6 karakter"
                return
            }
            if (birthDate.isEmpty()){
                inputDate.error = "Tanggal lahir tidak boleh kosong"
                return
            }
        }
        val user = PostUserResponse(address, birthDate, email, fullName, password, username)
        updateUser(user)
    }

    private fun updateUser(user: PostUserResponse) {
        viewModel.updateUser(user, id)
        viewModel.updateStatus.observe(viewLifecycleOwner){
            if (it!!){
                Toast.makeText(requireContext(), "Data diupdate", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_homeFragment)
            }else{
                Toast.makeText(requireContext(), "Data gagal diupdate", Toast.LENGTH_SHORT).show()
            }
        }
    }


}