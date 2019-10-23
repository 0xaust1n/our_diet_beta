package com.odstudio.ourdiet.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.odstudio.ourdiet.R
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var bmi: Double = 0.0
    private var age: Int = 0
    private var gender: String = ""
    private var height: Number = 0
    private var weight: Number = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        var sdf = SimpleDateFormat("yyyy-M-dd")
        var currentDate = sdf.format(Date())
        var now = currentDate.split("-").toMutableList()
        now[0] = now[0] + "年"
        now[1] = now[1] + "月"
        now[2] = now[2] + "日"
        var date = root.findViewById<TextView>(R.id.todaydate)
        date.text = "今天是" + now[0] + now[1] + now[2]
        Log.d("FUCKING CHECKER", date.toString())

        var userinf = root.findViewById<TextView>(R.id.userbmiage)
        var recommendcalories = root.findViewById<TextView>(R.id.recommendcalories)
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseFirestore.getInstance().collection("Users").whereEqualTo("uid", uid).get()
            .addOnSuccessListener {
                it.forEach {
                    age.apply {
                        age = ((it.get("age").toString()).toInt())
                        Log.d("Fucking age", age.toString())
                        userinf.text = "您的年齡為" + age.toString() + "歲"
                    }
                    bmi.apply {
                        bmi = ((it.get("bmi").toString()).toDouble())
                        Log.d("Fucking bmi", bmi.toString())
                        userinf.text = userinf.text.toString() + "，您的BMI為" + bmi.toString()
                    }
                    height.apply {
                        height = ((it.get("height").toString()).toInt())
                        Log.d("Fucking height", height.toString())
                    }
                    weight.apply {
                        weight = ((it.get("weight").toString()).toInt())
                        Log.d("Fucking weight", weight.toString())
                    }
                    gender.apply {
                        gender = (it.get("gender").toString())
                        Log.d("Fucking gender", gender)
                        if (gender == "男") {
                            recommendcalories.text =
                                "您的每日建議攝取為" + ((13.7 * weight.toDouble()) + (5 * height.toDouble()) - (6.8 * age) + 66) + "大卡"
                        } else {
                            recommendcalories.text =
                                "您的每日建議攝取為" + ((9.6 * weight.toDouble()) + (1.8 * height.toDouble()) - (4.7 * age) + 655) + "大卡"
                        }
                    }
                }
            }
        return root
    }
}
