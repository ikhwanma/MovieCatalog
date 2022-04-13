package ikhwan.binar.binarchallengelima.ui.fragment

import android.content.Context
import android.content.SharedPreferences
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
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import ikhwan.binar.binarchallengelima.R
import ikhwan.binar.binarchallengelima.database.UserDatabase
import ikhwan.binar.binarchallengelima.databinding.FragmentLoginBinding
import ikhwan.binar.binarchallengelima.ui.viewmodel.DatabaseViewModel
import java.util.regex.Pattern

class LoginFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var userDatabase: UserDatabase? = null
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel: DatabaseViewModel by activityViewModels()

    private lateinit var email: String
    private lateinit var password: String
    private var viewPass: Boolean = false
    private var cek: Boolean = false

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
        sharedPreferences =
            requireActivity().getSharedPreferences(HomeFragment.PREF_USER, Context.MODE_PRIVATE)
        userDatabase = UserDatabase.getInstance(requireContext())
        viewModel.setUserDb(userDatabase!!)

        if (sharedPreferences.contains(HomeFragment.EMAIL)) {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment)
        }

        binding.btnLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
        binding.btnViewPass.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_login -> {
                login(p0)
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

    private fun login(p0: View) {
        binding.apply {
            email = inputEmail.text.toString()
            password = inputPassword.text.toString()
            cek = isValidEmail(email)
        }

        if (inputCheck(email, password, cek)) {
            viewModel.loginUser(email, password)
            viewModel.loginStatus.observe(this) {
                if (it) {
                    val editor = sharedPreferences.edit()
                    editor.putString(HomeFragment.EMAIL, email)
                    editor.apply()
                    try {
                        p0.findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    } catch (e: IllegalArgumentException) {
                        Log.d("failed", e.toString())
                    }

                } else {
                    viewModel.toastLoginMessage.observe(this) { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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
                    inputEmail.error = "Email Tidak Boleh Kosong"
                    inputEmail.requestFocus()
                }

            }
            if (password.isEmpty()) {
                binding.apply {
                    inputPassword.error = "Password Tidak Boleh Kosong"
                    inputPassword.requestFocus()
                }
            }
            if (!cek) {
                binding.apply {
                    inputEmail.error = "Email Tidak Sesuai Format"
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