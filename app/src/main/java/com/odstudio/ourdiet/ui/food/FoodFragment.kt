package com.odstudio.ourdiet.ui.food

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ExpandableListView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.odstudio.ourdiet.R
import com.odstudio.ourdiet.RecordingActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class FoodFragment : Fragment() {

    private var titleList: List<String>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_food, container, false)
        //Default The Date
        val dateText = root.findViewById<EditText>(R.id.et_todaydate)
        var sdf = SimpleDateFormat("yyyy-M-dd")
        var currentDate = sdf.format(Date())
        dateText.setText(currentDate)
        //Date Edit Text
        val c = Calendar.getInstance()
        val y = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            activity,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                dateText.setText("" + year + "/" + (monthOfYear + 1) + "/" + dayOfMonth)
            },
            y,
            month,
            day
        )
        dateText.isClickable = true
        dateText.showSoftInputOnFocus = false
        dateText.setOnClickListener {
            dpd.show()
        }
        var elv = root.findViewById<ExpandableListView>(R.id.elv_meal)
        var db = FirebaseFirestore.getInstance()
        var tag = "Database Ref"
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        var date = dateText.text.toString()
        val meal = listOf("早餐", "午餐", "晚餐", "其他")
        // Read Data
        //Breakfast
        var list4Breakfast = ArrayList<String>()
        db.collection("RecordOf$uid").document(date).collection("早餐").get()
            .addOnSuccessListener { reslut ->
                for (document in reslut) {
                    list4Breakfast.add(document.get("foodName").toString())
                    list4Breakfast.add(document.get("brand").toString())
                    list4Breakfast.add(document.get("calories").toString())
                    list4Breakfast.add(document.get("serving").toString())


                }
            }.addOnFailureListener { exception ->
                Log.d(
                    tag,
                    "Error getting documents: ",
                    exception
                )
            }
        //Lunch
        var list4Lunch = ArrayList<String>()
        db.collection("RecordOf$uid").document(date).collection("午餐").get()
            .addOnSuccessListener { reslut ->
                for (document in reslut) {
                    list4Lunch.add(document.get("foodName").toString())
                    list4Lunch.add(document.get("brand").toString())
                    list4Lunch.add(document.get("calories").toString())
                    list4Lunch.add(document.get("serving").toString())
                }
            }.addOnFailureListener { exception ->
                Log.d(
                    tag,
                    "Error getting documents: ",
                    exception
                )
            }
        //Dinner
        var list4Dinner = ArrayList<String>()
        db.collection("RecordOf$uid").document(date).collection("晚餐").get()
            .addOnSuccessListener { reslut ->
                for (document in reslut) {
                    list4Dinner.add(document.get("foodName").toString())
                    list4Dinner.add(document.get("brand").toString())
                    list4Dinner.add(document.get("calories").toString())
                    list4Dinner.add(document.get("serving").toString())
                }
            }.addOnFailureListener { exception ->
                Log.d(
                    tag,
                    "Error getting documents: ",
                    exception
                )
            }
        //Other
        var list4Other = ArrayList<String>()
        db.collection("RecordOf$uid").document(date).collection("其他").get()
            .addOnSuccessListener { reslut ->
                for (document in reslut) {
                    list4Other.add(document.get("foodName").toString())
                    list4Other.add(document.get("brand").toString())
                    list4Other.add(document.get("calories").toString())
                    list4Other.add(document.get("serving").toString())
                }
            }.addOnFailureListener { exception ->
                Log.d(
                    tag,
                    "Error getting documents: ",
                    exception
                )
            }
        var food = listOf(
            list4Breakfast,
            list4Lunch,
            list4Dinner,
            list4Other
        )
        val adapter = FoodAdapter(
            activity,
            meal,
            list4Breakfast,
            list4Lunch,
            list4Dinner,
            list4Other,
            food
        )
        elv.setAdapter(adapter)
        var btnAdd = root.findViewById<Button>(R.id.btn_addfood)
        btnAdd.setOnClickListener {
            val intent = Intent(activity, RecordingActivity::class.java)
            startActivity(intent)
        }

        return root
    }

}

