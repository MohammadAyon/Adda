package com.example.adda.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.adda.R
import com.example.adda.databinding.FragmentUserBinding
import com.example.adda.models.BasicInfo
import com.example.adda.models.UserInfo
import com.example.adda.utils.Config
import com.example.adda.utils.SessionManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserFragment : Fragment() {

    private lateinit var mDatabase: DatabaseReference
    private var binding: FragmentUserBinding? = null
    private var view: View? = null
    private var sessionManager: SessionManager? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (view == null) {
            binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false)
            view = binding!!.root
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireActivity())
        mDatabase = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        binding!!.logoutBtn.setOnClickListener {
            showLogoutConfirmAlert()
        }
        getUserInfo()

    }

    private fun showLogoutConfirmAlert() {
        val alertDialog = AlertDialog.Builder(context)

        alertDialog.apply {
            setTitle("Logout")
            setIcon(R.drawable.baseline_logout_24)
            setMessage("Are you sure ? You want to logout. ")
            setPositiveButton("Logout") { _: DialogInterface?, _: Int ->
                makeLogout()
            }
            setNegativeButton("No") { _, _ ->
                onDestroy()
            }

        }.create().show()
    }

    private fun makeLogout() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val mGoogleSignInClient =
            GoogleSignIn.getClient(requireActivity(), gso)

        mGoogleSignInClient.signOut()
            .addOnFailureListener { e: java.lang.Exception ->
                Log.e("tag", e.message!!)
                Toast.makeText(
                    requireActivity(),
                    getText(R.string.error),
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnCompleteListener { task: Task<Void?>? ->
                Log.e("tag", "sign out")
                sessionManager!!.clearSP()

                requireActivity().finish()
            }

    }

    private fun getUserInfo() {
        binding!!.progressCircular.visibility = View.VISIBLE
        val postListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (snapshot in dataSnapshot.children) {
                    val userInfo: UserInfo? = snapshot.getValue(UserInfo::class.java)

                    if (sessionManager!!.userIdToken.equals(userInfo!!.uid)){
                        setUserInfo(userInfo)
                        break
                    }
                    binding!!.progressCircular.visibility = View.GONE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show()
                binding!!.progressCircular.visibility = View.GONE
            }
        }
        mDatabase.child(Config.USER_KEY).addValueEventListener(postListener)
    }

    @SuppressLint("SetTextI18n")
    private fun setUserInfo(userInfo: UserInfo) {
        binding!!.loginName.text = userInfo.name
        binding!!.loginEmail.text = userInfo.email
        binding!!.loginPhone.text = userInfo.phone
        binding!!.rgGender.text = userInfo.gender
        binding!!.deptTv.text = "Department: " + userInfo.department

        try {
            Glide.with(requireActivity())
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
        } catch (e: Exception) {
            Log.e("tag", "UserFragment")
        }
    }

}