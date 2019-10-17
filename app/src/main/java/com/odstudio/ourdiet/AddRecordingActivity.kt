package com.odstudio.ourdiet


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore


class AddRecordingActivity : AppCompatActivity() {
    //Global Declare
    private lateinit var selection: String
    private lateinit var mealSelection: String
    // Staring OnCreate Below
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recording)
        sp() // spinner
        selection = intent.getStringExtra("Select")
        Log.d("AddRecodingAct", "$selection")
        foodDataPushing()
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
        FirebaseFirestore.getInstance().collection("FoodList").whereEqualTo("foodName",selection).get().addOnSuccessListener {
            it.forEach {
                et_foodName.apply{
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