package com.example.vault

//import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.vault.databinding.ActivityRegisterBinding
//import com.example.vault.model.Usermodel
import com.google.firebase.Firebase
//import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var db=Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        val registerButton = binding.imageView5
        registerButton.setOnClickListener {
            signup()
        }
        binding.textLogin.setOnClickListener {
            val intent= Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
    fun setInProgress(inProgress : Boolean){
        if(inProgress){
            binding.progressCircular.visibility=View.VISIBLE
            binding.imageView5.visibility= View.GONE
        }else{
            binding.progressCircular.visibility=View.GONE
            binding.imageView5.visibility=View.VISIBLE
        }
    }

    fun signup(){
        val name = binding.editTextName.text.toString()
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()
        val confirmPassword = binding.editTextConfirmPassword.text.toString()

        //validations
        if(name.isEmpty()) {
            binding.editTextName.error = "Enter your name"
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.editTextEmail.error = "Email not valid"
        }else if (password.length < 6) {
            binding.editTextPassword.error = "Minimum 6 Characters"
        }else if (password != confirmPassword) {
                binding.editTextConfirmPassword.error = "Password mismatch"
        }else{
            signupWithFirebase(name,email,password)}
    }

    private fun signupWithFirebase(name: String,email: String, password:String){
        setInProgress(true)

        val userMap = hashMapOf(
            "name" to name,
            "email" to email,
            "password" to password
        )
// Storing data directly to firestore where mails can be repeated
        db.collection("user").document().set(userMap)
            .addOnSuccessListener {
                Toast.makeText(applicationContext,"Account created Successfully",Toast.LENGTH_LONG).show()
                        setInProgress(false)
                        startActivity(Intent(this,LoginActivity::class.java))
                        finish()
            }.addOnFailureListener{
            Toast.makeText(applicationContext,it.localizedMessage?:"Something went wrong",Toast.LENGTH_LONG).show()
            setInProgress(false)
        }
//Storing data in firebase where mail cant be repeated
    //        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
    //            email,password
    //           .addOnSuccessListener {
    //            it.user?.let{user->
    //                val userModel= Usermodel (name=(binding.editTextName.text.toString()),email)
    //                Firebase.firestore.collection("users")
    //                    .document(user.uid)
    //                    .set(userModel).addOnSuccessListener {
    //                        Toast.makeText(applicationContext,"Account created Successfully",Toast.LENGTH_LONG).show()
    //                        setInProgress(false)
    //                        startActivity(Intent(this,LoginActivity::class.java))
    //                        finish()
    //                    }
    //            }
    //        }.addOnFailureListener{
    //            Toast.makeText(applicationContext,it.localizedMessage?:"Something went wrong",Toast.LENGTH_LONG).show()
    //            setInProgress(false)
    //        }
    }
}