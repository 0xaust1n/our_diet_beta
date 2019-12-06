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


class FoodFragment : Fragment() {
    // Global Declare Below
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
    private var adapter: FoodAdapter?=null
    private var cal = Calendar.getInstance()
    private var sdf = SimpleDateFormat("yyyy-M-dd")
    // Start OnCreateView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_food, container, false)

        //Add Button
        var btnAdd = root.findViewById<Button>(R.id.btn_addFood)
        btnAdd.setOnClickListener {
            val intent = Intent(activity, RecordingActivity::class.java)
            startActivity(intent)
        }
        //Default The Date
        val dateText = root.findViewById<EditText>(R.id.et_dateTime)

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
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH , dayOfMonth)

                dateText.setText(sdf.format(cal.time));

                readDate( dateText.text.toString() )
                adapter?.notifyDataSetChanged()
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

        readDate( dateText.text.toString() )
        var elv = root.findViewById<ExpandableListView>(R.id.elv_meal)
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
        adapter = FoodAdapter(
            activity,
            meal,
            food,
            child
        )
        elv.setAdapter(adapter)

        return root
    }

    private fun readDate( foodOfDate : String ) {
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

