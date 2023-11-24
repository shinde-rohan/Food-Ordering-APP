package com.example.foodorderingapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.foodorderingapp.Model.UserModel
import com.example.foodorderingapp.databinding.ActivityLoginBinding
import com.example.foodorderingapp.databinding.ActivityStartBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private var userName: String? = null
    private lateinit var email : String
    private lateinit var password : String
    private lateinit var auth : FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val googleSignInOption: GoogleSignInOptions = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestIdToken(getString(R.string.default_web_clientId)).requestEmail().build()
       // initialization
        auth = Firebase.auth
        database = Firebase.database.reference
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOption)
        binding.login.setOnClickListener {
            email = binding.loginEmail.text.toString().trim()
            password = binding.loginPass.text.toString().trim()
            if(email.isBlank() || password.isBlank()){
                Toast.makeText(this,"Please fill all the details",Toast.LENGTH_SHORT).show()
            }else{
                createUser()
                Toast.makeText(this,"Login Successfull",Toast.LENGTH_SHORT).show()
            }

        }
        binding.googleBtn.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            launcher.launch(signIntent)
        }
        binding.dontHaveBtn.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
        }
    }

    private fun createUser() {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            task->
            if(task.isSuccessful){
                val user = auth.currentUser
                upadateUI(user)
            }else{
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                    task ->
                    if(task.isSuccessful){
                        val user = auth.currentUser
                        saveUserData()
                        upadateUI(user)
                    }else{
                        Toast.makeText(this,"Login failed",Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

    private fun saveUserData() {
        email = binding.loginEmail.text.toString().trim()
        password = binding.loginPass.text.toString().trim()
        val user = UserModel(userName,email, password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        // save data to firebase database
        database.child("user").child(userId).setValue(user)
    }

    private fun upadateUI(user: FirebaseUser?) {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
    // launcher for google signin
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount = task.result
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Successfully sign in with google",
                                Toast.LENGTH_SHORT
                            ).show()
                            upadateUI(authTask.result?.user)
                        } else {
                            Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}