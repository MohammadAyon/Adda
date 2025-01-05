package com.example.adda.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adda.R
import com.example.adda.adapters.MessagesAdater
import com.example.adda.databinding.ActivityMessageBinding
import com.example.adda.models.Messages
import com.example.adda.utils.Config
import com.example.adda.utils.SessionManager
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import java.util.Date
import java.util.Objects

class MessageActivity : AppCompatActivity() {

    var chatRefrece: DatabaseReference? = null
    private var binding: ActivityMessageBinding? = null

    var ReciverImage: String? = null
    var ReciverUID:String? = null
    var ReciverName:String? = null
    var ReciverPhone:String? = null
    var SenderUID:String? = null

    var storageReference: StorageReference? = null
    var database: FirebaseDatabase? = null
    var storage: FirebaseStorage? = null
    var firebaseAuth: FirebaseAuth? = null

    var imageUri: Uri? = null
    var senderRoom: String? = null
    var reciverRoom:String? = null

    var messagesArrayList: ArrayList<Messages>? = null

    var adater: MessagesAdater? = null
    private val REQUEST_CODE_SPEECH_INPUT = 1000
    private var sessionManager: SessionManager? = null

    companion object{
        var sImage: String? = null
        var rImage: String? = null
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_message)

        sessionManager = SessionManager(this)

        database = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()

        ReciverName = intent.getStringExtra("name")
        ReciverPhone = intent.getStringExtra("phone")
        ReciverImage = intent.getStringExtra("imgUrl")
        ReciverUID = intent.getStringExtra("userId")

        binding!!.reciverName.text = ReciverName.toString()

        messagesArrayList = java.util.ArrayList()


        messagesArrayList = java.util.ArrayList()
        adater = MessagesAdater(this@MessageActivity, messagesArrayList)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        binding!!.messageAdater.layoutManager = linearLayoutManager
        binding!!.messageAdater.adapter = adater

        Picasso.get().load(ReciverImage).into(binding!!.profileImage)
        binding!!.reciverName.text = "" + ReciverName


        SenderUID = firebaseAuth!!.uid

        if (SenderUID == null){
            sessionManager!!.clearSP()

            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)

            finish()
            Config.showToast(this, "Please login again")
        }else{

            senderRoom = SenderUID + ReciverUID
            reciverRoom = ReciverUID + SenderUID


            val reference = database!!.reference.child(Config.USER_KEY).child(firebaseAuth!!.uid!!)
            chatRefrece = database!!.reference.child("chats").child(senderRoom!!).child("messages")


            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    sImage = Objects.requireNonNull<Any?>(snapshot.child("imageUri").value).toString()
                    rImage = ReciverImage

                    adater!!.setImgHead(rImage, sImage)
                }

                override fun onCancelled(error: DatabaseError) {}
            })

            chatRefrece!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messagesArrayList!!.clear()
                    for (dataSnapshot in snapshot.children) {
                        val messages = dataSnapshot.getValue(Messages::class.java)
                        messagesArrayList!!.add(messages!!)
                    }
                    adater!!.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }



        binding!!.sendBtn.setOnClickListener { sendMessage() }
        binding!!.imgSendBtn.setOnClickListener{ openGallery() }
        binding!!.closeImgBtn.setOnClickListener{ actionCloseImg() }
        binding!!.sendImgBtn.setOnClickListener{ sendImg() }
        binding!!.imageView.setOnClickListener{ onBackPressed() }

    }

    private fun sendImg() {
        binding!!.progressBar.setVisibility(View.VISIBLE)
        val date = Date()
        storageReference = storage!!.reference.child("uplod").child(date.time.toString())
        if (imageUri != null) {
            storageReference!!.putFile(imageUri!!)
                .addOnCompleteListener { task: Task<UploadTask.TaskSnapshot?> ->
                    if (task.isSuccessful) {
                        storageReference!!.downloadUrl
                            .addOnSuccessListener { uri ->
                                val imageURI = uri.toString()
                                binding!!.edtMessage.setText("")
                                val date1 = Date()
                                val messages =
                                    Messages("", SenderUID, imageURI, date1.time)
                                database!!.reference.child("chats")
                                    .child(senderRoom!!)
                                    .child("messages")
                                    .push()
                                    .setValue(messages).addOnCompleteListener {
                                        database!!.reference.child("chats")
                                            .child(reciverRoom!!)
                                            .child("messages")
                                            .push().setValue(messages)
                                            .addOnCompleteListener {
                                                binding!!.progressBar.setVisibility(View.GONE)
                                                binding!!.imageLayout.setVisibility(View.GONE)
                                                binding!!.messageLayout.setVisibility(View.VISIBLE)
                                                imageUri = null
                                            }
                                    }
                            }
                    } else {
                        binding!!.progressBar.setVisibility(View.GONE)
                        Toast.makeText(this@MessageActivity, "Error in send image", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }

    private fun openGallery() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, 10)
    }

    private fun actionCloseImg() {
        binding!!.imageLayout.setVisibility(View.GONE)
        binding!!.messageLayout.setVisibility(View.VISIBLE)
        imageUri = null
    }

    private fun sendMessage() {
        val message: String = binding!!.edtMessage.getText().toString()
        if (message.isEmpty()) {
            Toast.makeText(this@MessageActivity, "Please enter Valid Message", Toast.LENGTH_SHORT)
                .show()
            return
        }
        binding!!.edtMessage.setText("")
        val date = Date()
        val messages = Messages(message, SenderUID, "", date.time)
        database!!.reference.child("chats")
            .child(senderRoom!!)
            .child("messages")
            .push()
            .setValue(messages).addOnCompleteListener {
                database!!.reference.child("chats")
                    .child(reciverRoom!!)
                    .child("messages")
                    .push().setValue(messages).addOnCompleteListener { }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SPEECH_INPUT && data != null) {
            // get the text array from voice intent
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (result!![0].contains("call him") || result[0].contains("call her")) {

            } else if (result[0].contains("send")) {
                sendMessage()
            } else if (result[0].contains("send image") || result[0].contains("send photo")) {
                sendImg()
            } else if (result[0].contains("open gallery")) {
                openGallery()
            } else if (result[0].contains("back")) {
                onBackPressed()
                finish()
            } else if (result[0].contains("cancel send")) {
                actionCloseImg()
            } else if (result[0].contains("clear message")) {
                binding!!.edtMessage.setText("")
            } else {
                binding!!.edtMessage.setText(result[0])
                //Toast.makeText(this, "Can't detect! try again", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 10) {
            if (data != null) {
                imageUri = data.data
                binding!!.imgSendIv.setImageURI(imageUri)
                binding!!.imageLayout.setVisibility(View.VISIBLE)
                binding!!.messageLayout.setVisibility(View.GONE)
            } else {
                binding!!.imageLayout.setVisibility(View.GONE)
                binding!!.messageLayout.setVisibility(View.VISIBLE)
            }
        } else {
            Toast.makeText(this@MessageActivity, "Something went wrong! try again", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}