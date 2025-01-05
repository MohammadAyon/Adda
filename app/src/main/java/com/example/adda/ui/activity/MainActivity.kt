package com.example.adda.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.adda.R
import com.example.adda.databinding.ActivityMainBinding
import com.example.adda.models.UserInfo
import com.example.adda.ui.fragment.ChatFragment
import com.example.adda.ui.fragment.TeaFragment
import com.example.adda.ui.fragment.UserFragment
import com.example.adda.utils.Config
import com.example.adda.utils.SessionManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private var isHaveUser = false
    private var binding: ActivityMainBinding? = null
    private lateinit var mDatabase: DatabaseReference
    private var sessionManager: SessionManager? = null

    private val fm = supportFragmentManager
    private val userFragment: Fragment = UserFragment()
    private val teaFragment: Fragment = TeaFragment()
    private val chatFragment: Fragment = ChatFragment()
    private var isGoneMain = false

    var active: Fragment = teaFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mDatabase = FirebaseDatabase.getInstance().reference
        sessionManager = SessionManager(this)

        initToolbar()

        fm.beginTransaction().add(R.id.nav_host_fragment, chatFragment, "3")
            .hide(chatFragment).commit()
        fm.beginTransaction().add(R.id.nav_host_fragment, teaFragment, "2")
            .show(teaFragment).commit()
        fm.beginTransaction().add(R.id.nav_host_fragment, userFragment, "1")
            .hide(userFragment).commit()

        binding!!.bottomNavigation.selectedItemId = R.id.nav_tea;

        binding!!.bottomNavigation.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_user -> {
                    fm.beginTransaction().hide(active).show(userFragment).commit()
                    active = userFragment
                    supportActionBar!!.title = "Profile"
                    return@setOnItemSelectedListener true
                }

                R.id.nav_tea -> {
                    fm.beginTransaction().hide(active).show(teaFragment).commit()
                    active = teaFragment
                    supportActionBar!!.title = "Friends"
                    return@setOnItemSelectedListener true
                }

                R.id.nav_chat -> {
                    fm.beginTransaction().hide(active).show(chatFragment).commit()
                    active = chatFragment
                    supportActionBar!!.title = "Chats"
                    return@setOnItemSelectedListener true
                }

                else -> return@setOnItemSelectedListener false
            }
        }

        getUserInfo(true)
    }

    private fun getUserInfo(value: Boolean) {

        mDatabase.child(Config.USER_KEY).child(sessionManager!!.userIdToken!!)
            .child(Config.ACTIVE_KEY).setValue(value)
    }

    private fun initToolbar() {
        setSupportActionBar(binding!!.toolbarBlue)
        supportActionBar!!.title = "Friends"
    }

    override fun onDestroy() {
        super.onDestroy()

        mDatabase.child(Config.USER_KEY).child(sessionManager!!.userIdToken!!)
            .child(Config.ACTIVE_KEY).setValue(false)

    }
}