package com.odstudio.ourdiet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ExpandableListView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FriendsRecordingActivity : AppCompatActivity() {
    private var list4Breakfast = ArrayList<String>()
    private var list4Lunch = ArrayList<String>()
    private var list4Dinner = ArrayList<String>()
    private var list4Other = ArrayList<String>()
    private var child4Breakfast = ArrayList<Int>()
    private var count4Breakfast = 0
    private var child4Lunch = ArrayList<Int>()
    private var count4Lunch = 0
    private var child4Dinner = ArrayList<Int>()
    private var count4Dinner = 0
    private var child4Other = ArrayList<Int>()
    private var count4Other = 0
    private var adapter: FriendRecordingAdapter? = null
    private var sdf = SimpleDateFormat("yyyy-M-dd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends_recording)
        //Pull Data Form Friends Fragment
        var uid = intent.getStringExtra("uid")
        var email = intent.getStringExtra("email")
        var nick = intent.getStringExtra("nick")
        //Set Avatar
        var filename = uid + "_avatar"
        var avatar = findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.rcAvatar)
        var avatarRef = FirebaseStorage.getInstance().getReference("/images/$filename")
            .downloadUrl.addOnSuccessListener {
            Glide.with(applicationContext)
                .asBitmap()
                .load(it)
                .into(avatar)
        }
        avatarRef.isSuccessful
        //Set Text
        var rcNick = findViewById<TextView>(R.id.rcNick)
        rcNick.text = nick
        var rcEmail = findViewById<TextView>(R.id.rcMail)
        rcEmail.text = email
        var currentDate = sdf.format(Date())
        readDate(currentDate)
        var flv = findViewById<ExpandableListView>(R.id.F_elv_meal)
        val meal = listOf("早餐", "午餐", "晚餐", "其他")
        var food = listOf(
            list4Breakfast,
            list4Lunch,
            list4Dinner,
            list4Other
        )
        var child = listOf(
            child4Breakfast,
            child4Lunch,
            child4Dinner,
            child4Other
        )
        adapter = FriendRecordingAdapter(
            this,
            meal,
            food,
            child
        )
        flv.setAdapter(adapter)


    }

    private fun readDate(foodOfDate: String) {
        var db = FirebaseFirestore.getInstance()
        var tag = "Database Ref"
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        list4Breakfast.clear()
        //Breakfast
        db.collection("RecordOf$uid").document(foodOfDate).collection("早餐").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    list4Breakfast.add(document.get("foodName").toString())
                    list4Breakfast.add(document.get("brand").toString())
                    list4Breakfast.add(document.get("calories").toString())
                    list4Breakfast.add(document.get("serving").toString())
                    child4Breakfast.add(count4Breakfast)
                    count4Breakfast += 4
                }
            }.addOnFailureListener { exception ->
                Log.d(
                    tag,
                    "Error getting documents: ",
                    exception
                )
            }
        //Lunch
        list4Lunch.clear()
        db.collection("RecordOf$uid").document(foodOfDate).collection("午餐").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    list4Lunch.add(document.get("foodName").toString())
                    list4Lunch.add(document.get("brand").toString())
                    list4Lunch.add(document.get("calories").toString())
                    list4Lunch.add(document.get("serving").toString())
                    child4Lunch.add(count4Lunch)
                    count4Lunch += 4
                }
            }.addOnFailureListener { exception ->
                Log.d(
                    tag,
                    "Error getting documents: ",
                    exception
                )
            }
        //Dinner
        list4Dinner.clear()
        db.collection("RecordOf$uid").document(foodOfDate).collection("晚餐").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    list4Dinner.add(document.get("foodName").toString())
                    list4Dinner.add(document.get("brand").toString())
                    list4Dinner.add(document.get("calories").toString())
                    list4Dinner.add(document.get("serving").toString())
                    child4Dinner.add(count4Dinner)
                    count4Dinner += 4
                }
            }.addOnFailureListener { exception ->
                Log.d(
                    tag,
                    "Error getting documents: ",
                    exception
                )
            }
        //Other
        list4Other.clear()
        db.collection("RecordOf$uid").document(foodOfDate).collection("其他").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    list4Other.add(document.get("foodName").toString())
                    list4Other.add(document.get("brand").toString())
                    list4Other.add(document.get("calories").toString())
                    list4Other.add(document.get("serving").toString())
                    child4Other.add(count4Other)
                    count4Other += 4
                }
            }.addOnFailureListener { exception ->
                Log.d(
                    tag,
                    "Error getting documents: ",
                    exception
                )
            }
    }
}