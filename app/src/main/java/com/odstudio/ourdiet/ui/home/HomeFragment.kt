package com.odstudio.ourdiet.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.odstudio.ourdiet.LoginActivity
import com.odstudio.ourdiet.R
import com.odstudio.ourdiet.RecordingActivity
import java.math.RoundingMode
import java.text.DecimalFormat
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

        var userInfo = root.findViewById<TextView>(R.id.userbmiage)
        var recommend = root.findViewById<TextView>(R.id.recommendcalories)
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if( uid == null)
        {
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        FirebaseFirestore.getInstance().collection("Users").whereEqualTo("uid", uid).get()
            .addOnSuccessListener {
                it.forEach {
                    age.apply {
                        age = ((it.get("age").toString()).toInt())
                        userInfo.text = "您的年齡為" + age.toString() + "歲"
                    }
                    bmi.apply {
                        bmi = ((it.get("bmi").toString()).toDouble())
                        userInfo.text = userInfo.text.toString() + "，您的BMI為" + bmi.toString()
                    }
                    height.apply {
                        height = ((it.get("height").toString()).toInt())
                    }
                    weight.apply {
                        weight = ((it.get("weight").toString()).toInt())
                    }
                    gender.apply {
                        gender = (it.get("gender").toString())
                        if (gender == "男") {
                            var df = DecimalFormat("0")
                            df.roundingMode = RoundingMode.HALF_UP
                            recommend.text =
                                "您的每日建議攝取為" + df.format((13.7 * weight.toDouble()) + (5 * height.toDouble()) - (6.8 * age) + 66) + "大卡"
                        } else {
                            var df = DecimalFormat("0")
                            df.roundingMode = RoundingMode.HALF_UP
                            recommend.text =
                                "您的每日建議攝取為" + df.format((9.6 * weight.toDouble()) + (1.8 * height.toDouble()) - (4.7 * age) + 655) + "大卡"
                        }
                    }
                }
            }
        return root
    }
}
