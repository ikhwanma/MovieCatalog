package ikhwan.binar.binarchallengelima.view.fragment

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Base64
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import ikhwan.binar.binarchallengelima.R
import ikhwan.binar.binarchallengelima.data.datastore.DataStoreManager
import ikhwan.binar.binarchallengelima.data.helper.ApiHelper
import ikhwan.binar.binarchallengelima.data.network.ApiClient
import ikhwan.binar.binarchallengelima.data.utils.Status.*
import ikhwan.binar.binarchallengelima.databinding.FragmentEditProfileBinding
import ikhwan.binar.binarchallengelima.model.users.PostUserResponse
import ikhwan.binar.binarchallengelima.viewmodel.UserApiViewModel
import ikhwan.binar.binarchallengelima.viewmodel.ViewModelFactory
import java.io.ByteArrayOutputStream
import java.util.*


class EditProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var pref: DataStoreManager

    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)

    private lateinit var id: String
    private var viewPass: Boolean = false

    private var imgBitmap = ""
    private var imgUri = ""

    private lateinit var viewModelUser: UserApiViewModel


    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                handleCameraImage(result.data)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity?)!!.supportActionBar?.title = "Edit Profile"

        pref = DataStoreManager(requireContext())

        viewModelUser = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(ApiHelper(ApiClient.userInstance), pref)
        )[UserApiViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        viewModelUser.getImage().observe(viewLifecycleOwner){
            if (it!=""){
                binding.imgUser.setImageBitmap(convertStringToBitmap(it))
            }
        }

        viewModelUser.getImageGallery().observe(viewLifecycleOwner){
            if (it != ""){
                binding.imgUser.setImageURI(Uri.parse(it))
            }
        }

        binding.btnViewPass.setOnClickListener(this)
        binding.btnUpdate.setOnClickListener(this)
        binding.inputDate.setOnClickListener {
            openDatePicker()
        }
        binding.imgUser.setOnClickListener {
            checkingPermissions()
        }
    }

    private fun bitMapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun convertStringToBitmap(string: String?): Bitmap? {
        val byteArray1: ByteArray = Base64.decode(string, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(
            byteArray1, 0,
            byteArray1.size
        )
    }

    private fun checkingPermissions() {
        if (isGranted(
                requireActivity(),
                Manifest.permission.CAMERA,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_CODE_PERMISSION,
            )
        ) {
            chooseImageDialog()
        }
    }

    private fun isGranted(
        activity: Activity,
        permission: String,
        permissions: Array<String>,
        request: Int,
    ): Boolean {
        val permissionCheck = ActivityCompat.checkSelfPermission(activity, permission)
        return if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(activity, permissions, request)
            }
            false
        } else {
            true
        }
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, Please allow permissions from App Settings.")
            .setPositiveButton(
                "App Settings"
            ) { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", "packageName", null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .show()
    }

    private fun chooseImageDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("Pilih Gambar")
            .setPositiveButton("Gallery") { _, _ -> openGallery() }
            .setNegativeButton("Camera") { _, _ -> openCamera() }
            .show()
    }

    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            val str = result.toString()
            imgUri = str
            binding.imgUser.setImageURI(Uri.parse(str))
        }

    private fun openGallery() {
        galleryResult.launch("image/*")
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraResult.launch(cameraIntent)
    }

    private fun handleCameraImage(intent: Intent?) {
        val bitmap = intent?.extras?.get("data") as Bitmap
        binding.imgUser.setImageBitmap(bitmap)
        val str = bitMapToString(bitmap)
        imgBitmap = str!!
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
        if (imgBitmap != ""){
            Log.d("imgHandlerBtm", imgBitmap)
            viewModelUser.setImage(imgBitmap)
            viewModelUser.setImageGallery("")
        }

        if (imgUri != ""){
            Log.d("imgHandlerUri", imgUri)
            viewModelUser.setImageGallery(imgUri)
            viewModelUser.setImage("")
        }
        viewModelUser.setImage(imgBitmap)
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

    companion object {
        const val REQUEST_CODE_PERMISSION = 100
    }


}