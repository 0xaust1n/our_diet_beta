package com.odstudio.ourdiet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
        var img = findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.avatar)
        var userName = findViewById<TextView>(R.id.userName)
        var btnAdd = findViewById<Button>(R.id.btnAdd)
        img.alpha = 0f
        userName.alpha = 0f
        btnAdd.alpha = 0f
        // Button OnClick Function
        var search = findViewById<Button>(R.id.searchBtn)
        var etEmail = findViewById<EditText>(R.id.et_friendMail)
        var searchAble = false
        var email = ""
        var searchingUid = ""
        search.setOnClickListener {

            if (etEmail.text.toString() != "") {
                email = etEmail.text.toString()
                searchAble = true
            }
            if (searchAble) {
                Log.d("LOGGGGGGG", "Click?")
                img.alpha = 1f
                userName.alpha = 1f
                btnAdd.alpha = 1f
                FirebaseFirestore.getInstance().collection("Users").whereEqualTo("email", email)
                    .get().addOnSuccessListener {
                        it.forEach {
                            searchingUid.apply {
                                searchingUid = (it.get("uid")?.toString() ?: "")
                            }
                            userName.apply {
                                text = it.get("nick")?.toString() ?: ""
                            }
                        }
                    }
                val filename: String = searchingUid + "_avatar"
                var avatarRef = FirebaseStorage.getInstance().getReference("/images/$filename")
                    .downloadUrl.addOnSuccessListener {
                    Glide.with(applicationContext)
                        .asBitmap()
                        .load(it)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .into(img)
                }
                avatarRef.isSuccessful
            }
            // Search Code above
            var db = FirebaseFirestore.getInstance()
            var userUid = FirebaseAuth.getInstance().currentUser!!.uid
            var add = findViewById<Button>(R.id.btnAdd)
            var userStatus = "申請中"
            var friendStatus = "尚未確認"
            var receiveApply = ReceiveApply(userUid, friendStatus)
            var sendApply = ApplyFriend(searchingUid, userStatus)
            add.setOnClickListener {
                // Add database for Current User
                db.collection("FriendsOf$userUid")
                    .document(searchingUid).set(
                        sendApply,
                        SetOptions.merge()
                    )
                // Searching Friend
                db.collection("FriendsOf$searchingUid")
                    .document(userUid).set(
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
