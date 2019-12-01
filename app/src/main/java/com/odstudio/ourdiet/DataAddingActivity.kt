package com.odstudio.ourdiet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.odstudio.ourdiet.Data_Class.FoodList

class DataAddingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_adding)
        var btnSend = findViewById<Button>(R.id.btn_send)
        btnSend.setOnClickListener{
            var et_foodName = findViewById<EditText>(R.id.et_foodName)
            var et_brand= findViewById<EditText>(R.id.et_brand)
            var et_calories = findViewById<EditText>(R.id.et_calories)
            var et_cho = findViewById<EditText>(R.id.et_cho)
            var et_prote = findViewById<EditText>(R.id.et_prote)
            var et_fat = findViewById<EditText>(R.id.et_fat)
            var et_na = findViewById<EditText>(R.id.et_na)
            var foodName = et_foodName.text.toString()
            var brand = et_brand.text.toString()
            var calories = et_calories.text.toString()
            var cho = et_cho.text.toString()
            var prote = et_prote.text.toString()
            var fat = et_fat.text.toString()
            var na = et_na.text.toString()
            val foodList = FoodList(brand,foodName,calories,cho,prote,fat,na)
            FirebaseFirestore.getInstance().collection("FoodList").document("$brand-$foodName").set(foodList)
            Toast.makeText(this, "已存入資料庫啾咪", Toast.LENGTH_SHORT).show()
        }
    }
}
