package com.example.adda.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.database.DatabaseUtils
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.adda.R
import com.example.adda.databinding.ActivityInfoBinding
import com.example.adda.databinding.ActivityUserRegistrationBinding
import com.example.adda.models.BasicInfo
import com.example.adda.models.GoogleInfo
import com.example.adda.models.UserInfo
import com.example.adda.utils.Config
import java.util.Objects

class InfoActivity : AppCompatActivity() {
    private var binding: ActivityInfoBinding? = null
    private lateinit var userInfo: UserInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_info)

        userInfo = intent.getSerializableExtra("userInfo") as UserInfo

        initToolbar()
        setUserInfo(userInfo)

        binding!!.sendBtn.setOnClickListener {
            val i = Intent(this, MessageActivity::class.java)
            i.putExtra("name", userInfo.name)
            i.putExtra("phone", userInfo.phone)
            i.putExtra("userId", userInfo.uid)
            i.putExtra("imgUrl", userInfo.imageUri)
            startActivity(i)
        }
    }

    private fun initToolbar() {
        setSupportActionBar(binding!!.toolbarBlue)
        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = userInfo.name
        binding!!.toolbarBlue.setNavigationOnClickListener { onBackPressed() }
    }

    @SuppressLint("SetTextI18n")
    private fun setUserInfo(userInfo: UserInfo) {
        binding!!.loginName.text = userInfo.name
        binding!!.loginEmail.text = userInfo.email
        binding!!.loginPhone.text = userInfo.phone
        binding!!.rgGender.text = userInfo.gender
        binding!!.deptTv.text = "Department: " + userInfo.department

        Glide.with(this)
            .load(userInfo.imageUri)
            .placeholder(R.drawable.round_perm_identity_24)
            .fitCenter()
            .error(R.drawable.round_perm_identity_24)
            .into(binding!!.profileImg)

        var movieList: String? = ""

        for (list: BasicInfo in userInfo.movie!!){
            movieList = movieList + ", " + list.name
        }
        binding!!.movieListTv.text = movieList!!.substring(2)

        var musicList: String? = ""

        for (list: BasicInfo in userInfo.music!!){
            musicList = musicList + ", " + list.name
        }
        binding!!.musicListTv.text = musicList!!.substring(2)

        var sportsList: String? = ""

        for (list: BasicInfo in userInfo.sports!!){
            sportsList = sportsList + ", " + list.name
        }
        binding!!.sportsListTv.text = sportsList!!.substring(2)
    }
}