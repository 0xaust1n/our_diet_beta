package com.odstudio.ourdiet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.odstudio.ourdiet.ui.friends.FriendsFragment
import kotlinx.android.synthetic.main.activity_personal.*

class FriendRequestActivity : AppCompatActivity() {
    private var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_request)
        // Pull the data From Friends Fragment
        var uid = intent.getStringExtra("uid")
        var email = intent.getStringExtra("email")
        var nick = intent.getStringExtra("nick")
        var status = intent.getStringExtra("status")
        // Toast.makeText(this, "$uid-$email-$nick-$status", Toast.LENGTH_SHORT).show()
        //Declare the Button
        var accept = findViewById<Button>(R.id.acceptBtn)
        var reject = findViewById<Button>(R.id.rejectBtn)
        //Set Text
        var nickName = findViewById<TextView>(R.id.rqNick)
        nickName.text = nick
        var etStatus = findViewById<TextView>(R.id.rqStatus)
        etStatus.text = status
        var etEmail = findViewById<TextView>(R.id.rqEmail)
        etEmail.text = email
        //Set Avatar
        var filename = uid + "_avatar"
        var avatar = findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.rqAvatar)
        var avatarRef = FirebaseStorage.getInstance().getReference("/images/$filename")
            .downloadUrl.addOnSuccessListener {
            Glide.with(applicationContext)
                .asBitmap()
                .load(it)
                .into(avatar)
        }
        avatarRef.isSuccessful
        // Show button or NOot
        if (status != "尚未確認") {
            accept.alpha = 0f
            reject.alpha = 0f
        }
        var currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        accept.setOnClickListener {
            db.collection("FriendsOf$uid").document(currentUser).update("status", "完成")
            db.collection("FriendsOf$currentUser").document(uid).update("status", "完成")
            Toast.makeText(this, "已經接受好友請求", Toast.LENGTH_SHORT).show()
            Thread {
                Runnable {
                    Thread.sleep(200)
                    val intent = Intent(this, FriendsFragment::class.java)
                    startActivity(intent)
                }
            }
        }
        reject.setOnClickListener {
            db.collection("FriendsOf$uid").document(currentUser).delete()
            db.collection("FriendsOf$currentUser").document(uid).delete()
            Toast.makeText(this, "已經拒絕好友請求", Toast.LENGTH_SHORT).show()
            Thread {
                Runnable {
                    Thread.sleep(200)
                    val intent = Intent(this, FriendsFragment::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
