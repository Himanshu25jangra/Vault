package com.example.vault

//import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.vault.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var db= Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        val loginButton = binding.buttonLogin
        loginButton.setOnClickListener {
            if(binding.editTextEmail.text.isEmpty()){
                binding.editTextEmail.error="Enter email"
            }else if (binding.editTextPassword.text.isEmpty()){
                binding.editTextPassword.error="Enter Password"
            }else
                login()
        }
        binding.textRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

//        binding.buttonGoogle.setOnClickListener {
//            val intent= Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//        }
    }

    fun login(){
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()
        db.collection("user")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    binding.editTextEmail.error = "User not found"
                    return@addOnSuccessListener
                }
                // Check if the password matches the stored password for the user
                val userDoc = documents.documents[0]
                val storedPassword = userDoc.getString("password")
                if (storedPassword == password) {
                    Toast.makeText(applicationContext,"User successfully logged In",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
            }.addOnFailureListener {
                binding.editTextPassword.error= "Incorrect Email or password"
                binding.editTextEmail.error= "Incorrect Email or password"
            }
    }
}
