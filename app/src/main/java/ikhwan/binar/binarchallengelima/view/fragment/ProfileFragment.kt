package ikhwan.binar.binarchallengelima.view.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import ikhwan.binar.binarchallengelima.R
import ikhwan.binar.binarchallengelima.data.helper.ApiHelper
import ikhwan.binar.binarchallengelima.databinding.FragmentProfileBinding
import ikhwan.binar.binarchallengelima.data.model.users.PostUserResponse
import ikhwan.binar.binarchallengelima.data.network.ApiClient
import ikhwan.binar.binarchallengelima.data.utils.Status
import ikhwan.binar.binarchallengelima.data.utils.Status.*
import ikhwan.binar.binarchallengelima.viewmodel.UserApiViewModel
import ikhwan.binar.binarchallengelima.viewmodel.ViewModelFactory
import java.util.*


class ProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewSharedPreferences: SharedPreferences

    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)

    private lateinit var id: String
    private var viewPass: Boolean = false

    private lateinit var viewModelUser: UserApiViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity?)!!.supportActionBar?.title = "Edit Profile"
        viewModelUser = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(ApiHelper(ApiClient.userInstance))
        )[UserApiViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        sharedPreferences =
            requireActivity().getSharedPreferences(LoginFragment.PREF_USER, Context.MODE_PRIVATE)
        viewSharedPreferences =
            requireActivity().getSharedPreferences(HomeFragment.PREF_VIEW, Context.MODE_PRIVATE)

        viewModelUser.user.observe(viewLifecycleOwner) {
            binding.apply {
                inputNama.setText(it.username)
                inputAlamat.setText(it.address)
                inputDate.setText(it.birthDate)
                inputEmail.setText(it.email)
                inputFullName.setText(it.fullName)
                inputPassword.setText(it.password)
            }
            this.id = it.id
        }

        binding.btnViewPass.setOnClickListener(this)
        binding.btnUpdate.setOnClickListener(this)
        binding.inputDate.setOnClickListener {
            openDatePicker()
        }
    }

    private fun openDatePicker() {
        DatePickerDialog(requireContext(), { _, i, i2, i3 ->
            val now = c.get(Calendar.YEAR)
            val age = now - i

            val i3String = i3.toString()
            val dd = if (i3String.length == 1) {
                "0$i3String"
            } else {
                i3String
            }
            var mm = ""
            when (i2) {
                0 -> {
                    mm = "January"
                }
                1 -> {
                    mm = "February"
                }
                2 -> {
                    mm = "March"
                }
                3 -> {
                    mm = "April"
                }
                4 -> {
                    mm = "May"
                }
                5 -> {
                    mm = "June"
                }
                6 -> {
                    mm = "July"
                }
                7 -> {
                    mm = "August"
                }
                8 -> {
                    mm = "September"
                }
                9 -> {
                    mm = "October"
                }
                10 -> {
                    mm = "November"
                }
                11 -> {
                    mm = "December"
                }
            }

            val txtDate = "$dd $mm $i ($age y.o)"
            binding.inputDate.setText(txtDate)
        }, year, month, day).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                AlertDialog.Builder(requireContext()).setTitle("Logout")
                    .setMessage("Are you sure?")
                    .setIcon(R.mipmap.ic_launcher_round)
                    .setPositiveButton("Yes") { _, _ ->
                        sharedPreferences.edit().clear().apply()
                        viewSharedPreferences.edit().clear().apply()
                        val navOptions =
                            NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build()
                        Navigation.findNavController(requireView()).navigate(
                            R.id.action_profileFragment_to_loginFragment,
                            null,
                            navOptions
                        )
                    }.setNegativeButton("No") { _, _ ->

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
            R.id.btn_view_pass -> {
                if (!viewPass) {
                    binding.apply {
                        btnViewPass.setImageResource(R.drawable.ic_green_eye_24)
                        inputPassword.transformationMethod =
                            HideReturnsTransformationMethod.getInstance()
                    }
                    viewPass = true
                } else {
                    binding.apply {
                        btnViewPass.setImageResource(R.drawable.ic_outline_remove_green_eye_24)
                        inputPassword.transformationMethod =
                            PasswordTransformationMethod.getInstance()
                    }
                    viewPass = false
                }
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

            if (username.isEmpty()) {
                inputDate.error = "Username can't be empty"
                return
            }
            if (fullName.isEmpty()) {
                inputFullName.error = "Nama lengkap can't be empty"
                return
            }
            if (address.isEmpty()) {
                inputAlamat.error = "Alamat can't be empty"
                return
            }
            if (password.isEmpty()) {
                inputPassword.error = "Password can't be empty"
                return
            }
            if (password.length < 6) {
                inputPassword.error = "Minimum 6 characters"
                return
            }
            if (birthDate.isEmpty()) {
                inputDate.error = "Tanggal lahir can't be empty"
                return
            }
        }
        val user = PostUserResponse(
            address,
            birthDate,
            email,
            fullName,
            password,
            username
        )
        updateUser(user)
    }

    private fun updateUser(user: PostUserResponse) {
        viewModelUser.updateUser(user, id).observe(viewLifecycleOwner){
            when(it.status){
                SUCCESS -> {
                    Toast.makeText(requireContext(), "Data Updated", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_profileFragment_to_homeFragment)
                }
                ERROR -> {
                    Log.d("loadingMsg", it.message.toString())
                }
                LOADING -> {
                    Log.d("loadingMsg", "Loading")
                }
            }
        }
    }


}