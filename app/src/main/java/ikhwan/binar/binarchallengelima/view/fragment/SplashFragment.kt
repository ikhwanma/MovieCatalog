package ikhwan.binar.binarchallengelima.view.fragment

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import ikhwan.binar.binarchallengelima.R
import ikhwan.binar.binarchallengelima.databinding.FragmentSplashBinding
import ikhwan.binar.binarchallengelima.viewmodel.UserApiViewModel

class SplashFragment : Fragment() {

    private var _binding : FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private val viewModelUser: UserApiViewModel by hiltNavGraphViewModels(R.id.nav_main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity?)!!.supportActionBar?.hide()

        ObjectAnimator.ofPropertyValuesHolder(
            binding.imgLogo,
            PropertyValuesHolder.ofFloat("scaleX", 1.2f),
            PropertyValuesHolder.ofFloat("scaleY", 1.2f)
        ).setDuration(2000).start()

        Handler(Looper.getMainLooper()).postDelayed({
            viewModelUser.getEmail().observe(viewLifecycleOwner) {
                Log.d("email", "tesEmail $it")
                if (it != ""){
                    Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_homeFragment)
                }else{
                    Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_loginFragment)
                }
            }
        }, 3000)

    }

}