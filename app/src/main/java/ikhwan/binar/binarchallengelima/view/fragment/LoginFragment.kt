package ikhwan.binar.binarchallengelima.view.fragment

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ikhwan.binar.binarchallengelima.R
import ikhwan.binar.binarchallengelima.data.utils.Status.*
import ikhwan.binar.binarchallengelima.databinding.FragmentLoginBinding
import ikhwan.binar.binarchallengelima.view.dialogfragment.LoginWaitDialogFragment
import ikhwan.binar.binarchallengelima.viewmodel.UserApiViewModel
import java.util.regex.Pattern

class LoginFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModelUser: UserApiViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private lateinit var email: String
    private lateinit var password: String
    private var viewPass: Boolean = false
    private var cek: Boolean = false
    private var cekEmail: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity?)!!.supportActionBar?.title = "Login"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        viewModelUser.getEmail().observe(viewLifecycleOwner) {
            cekEmail = it
            Log.d("cekEmail", cekEmail)
        }

        binding.btnLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
        binding.btnViewPass.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_login -> {
                login()
            }
            R.id.btn_register -> {
                p0.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
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

    private fun login() {
        binding.apply {
            email = inputEmail.text.toString()
            password = inputPassword.text.toString()
            cek = isValidEmail(email)
        }

        if (inputCheck(email, password, cek)) {

            viewModelUser.getUser(email).observe(viewLifecycleOwner){
                when (it.status) {
                    SUCCESS -> {
                        val data = it.data!!
                        viewModelUser.loginStatus.postValue(true)
                        if (data.isNotEmpty()){
                            if (password == data[0].password){
                                viewModelUser.setEmail(email)
                                Navigation.findNavController(requireView())
                                    .navigate(R.id.action_loginFragment_to_homeFragment)
                            }else{
                                Toast.makeText(requireContext(), "Wrong password", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(requireContext(), "Email not registered", Toast.LENGTH_SHORT).show()
                        }

                    }
                    ERROR -> {
                        viewModelUser.loginStatus.postValue(true)
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    LOADING -> {
                        LoginWaitDialogFragment().show(
                            requireActivity().supportFragmentManager,
                            null
                        )
                    }
                }
            }
        }
    }


    private fun inputCheck(email: String, password: String, cek: Boolean): Boolean {
        if (email.isEmpty() || password.isEmpty() || !cek
        ) {
            if (email.isEmpty()) {
                binding.apply {
                    inputEmail.error = "Email can't be empty"
                    inputEmail.requestFocus()
                }

            }
            if (password.isEmpty()) {
                binding.apply {
                    inputPassword.error = "Password can't be empty"
                    inputPassword.requestFocus()
                }
            }
            if (!cek) {
                binding.apply {
                    inputEmail.error = "Email can't be empty"
                    inputEmail.requestFocus()
                }
            }
            return false
        } else {
            return true
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = Pattern.compile(
            "[a-zA-Z0-9+._%\\-]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        return emailPattern.matcher(email).matches()
    }


}