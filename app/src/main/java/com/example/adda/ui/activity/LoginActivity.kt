package com.example.adda.ui.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.adda.R
import com.example.adda.databinding.ActivityLoginBinding
import com.example.adda.models.GoogleInfo
import com.example.adda.models.UserInfo
import com.example.adda.utils.Config
import com.example.adda.utils.SessionManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private var binding: ActivityLoginBinding? = null

    private lateinit var auth: FirebaseAuth
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var sessionManager: SessionManager? = null
    private var progressDialog: ProgressDialog? = null
    private lateinit var mDatabase: DatabaseReference
    private var isGoneMain = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        init()

        binding!!.googleLogin.setOnClickListener {
            googleSignIn()
        }
    }

    private fun init() {
        sessionManager = SessionManager(this)
        mDatabase = FirebaseDatabase.getInstance().reference
        progressDialog = ProgressDialog(this@LoginActivity, R.style.ProgressDialogColour)
        progressDialog!!.setMessage("Please wait...")
        progressDialog!!.setCancelable(false)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun googleSignIn() {
        val signInIntent: Intent = mGoogleSignInClient!!.signInIntent

        launcher.launch(signInIntent)
    } // googleSignIn

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result->

        if (result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        }else{
            Config.showToast( this,"result not ok")
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            if(task.isSuccessful){

                val account: GoogleSignInAccount? = task.result
                if (account != null){
                    updateInfo(account)
                }else{
                    Config.showToast(this, "account null")
                }
            }else{
                Config.showToast(this, "task not success")
            }
        } catch (e: ApiException) {
            Log.e("tag", "signInResult:failed code=" + e.statusCode)
            Toast.makeText(this, "catch: "+getString(R.string.error), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateInfo(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val googleInfo = GoogleInfo(auth.uid, account.displayName, account.email, account.photoUrl.toString())

                verifyExistingUser(googleInfo)

            }else{
                Config.showToast(this, "it not okay")
            }
        }
    }

    private fun verifyExistingUser(googleInfo: GoogleInfo) {
        binding!!.progressCircular.visibility = View.VISIBLE
        val postListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (snapshot in dataSnapshot.children) {
                    val userInfo: UserInfo? = snapshot.getValue(UserInfo::class.java)

                    if (googleInfo.email.equals(userInfo!!.email)){

                        sessionManager!!.isLogedIn = true
                        sessionManager!!.userIdToken = userInfo.uid

                        break
                    }
                }
                binding!!.progressCircular.visibility = View.GONE

                if (sessionManager!!.isLogedIn && !isGoneMain){
                    isGoneMain = true
                    val i = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(i)
                    finish()
                }

                if (!sessionManager!!.isLogedIn){
                    val i = Intent(this@LoginActivity, UserRegistrationActivity::class.java)
                    i.putExtra("googleInfo", googleInfo)
                    startActivity(i)
                    finish()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Toast.makeText(this@LoginActivity, R.string.error, Toast.LENGTH_SHORT).show()
                binding!!.progressCircular.visibility = View.GONE
            }
        }
        mDatabase.child(Config.USER_KEY).addValueEventListener(postListener)
    }
}