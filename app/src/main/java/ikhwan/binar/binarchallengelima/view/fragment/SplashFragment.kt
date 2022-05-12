package ikhwan.binar.binarchallengelima.view.fragment

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import ikhwan.binar.binarchallengelima.R
import ikhwan.binar.binarchallengelima.data.datastore.DataStoreManager
import ikhwan.binar.binarchallengelima.data.helper.ApiHelper
import ikhwan.binar.binarchallengelima.data.network.ApiClient
import ikhwan.binar.binarchallengelima.data.room.FavoriteDatabase
import ikhwan.binar.binarchallengelima.databinding.FragmentSplashBinding
import ikhwan.binar.binarchallengelima.viewmodel.UserApiViewModel
import ikhwan.binar.binarchallengelima.viewmodel.ViewModelFactory

class SplashFragment : Fragment() {

    private var _binding : FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelUser: UserApiViewModel
    private lateinit var pref: DataStoreManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)

        pref = DataStoreManager(requireContext())

        viewModelUser = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(ApiHelper(ApiClient.userInstance), pref, FavoriteDatabase.getInstance(requireContext())!!
                .favoriteDao())
        )[UserApiViewModel::class.java]

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
                if (it != ""){
                    Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_homeFragment)
                }else{
                    Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_loginFragment)
                }
            }
        }, 3000)

    }

}