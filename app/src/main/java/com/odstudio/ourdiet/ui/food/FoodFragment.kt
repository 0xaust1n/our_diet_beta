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
import kotlinx.android.synthetic.main.fragment_food.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FoodFragment : Fragment() {

    private var food = ArrayList<String>()
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
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            activity,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                dateText.setText("" + year + "/" + (monthOfYear + 1) + "/" + dayOfMonth)
            },
            year,
            month,
            day
        )
        dateText.isClickable = true
        dateText.showSoftInputOnFocus = false
        dateText.setOnClickListener {
            dpd.show()
        }

        var db = FirebaseFirestore.getInstance()
        var TAG = "Database Ref"
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        var elv = root.findViewById<ExpandableListView>(R.id.elv_meal)
        var date = dateText.text.toString()
        val meal = listOf("早餐", "午餐", "晚餐", "小食/其他")
        var food = listOf(
            listOf(
                db.collection("RecordOf$uid").whereEqualTo("date", date).whereGreaterThan(
                    "meal",
                    "早餐"
                ).get().addOnSuccessListener { reslut ->
                    for (document in reslut) {
                        food.add(document.get("foodName").toString())
                    }
                }.addOnFailureListener { exception ->
                    Log.d(
                        TAG,
                        "Error getting documents: ",
                        exception
                    )
                }),
            listOf(
                db.collection("RecordOf$uid").whereEqualTo("date", date).whereGreaterThan(
                    "meal",
                    "午餐"
                ).get().addOnSuccessListener { reslut ->
                    for (document in reslut) {
                        food.add(document.get("foodName").toString())
                    }
                }.addOnFailureListener { exception ->
                    Log.d(
                        TAG,
                        "Error getting documents: ",
                        exception
                    )
                }),
            listOf(
                db.collection("RecordOf$uid").whereEqualTo("date", date).whereGreaterThan(
                    "meal",
                    "晚餐"
                ).get().addOnSuccessListener { reslut ->
                    for (document in reslut) {
                        food.add(document.get("foodName").toString())
                    }
                }.addOnFailureListener { exception ->
                    Log.d(
                        TAG,
                        "Error getting documents: ",
                        exception
                    )
                }),
            listOf(
                db.collection("RecordOf$uid").whereEqualTo("date", date).whereGreaterThan(
                    "meal",
                    "小食/其他"
                ).get().addOnSuccessListener { reslut ->
                    for (document in reslut) {
                        food.add(document.get("foodName").toString())
                    }
                }.addOnFailureListener { exception ->
                    Log.d(
                        TAG,
                        "Error getting documents: ",
                        exception
                    )
                }
            )
        )
        val adapter = FoodAdapter(activity, meal, food)
        elv.setAdapter(adapter)

        var btn_addrecord = root.findViewById<Button>(R.id.btn_addfood)
        btn_addrecord.setOnClickListener {
            val intent = Intent(activity, RecordingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        return root
    }
}
