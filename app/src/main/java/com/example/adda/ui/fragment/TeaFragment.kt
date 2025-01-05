package com.example.adda.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adda.R
import com.example.adda.adapters.UserAdapter
import com.example.adda.databinding.FragmentTeaBinding
import com.example.adda.models.UserInfo
import com.example.adda.ui.activity.InfoActivity
import com.example.adda.ui.activity.MessageActivity
import com.example.adda.utils.Config
import com.example.adda.utils.SessionManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TeaFragment : Fragment(), UserAdapter.ItemUserClickListener {

    private lateinit var mDatabase: DatabaseReference
    private var binding: FragmentTeaBinding? = null
    private var view: View? = null
    private var sessionManager: SessionManager? = null
    var userInfos = ArrayList<UserInfo>()

    private var adapter: UserAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (view == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tea, container, false)
            view = binding!!.root
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireActivity())
        mDatabase = FirebaseDatabase.getInstance().reference

        getUserInfo()
    }

    private fun getUserInfo() {

        binding!!.progressCircular.visibility = View.VISIBLE
        val postListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                userInfos.clear()

                for (snapshot in dataSnapshot.children) {
                    val userInfo: UserInfo? = snapshot.getValue(UserInfo::class.java)

                    if (!sessionManager!!.userIdToken.equals(userInfo!!.uid) && userInfo.isActiveStatus){
                        userInfos.add(userInfo)
                    }

                    binding!!.progressCircular.visibility = View.GONE
                }

                try {
                    binding!!.userRv.layoutManager = LinearLayoutManager(requireActivity())
                    adapter = UserAdapter(userInfos, requireActivity(), this@TeaFragment, true)
                    binding!!.userRv.adapter = adapter
                } catch (e: Exception) {
                    Log.e("tag", "TeaFragment")
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

    override fun onUserClick(userInfo: UserInfo?) {

        val user = UserInfo(userInfo!!.uid,
            userInfo.phone,
            userInfo.name,
            userInfo.email,
            userInfo.imageUri,
            userInfo.isActiveStatus,
            userInfo.gender,
            userInfo.department,
            userInfo.movie,
            userInfo.music,
            userInfo.sports)

        val i = Intent(requireContext(), MessageActivity::class.java)
        i.putExtra("name", user.name)
        i.putExtra("phone", user.phone)
        i.putExtra("userId", user.uid)
        i.putExtra("imgUrl", user.imageUri)
        startActivity(i)
    }

    override fun onUserInfoClick(userInfo: UserInfo?) {
        val i = Intent(requireActivity(), InfoActivity::class.java)
        i.putExtra("userInfo", userInfo)
        startActivity(i)
    }
}