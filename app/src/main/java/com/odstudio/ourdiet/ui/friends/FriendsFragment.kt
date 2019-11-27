package com.odstudio.ourdiet.ui.friends


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.odstudio.ourdiet.R


class FriendsFragment : Fragment() {
    private var count = 0
    private var countTwo = 0
    private var listOfAccept = ArrayList<String>()
    private var listOfRequest = ArrayList<String>()
    private var listOfFriends = ArrayList<String>()
    private var applyList = ArrayList<String>()
    private var friendsList = ArrayList<String>()
    private var applyPosition = ArrayList<Int>()
    private var friendsPosition = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var db = FirebaseFirestore.getInstance()
        var currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        db.collection("FriendsOf$currentUser").whereEqualTo("status", "申請中").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    listOfRequest.add(document.get("uid").toString())
                }
            }
        db.collection("FriendsOf$currentUser").whereEqualTo("status", "尚未確認").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    listOfAccept.add(document.get("uid").toString())
                }
            }
        db.collection("FriendsOf$currentUser").whereEqualTo("status", "完成").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    listOfFriends.add(document.get("uid").toString())
                }
            }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_friends, container, false)
        var request = root.findViewById<ListView>(R.id.requestList)
        //Pull Data From Database
        var db = FirebaseFirestore.getInstance()

        // Requesting Array
        if (listOfRequest.size > 0) {
            for (x in 0..listOfRequest.size) {
                var uid = listOfRequest[x]
                db.collection("Users").whereEqualTo("uid", uid).get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            applyList.add(document.get("avatar").toString())
                            applyList.add(document.get("nick").toString())
                            Log.d("nick", (document.get("nick").toString()))
                            applyList.add((document.get("email")).toString())
                            applyList.add("申請中")
                            applyPosition.add(count)
                            count += 3
                        }
                    }
            }
        }
        // Accept Array
        if (listOfAccept.size > 0) {
            for (x in 0..listOfAccept.size) {
                db.collection("Users").whereEqualTo("uid", listOfAccept[x]).get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            applyList.add(document.get("avatar").toString())
                            applyList.add(document.get("nick").toString())
                            applyList.add((document.get("email")).toString())
                            applyList.add("尚未確認")
                            applyPosition.add(count)
                            count += 3
                        }
                    }
            }
        }
        // Friends List
        if (listOfFriends.size > 0) {
            for (x in 0..listOfFriends.size) {
                db.collection("Users").whereEqualTo("uid", listOfFriends[x]).get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            friendsList.add(document.get("avatar").toString())
                            friendsList.add(document.get("nick").toString())
                            friendsList.add((document.get("email")).toString())
                            friendsPosition.add(countTwo)
                            countTwo += 3
                        }
                    }
            }
        }

        val applyAdapter = ApplyAdapter(activity, applyList, applyPosition)
        request.adapter = applyAdapter
        return root
    }
}
