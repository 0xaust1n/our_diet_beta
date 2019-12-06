package com.odstudio.ourdiet.ui.friends


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.odstudio.ourdiet.FriendRequestActivity
import com.odstudio.ourdiet.FriendsRecordingActivity
import com.odstudio.ourdiet.R


class FriendsFragment : Fragment() {
    private var list4Applying = ArrayList<String>()
    private var count4Applying = 0
    private var list4Friends = ArrayList<String>()
    private var count4Friends = 0
    private var child4Apply = ArrayList<Int>()
    private var child4Friends = ArrayList<Int>()
    private var adapter: FriendsAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_friends, container, false)
        var elv = root.findViewById<ExpandableListView>(R.id.elv_friends)
        val groupName = listOf("申請中", "好友清單")
        readData()
        Thread{
            Thread.sleep(1000)
            Log.d("www",list4Applying.toString())
        }
        var friendList = listOf(
            list4Applying,
            list4Friends
        )
        var child = listOf(
            child4Apply,
            child4Friends
        )
        adapter = FriendsAdapter(
            activity,
            groupName,
            friendList,
            child
        )
        elv.setAdapter(adapter)
        elv.setOnChildClickListener {
                parent, v, groupPosition, childPosition, id ->
            if(friendList[groupPosition][childPosition+3] != "完成") {
                val intent = Intent(activity, FriendRequestActivity::class.java)
                intent.putExtra("uid", friendList[groupPosition][childPosition])
                intent.putExtra("email", friendList[groupPosition][childPosition + 1])
                intent.putExtra("nick", friendList[groupPosition][childPosition + 2])
                intent.putExtra("status", friendList[groupPosition][childPosition + 3])
                startActivity(intent)
            }
            else{
                val intent = Intent(activity, FriendsRecordingActivity::class.java)
                intent.putExtra("uid", friendList[groupPosition][childPosition])
                intent.putExtra("email", friendList[groupPosition][childPosition + 1])
                intent.putExtra("nick", friendList[groupPosition][childPosition + 2])
                startActivity(intent)
            }
            false

        }

        return root
    }
    private fun readData(){
        var db = FirebaseFirestore.getInstance()
        var tag = "Database Ref"
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        list4Applying.clear()
        db.collection("FriendsOf$uid")
            .whereEqualTo("status","申請中").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    list4Applying.add(document.get("uid").toString())
                    list4Applying.add(document.get("email").toString())
                    list4Applying.add(document.get("nick").toString())
                    list4Applying.add(document.get("status").toString())
                    child4Apply.add(count4Applying)
                    count4Applying += 4
                }
            }.addOnFailureListener { exception ->
                Log.d(
                    tag,
                    "Error getting documents: ",
                    exception
                )
            }
        db.collection("FriendsOf$uid")
            .whereEqualTo("status","尚未確認").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    list4Applying.add(document.get("uid").toString())
                    list4Applying.add(document.get("email").toString())
                    list4Applying.add(document.get("nick").toString())
                    list4Applying.add(document.get("status").toString())
                    child4Apply.add(count4Applying)
                    count4Applying += 4
                }
            }.addOnFailureListener { exception ->
                Log.d(
                    tag,
                    "Error getting documents: ",
                    exception
                )
            }
        list4Friends.clear()
        db.collection("FriendsOf$uid").whereEqualTo("status", "完成").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    list4Friends.add(document.get("uid").toString())
                    list4Friends.add(document.get("email").toString())
                    list4Friends.add(document.get("nick").toString())
                    list4Friends.add(document.get("status").toString())
                    child4Friends.add(count4Friends)
                    count4Friends += 4
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
