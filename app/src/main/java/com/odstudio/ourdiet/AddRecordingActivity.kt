package com.odstudio.ourdiet


import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.odstudio.ourdiet.Data_Class.AddFoodList
import java.text.SimpleDateFormat
import java.util.*


class AddRecordingActivity : AppCompatActivity() {
    //Global Declare
    private lateinit var selection: String
    private lateinit var mealSelection: String
    private lateinit var dateText: EditText
    // Staring OnCreate Below
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recording)
        sp() // spinner
        selection = intent.getStringExtra("Select")
        Log.d("AddRecodingAct", "$selection")
        foodDataPushing()
        date()
        var btnSend = findViewById<Button>(R.id.btn_sendData)
        btnSend.setOnClickListener {
            addData()
        }

    }

    private fun foodDataPushing() {
        var et_foodName = findViewById<EditText>(R.id.et_foodName)
        var et_brand = findViewById<EditText>(R.id.et_brand)
        var et_calories = findViewById<EditText>(R.id.et_calories)
        var et_cho = findViewById<EditText>(R.id.et_cho)
        var et_prote = findViewById<EditText>(R.id.et_prote)
        var et_fat = findViewById<EditText>(R.id.et_fat)
        var et_na = findViewById<EditText>(R.id.et_na)
        var et_serving = findViewById<EditText>(R.id.et_Serving)
        var serving = (et_serving.text.toString()).toDouble()
        FirebaseFirestore.getInstance().collection("FoodList").whereEqualTo("foodName", selection)
            .get().addOnSuccessListener {
            it.forEach {
                et_foodName.apply {
                    setText(it.get("foodName")?.toString() ?: "")
                }
                et_brand.apply {
                    setText(it.get("brand")?.toString() ?: "")
                }
                et_calories.apply {
                    setText(it.get("calories")?.toString() ?: "")
                    setText(((et_calories.text.toString()).toDouble() * serving).toString())
                }
                et_cho.apply {
                    setText(it.get("cho")?.toString() ?: "")
                    setText(((et_cho.text.toString()).toDouble() * serving).toString())
                }
                et_fat.apply {
                    setText(it.get("fat")?.toString() ?: "")
                    setText(((et_fat.text.toString()).toDouble() * serving).toString())
                }
                et_na.apply {
                    setText(it.get("na")?.toString() ?: "")
                    setText(((et_na.text.toString()).toDouble() * serving).toString())
                }
                et_prote.apply {
                    setText(it.get("prote")?.toString() ?: "")
                    setText(((et_prote.text.toString()).toDouble() * serving).toString())
                }
            }
        }.addOnFailureListener {
            //Do Nothing
        }
    }

    private fun date() {
        val c = Calendar.getInstance()
        val y = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        dateText = findViewById<EditText>(R.id.et_date)
        var sdf = SimpleDateFormat("yyyy-M-dd")
        var currentDate = sdf.format(Date())
        dateText.setText(currentDate)
        val dpd = DatePickerDialog(
            this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                dateText.setText("" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth)
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
    }

    private  fun addData(){
        var et_foodName = findViewById<EditText>(R.id.et_foodName)
        var et_brand = findViewById<EditText>(R.id.et_brand)
        var et_calories = findViewById<EditText>(R.id.et_calories)
        var et_cho = findViewById<EditText>(R.id.et_cho)
        var et_prote = findViewById<EditText>(R.id.et_prote)
        var et_fat = findViewById<EditText>(R.id.et_fat)
        var et_na = findViewById<EditText>(R.id.et_na)
        var et_serving = findViewById<EditText>(R.id.et_Serving)
        foodDataPushing()
        var date = dateText.text.toString()
        var brand = et_brand.text.toString()
        var foodName = et_foodName.text.toString()
        var calories = et_calories.text.toString()
        var cho = et_cho.text.toString()
        var prote = et_prote.text.toString()
        var fat = et_fat.text.toString()
        var na = et_na.text.toString()
        var serving = et_serving.text.toString()
        val addList = AddFoodList(
            mealSelection,
            date,
            brand,
            foodName,
            calories,
            cho,
            prote,
            fat,
            na,
            serving
        )
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseFirestore.getInstance().collection("RecordOf$uid").document(date)
            .collection(mealSelection).document(foodName).set(
                addList,
                SetOptions.merge())
    }

    private fun sp() {

        var mealOpt = findViewById<Spinner>(R.id.spMeal)
        val optionMeal = arrayListOf("早餐", "午餐", "晚餐", "小食/其他")
        mealOpt.adapter = ArrayAdapter<String>(this, R.layout.spinner_layout, optionMeal)
        mealOpt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Do Nothing
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mealSelection = optionMeal[position]
            }
        }
    }
}