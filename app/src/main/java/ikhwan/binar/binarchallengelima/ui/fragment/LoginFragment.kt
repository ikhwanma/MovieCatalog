package ikhwan.binar.binarchallengelima.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import ikhwan.binar.binarchallengelima.R
import ikhwan.binar.binarchallengelima.databinding.FragmentLoginBinding
import ikhwan.binar.binarchallengelima.ui.dialogfragment.LoginWaitDialogFragment
import ikhwan.binar.binarchallengelima.ui.viewmodel.UserApiViewModel
import java.util.regex.Pattern

class LoginFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel: UserApiViewModel by activityViewModels()

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
        (activity as AppCompatActivity?)!!.supportActionBar?.show()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences =
            requireActivity().getSharedPreferences(PREF_USER, Context.MODE_PRIVATE)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        if (sharedPreferences.contains(EMAIL)) {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment)
        }

        binding.btnLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
        binding.btnViewPass.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_login -> {
                viewModel.loginStatus.value = false
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
            LoginWaitDialogFragment().show(requireActivity().supportFragmentManager, null)

            viewModel.getAllUsers()
            viewModel.listUsers.observe(viewLifecycleOwner) {
                if (it != null) {
                    for (data in it) {
                        if (email == data.email && password == data.password) {
                            viewModel.user.value = data
                            viewModel.loginCheck.postValue(true)
                            viewModel.loginStatus.postValue(true)
                            break
                        } else {
                            viewModel.loginCheck.postValue(false)
                            viewModel.loginStatus.postValue(true)
                        }
                    }
                }
            }

            viewModel.loginCheck.observe(viewLifecycleOwner) {
                if (it) {
                    val editor = sharedPreferences.edit()
                    editor.putString(EMAIL, email)
                    editor.apply()
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_loginFragment_to_homeFragment)
                } else {

                    Toast.makeText(
                        requireContext(),
                        "Email atau Password salah",
                        Toast.LENGTH_SHORT
                    ).show()
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

    companion object {
        const val PREF_USER = "user_preference"
        const val EMAIL = "email"
    }

}