package com.odstudio.ourdiet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.odstudio.ourdiet.dataClass.ApplyFriend
import com.odstudio.ourdiet.dataClass.ReceiveApply

class AddFriendActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)
        // Hide the Stuffs
        var avatar = findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.avatar)
        var nick = findViewById<TextView>(R.id.userName)
        var btnAdd = findViewById<Button>(R.id.btnAdd)
        avatar.alpha = 0f
        nick.alpha = 0f
        btnAdd.alpha = 0f
        // Current User Info
        var db = FirebaseFirestore.getInstance()
        var currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        var currentAvatar = currentUser + "_avatar"
        var currentNick =""
        var currentEmail =""
        FirebaseFirestore.getInstance().collection("Users").whereEqualTo("uid", currentUser)
            .get().addOnSuccessListener {
                it.forEach {
                    currentNick.apply {
                        currentNick = it.get("nick")?.toString() ?: ""
                    }
                    currentEmail.apply {
                        currentEmail = it.get("email")?.toString() ?: ""
                    }
                }
            }
        // Button OnClick Function
        var search = findViewById<Button>(R.id.searchBtn)
        var etEmail = findViewById<EditText>(R.id.et_friendMail)
        var searchAble = false
        var searchingMail = etEmail.text.toString()
        var searchingUid = ""
        var searchingNIck =""
        var searchingFilename = ""
        search.setOnClickListener {

            if (etEmail.text.toString() != "" && etEmail.text.toString() != currentEmail) {
                searchingMail = etEmail.text.toString()
                searchAble = true
            }
            if (searchAble) {
                avatar.alpha = 1f
                nick.alpha = 1f
                btnAdd.alpha = 1f
                FirebaseFirestore.getInstance().collection("Users").whereEqualTo("email", searchingMail)
                    .get().addOnSuccessListener {
                        it.forEach {
                            searchingUid.apply {
                                searchingUid = it.get("uid")?.toString() ?: ""
                                searchingFilename = searchingUid + "_avatar"
                            }
                            nick.apply {
                                text = it.get("nick")?.toString() ?: ""
                                searchingNIck = it.get("nick")?.toString() ?: ""
                            }
                        }
                    }
                var avatarRef = FirebaseStorage.getInstance().getReference("/images/$searchingFilename")
                    .downloadUrl.addOnSuccessListener {
                    Glide.with(applicationContext)
                        .asBitmap()
                        .load(it)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .into(avatar)
                }
                avatarRef.isSuccessful
            }
            // Search Code above
            var add = findViewById<Button>(R.id.btnAdd)
            var userStatus = "申請中"
            var friendStatus = "尚未確認"
            var receiveApply = ReceiveApply(currentNick,currentUser,currentAvatar,currentEmail, friendStatus)
            var sendApply = ApplyFriend(searchingNIck,searchingUid,searchingFilename,searchingMail,userStatus)
            add.setOnClickListener {
                // Add database for Current User
                db.collection("FriendsOf$currentUser")
                    .document(searchingUid).set(
                        sendApply,
                        SetOptions.merge()
                    )
                // Searching Friend
                db.collection("FriendsOf$searchingUid")
                    .document(currentUser).set(
                        receiveApply,
                        SetOptions.merge()
                    )
                Toast.makeText(this, "已送出好友邀請", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }
        }
    }
}