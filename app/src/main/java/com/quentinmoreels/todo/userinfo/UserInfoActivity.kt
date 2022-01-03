package com.quentinmoreels.todo.userinfo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import com.quentinmoreels.todo.BuildConfig
import com.quentinmoreels.todo.MainActivity
import com.quentinmoreels.todo.R
import com.quentinmoreels.todo.databinding.ActivityUserInfoBinding
import com.quentinmoreels.todo.form.FormActivity
import com.quentinmoreels.todo.network.UserInfo
import com.quentinmoreels.todo.tasklist.TaskListFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserInfoBinding
    private val userInfoViewModel: UserInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.takePictureButton.setOnClickListener {
            askCameraPermissionAndOpenCamera()
        }

        binding.uploadImageButton.setOnClickListener {
            choosePhoto()
        }

        binding.buttonChangeUserInfo.setOnClickListener {
            changeUserInfo()
        }

        lifecycleScope.launch { // on lance une coroutine car `collect` est `suspend`
            userInfoViewModel.userInfo.collect { newUserInfo ->
                userInfoViewModel.editUserInfo(newUserInfo)
                val editTextFirstName = binding.editTextFirstName
                val editTextLastName = binding.editTextLastName
                val editTextEmail = binding.editTextEmail
                val imageView = binding.imageView

                editTextFirstName.setText(userInfoViewModel.userInfo.value.firstName)
                editTextLastName.setText(userInfoViewModel.userInfo.value.lastName)
                editTextEmail.setText(userInfoViewModel.userInfo.value.email)
                imageView.load(userInfoViewModel.userInfo.value.avatar) {
                    error(R.drawable.ic_launcher_background) // display something when image request fails
                }
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) openCamera()
            else showExplanationDialog()
        }

    private fun requestCameraPermission() =
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)

    private fun askCameraPermissionAndOpenCamera() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> openCamera()
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> showExplanationDialog()
            else -> requestCameraPermission()
        }
    }

    private fun showExplanationDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("On a besoin de la caméra sivouplé ! 🥺")
            setPositiveButton("Bon, ok") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            setCancelable(true)
            show()
        }
    }

    // register
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) handleImage(photoUri)
        else Toast.makeText(this, "Erreur ! 😢", Toast.LENGTH_LONG).show()
    }

    // use
    private fun openCamera() = takePicture.launch(photoUri)

    // convert
    private fun convert(uri: Uri) =
        MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "temp.jpeg",
            body = contentResolver.openInputStream(uri)!!.readBytes().toRequestBody()
        )

    private fun handleImage(uri: Uri) {
        val multipartBody = convert(uri)

        userInfoViewModel.editAvatar(multipartBody)
    }

    // create a temp file and get a uri for it
    private val photoUri by lazy {
        FileProvider.getUriForFile(
            this,
            BuildConfig.APPLICATION_ID +".provider",
            File.createTempFile("avatar", ".jpeg", externalCacheDir)

        )
    }

    // register
    private val pickInGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) handleImage(it)
            else Toast.makeText(this, "Erreur ! 😢", Toast.LENGTH_LONG).show()
        }

    // use
    private fun choosePhoto() = pickInGallery.launch("image/*")

    private fun changeUserInfo() {
        val firstName = binding.editTextFirstName.text.toString()
        val lastName = binding.editTextLastName.text.toString()
        val email = binding.editTextEmail.text.toString()
        val avatar = userInfoViewModel.userInfo.value.avatar
        val userinfo = UserInfo(email, firstName, lastName, avatar)
        userInfoViewModel.editUserInfo(userinfo)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}