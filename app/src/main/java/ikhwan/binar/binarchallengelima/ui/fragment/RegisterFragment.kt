package ikhwan.binar.binarchallengelima.ui.fragment

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ikhwan.binar.binarchallengelima.R
import ikhwan.binar.binarchallengelima.database.User
import ikhwan.binar.binarchallengelima.database.UserDatabase
import ikhwan.binar.binarchallengelima.databinding.FragmentRegisterBinding
import java.util.regex.Pattern

class RegisterFragment : Fragment() , View.OnClickListener{
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private var userDatabase : UserDatabase? = null

    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String
    private var cek: Boolean = false
    private var viewPass : Boolean = false
    private var viewKonfPass : Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDatabase = UserDatabase.getInstance(requireContext())
        binding.apply {
            btnRegister.setOnClickListener(this@RegisterFragment)
            btnViewPass.setOnClickListener(this@RegisterFragment)
            btnViewKonfPass.setOnClickListener(this@RegisterFragment)
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_register -> {
                register()
            }
            R.id.btn_view_pass ->{
                if (viewPass == false){
                    binding.apply {
                        btnViewPass.setImageResource(R.drawable.ic_green_eye_24)
                        inputPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    }
                    viewPass = true
                }else{
                    binding.apply {
                        btnViewPass.setImageResource(R.drawable.ic_outline_remove_green_eye_24)
                        inputPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    }
                    viewPass = false
                }
            }
            R.id.btn_view_konf_pass -> {
                if (viewKonfPass == false){
                    binding.apply {
                        btnViewKonfPass.setImageResource(R.drawable.ic_green_eye_24)
                        inputKonfPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    }
                    viewKonfPass = true
                }else{
                    binding.apply {
                        btnViewKonfPass.setImageResource(R.drawable.ic_outline_remove_green_eye_24)
                        inputKonfPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    }
                    viewKonfPass = false
                }
            }
        }
    }

    private fun register(){
        binding.apply {
            name = inputNama.text.toString()
            email = inputEmail.text.toString()
            password = inputPassword.text.toString()
            cek = isValidEmail(email)
        }

        if (inputCheck(name, email, password, cek)){
            registerUser(name, email, password)
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        val user = User(email, name,null,null,null, password)


    }


    private fun inputCheck(name: String, email: String, password: String, cek: Boolean) : Boolean {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || !cek
            || binding.inputKonfPassword.text.toString() != password || password.length < 6
        ) {
            if (name.isEmpty()) {
                binding.apply {
                    inputNama.setError("Username Tidak Boleh Kosong")
                    inputNama.requestFocus()
                }

            }
            if (email.isEmpty()) {
                binding.apply {
                    inputEmail.setError("Email Tidak Boleh Kosong")
                    inputEmail.requestFocus()
                }
            }
            if (password.isEmpty()) {
                binding.apply {
                    inputPassword.setError("Password Tidak Boleh Kosong")
                    inputPassword.requestFocus()
                }
            }
            if (!cek) {
                binding.apply {
                    inputEmail.setError("Email Tidak Sesuai Format")
                    inputEmail.requestFocus()
                }
            }
            if (binding.inputKonfPassword.text.toString() != password) {
                binding.apply {
                    inputKonfPassword.setError("Password Tidak Sama")
                    inputKonfPassword.requestFocus()
                }

            }
            if (password.length < 6) {
                binding.apply {
                    inputPassword.setError("Password minimal 6 karakter")
                    inputPassword.requestFocus()
                }
            }
            return false
        }else{
            return true
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9+._%\\-]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }
}