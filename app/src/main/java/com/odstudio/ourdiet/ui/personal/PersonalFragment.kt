package com.odstudio.ourdiet.ui.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.odstudio.ourdiet.R

class PersonalFragment : Fragment() {

    private lateinit var personalViewModel: PersonalViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        personalViewModel =
            ViewModelProviders.of(this).get(PersonalViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_personal, container, false)
        return root
    }
}