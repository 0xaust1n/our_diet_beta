package com.odstudio.ourdiet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

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
        var uid = ""
        search.setOnClickListener {

            if (etEmail.text.toString() != "") {
                email = etEmail.text.toString()
                searchAble = true
            }
            if (searchAble) {
                Log.d("LOGGGGGGG","Click?")
                img.alpha = 1f
                userName.alpha = 1f
                btnAdd.alpha = 1f
                FirebaseFirestore.getInstance().collection("Users").whereEqualTo("email", email)
                    .get().addOnSuccessListener {
                        it.forEach {
                            uid.apply {
                                uid = (it.get("uid")?.toString() ?: "")
                            }
                            userName.apply {
                                text = it.get("nick")?.toString() ?: ""
                            }
                        }
                    }
                val filename = uid + "_avatar"
                var avatarRef = FirebaseStorage.getInstance().getReference("/images/$filename")
                    .downloadUrl.addOnSuccessListener {
                    Glide.with(applicationContext)
                        .asBitmap()
                        .load(it)
                        .into(img)

                }
                avatarRef.isSuccessful
            }
        }
    }
}
