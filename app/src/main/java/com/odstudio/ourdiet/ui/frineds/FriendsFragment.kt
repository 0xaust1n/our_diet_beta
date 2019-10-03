package com.odstudio.ourdiet.ui.frineds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.odstudio.ourdiet.R


class FriendsFragment : Fragment() {

    private lateinit var FriendsViewModel:FriendsViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_friends, container, false)
        return root
    }
}