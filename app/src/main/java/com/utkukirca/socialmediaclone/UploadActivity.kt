package com.utkukirca.socialmediaclone

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PathPermission
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.utkukirca.socialmediaclone.databinding.ActivityUploadBinding

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher : ActivityResultLauncher<String>
    private lateinit var auth : FirebaseAuth
    private lateinit var storage : FirebaseStorage
    private lateinit var firestore : FirebaseFirestore


    var pictureFromMedia : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage



        registerLauncher()
    }


    fun upload (view: View) {

        val referenceFromStorage = storage.reference
        val pictureReference =  referenceFromStorage.child("images/image.jpg")

        if(pictureFromMedia != null){
            pictureReference.putFile(pictureFromMedia!!).addOnSuccessListener {

            }.addOnFailureListener{
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }

    }

    fun selectImage (view: View) {
        //permission request and check block
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Permission needed for using gallery!",Snackbar.LENGTH_INDEFINITE).setAction("Give permission"){
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }.show()

            }
            else
            {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        else
        {
            val intentToImages = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToImages)

        }

    }

    private fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
         if (it.resultCode == RESULT_OK){
             val intentFromResult = it.data
             if(intentFromResult != null){
                 pictureFromMedia = intentFromResult.data
                 pictureFromMedia?.let { uri ->
                     binding.imageView.setImageURI(uri)
                 }
             }
         }
        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if (it){
                val intentToImages = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToImages)
            } else {
              Toast.makeText(this@UploadActivity,"Permission denied!!",Toast.LENGTH_LONG).show()
            }
        }
    }

}