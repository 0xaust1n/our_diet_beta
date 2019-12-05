package com.odstudio.ourdiet.ui.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.odstudio.ourdiet.R
import com.odstudio.ourdiet.dataClass.UserItem
import com.odstudio.ourdiet.dataClass.UserInfo as UserInfo


class FriendsFragment : Fragment() {
    private var db = FirebaseFirestore.getInstance()
    private var currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    private var userInfo: MutableList<UserInfo> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_friends, container, false)

        db.collection("FriendsOf$currentUser").whereEqualTo("status", "申請中").get()
            .addOnSuccessListener { document ->
                if(document != null) {
                    userInfo = document.toObjects(UserInfo::class.java)
                    print(userInfo)
                }
            }
                return root
            }
    }