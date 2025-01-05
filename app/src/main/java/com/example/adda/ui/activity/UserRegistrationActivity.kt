package com.example.adda.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.adda.R
import com.example.adda.databinding.ActivityUserRegistrationBinding
import com.example.adda.models.BasicInfo
import com.example.adda.models.GoogleInfo
import com.example.adda.models.UserInfo
import com.example.adda.utils.Config
import com.example.adda.utils.SessionManager
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Objects


class UserRegistrationActivity : AppCompatActivity() {

    private lateinit var mDatabase: DatabaseReference
    private var binding: ActivityUserRegistrationBinding? = null
    private var sessionManager: SessionManager? = null

    var phone: String? = null
    var name: String? = null
    var email: String? = null
    var imageUri: String? = null
    var activeStatus = false
    var gender: String? = null
    var department: String? = null
    private var movie = ArrayList<BasicInfo>()
    private var music = ArrayList<BasicInfo>()
    private var sports = ArrayList<BasicInfo>()
    private lateinit var googleInfo: GoogleInfo
    private var isGoneMain = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_registration)

        sessionManager = SessionManager(this)
        mDatabase = FirebaseDatabase.getInstance().reference

        googleInfo = intent.getSerializableExtra("googleInfo") as GoogleInfo


        initToolbar()

        binding!!.loginName.setText(googleInfo.name)
        binding!!.loginEmail.text = googleInfo.email
        email = binding!!.loginEmail.text.toString()
        imageUri = googleInfo.imgUrl

        Glide.with(this)
            .load(googleInfo.imgUrl)
            .placeholder(R.drawable.round_perm_identity_24)
            .fitCenter()
            .error(R.drawable.round_perm_identity_24)
            .into(binding!!.profileImg)

        binding!!.rgGender.setOnCheckedChangeListener { group, checkedId ->
            // This will get the radiobutton that has changed in its check state
            val checkedRadioButton = group.findViewById<View>(checkedId) as RadioButton
            // This puts the value (true/false) into the variable
            val isChecked = checkedRadioButton.isChecked
            // If the radiobutton that has changed in check state is now checked...
            if (isChecked) {
                // Changes the textview text to "Checked: example radiobutton text"
                gender = checkedRadioButton.text.toString()
            }
        }

        binding!!.rgDepartment.setOnCheckedChangeListener { group, checkedId ->
            // This will get the radiobutton that has changed in its check state
            val checkedRadioButton = group.findViewById<View>(checkedId) as RadioButton
            // This puts the value (true/false) into the variable
            val isChecked = checkedRadioButton.isChecked
            // If the radiobutton that has changed in check state is now checked...
            if (isChecked) {
                // Changes the textview text to "Checked: example radiobutton text"
                department = checkedRadioButton.text.toString()
            }
        }

        binding!!.cbMovieAction.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val pushId = mDatabase.push().key
                val basicInfo = BasicInfo(pushId, binding!!.cbMovieAction.text.toString())
                movie.add(basicInfo)
            } else {
                for (m: BasicInfo in movie) {
                    if (m.name.equals(binding!!.cbMovieAction.text.toString())) {
                        movie.remove(m)
                    }
                }
            }
        }

        binding!!.cbMovieThriller.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val pushId = mDatabase.push().key
                val basicInfo = BasicInfo(pushId, binding!!.cbMovieThriller.text.toString())
                movie.add(basicInfo)
            } else {
                for (m: BasicInfo in movie) {
                    if (m.name.equals(binding!!.cbMovieThriller.text.toString())) {
                        movie.remove(m)
                    }
                }
            }
        }

        binding!!.cbRock.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val pushId = mDatabase.push().key
                val basicInfo = BasicInfo(pushId, binding!!.cbRock.text.toString())
                music.add(basicInfo)
            } else {
                for (m: BasicInfo in music) {
                    if (m.name.equals(binding!!.cbRock.text.toString())) {
                        music.remove(m)
                    }
                }
            }
        }

        binding!!.cbMetal.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val pushId = mDatabase.push().key
                val basicInfo = BasicInfo(pushId, binding!!.cbMetal.text.toString())
                music.add(basicInfo)
            } else {
                for (m: BasicInfo in music) {
                    if (m.name.equals(binding!!.cbMetal.text.toString())) {
                        music.remove(m)
                    }
                }
            }
        }

        binding!!.cbMovieCricket.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val pushId = mDatabase.push().key
                val basicInfo = BasicInfo(pushId, binding!!.cbMovieCricket.text.toString())
                sports.add(basicInfo)
            } else {
                for (m: BasicInfo in sports) {
                    if (m.name.equals(binding!!.cbMovieCricket.text.toString())) {
                        sports.remove(m)
                    }
                }
            }
        }

        binding!!.cbMovieFootball.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val pushId = mDatabase.push().key
                val basicInfo = BasicInfo(pushId, binding!!.cbMovieFootball.text.toString())
                sports.add(basicInfo)
            } else {
                for (m: BasicInfo in sports) {
                    if (m.name.equals(binding!!.cbMovieFootball.text.toString())) {
                        sports.remove(m)
                    }
                }
            }
        }

        binding!!.cbMovieHockey.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val pushId = mDatabase.push().key
                val basicInfo = BasicInfo(pushId, binding!!.cbMovieHockey.text.toString())
                sports.add(basicInfo)
            } else {
                for (m: BasicInfo in sports) {
                    if (m.name.equals(binding!!.cbMovieHockey.text.toString())) {
                        sports.remove(m)
                    }
                }
            }
        }

        binding!!.submitBtn.setOnClickListener { verifyUserInfo() }
    }

    private fun verifyUserInfo() {
        phone = binding!!.loginPhone.text.toString()
        name = binding!!.loginName.text.toString()
        email = binding!!.loginEmail.text.toString()

        if (name!!.isEmpty()) {
            binding!!.loginName.error = Html.fromHtml("<font color='red'>Name not empty</font>")
            binding!!.loginName.requestFocus()
        } else if (phone!!.length != 11) {
            binding!!.loginPhone.error =
                Html.fromHtml("<font color='red'>Input valid phone number</font>")
            binding!!.loginPhone.requestFocus()
        } else if (gender == null) {
            Config.showToast(this, "Select your gender")
        } else if (department == null) {
            Config.showToast(this, "Select your department")
        } else if (movie.size == 0) {
            Config.showToast(this, "Choose your favorite movie")
        } else if (music.size == 0) {
            Config.showToast(this, "Choose your favorite music")
        } else if (sports.size == 0) {
            Config.showToast(this, "Choose your favorite sports")
        } else {
            Config.showToast(this, "Loading....")


            val user = UserInfo(
                googleInfo.uId,
                phone,
                name,
                email,
                imageUri,
                true,
                gender,
                department,
                movie,
                music,
                sports
            )

            binding!!.progressCircular.visibility = View.VISIBLE
            //create new user
            mDatabase.child(Config.USER_KEY).child(googleInfo.uId!!).setValue(user)
                .addOnCompleteListener {
                    binding!!.progressCircular.visibility = View.GONE
                    Toast.makeText(
                        this@UserRegistrationActivity,
                        getString(R.string.update_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                    sessionManager!!.isLogedIn = true
                    sessionManager!!.userIdToken = googleInfo.uId
                    if (!isGoneMain) {
                        isGoneMain = true
                        val i = Intent(this@UserRegistrationActivity, MainActivity::class.java)
                        startActivity(i)
                        finish()
                    }
                }.addOnFailureListener { _: Exception? ->
                    binding!!.progressCircular.visibility = View.GONE
                    Toast.makeText(
                        this@UserRegistrationActivity,
                        R.string.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun initToolbar() {
        setSupportActionBar(binding!!.toolbarBlue)
        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Create Account"
        binding!!.toolbarBlue.setNavigationOnClickListener { onBackPressed() }
    }
}