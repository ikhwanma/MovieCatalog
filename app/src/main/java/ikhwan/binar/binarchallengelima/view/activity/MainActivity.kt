package ikhwan.binar.binarchallengelima.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import ikhwan.binar.binarchallengelima.R
import ikhwan.binar.binarchallengelima.viewmodel.MovieApiViewModel
import ikhwan.binar.binarchallengelima.viewmodel.UserApiViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}