package com.utkukirca.socialmediaclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.utkukirca.socialmediaclone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val alreadySignedUser = auth.currentUser

        if (alreadySignedUser != null){
            startActivity(Intent(this,FeedActivity::class.java))
            finish()
        }

    }


    fun signInClicked(view: View) {

        val email = binding.emailEt.text.toString()
        val password = binding.passwordEt.text.toString()

        if (email.equals("") || password.equals("") ){

            Toast.makeText(this,"Email & password cannot be empty!", Toast.LENGTH_LONG).show()

        }else {

            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                val intent = Intent(this,FeedActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }

        }

    }

    fun signUpClicked(view: View) {

        val email = binding.emailEt.text.toString()
        val password = binding.passwordEt.text.toString()

        if (email.equals("") || password.equals("") ){

            Toast.makeText(this,"Email & password cannot be empty!", Toast.LENGTH_LONG).show()

        }else {

            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                val intent = Intent(this,FeedActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }

        }

    }
}