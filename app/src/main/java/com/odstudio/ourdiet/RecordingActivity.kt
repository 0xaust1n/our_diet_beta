package com.odstudio.ourdiet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import com.mancj.materialsearchbar.MaterialSearchBar

class RecordingActivity : AppCompatActivity() {
    private var food = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recording)
        databasePush()
        var searchBar = findViewById<MaterialSearchBar>(R.id.searchView)
        var lv = findViewById<ListView>(R.id.listView)
        searchBar.setHint("Search..")
        searchBar.setSpeechMode(true)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, food)
        lv.adapter = adapter

        searchBar.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                //SEARCH FILTER
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })

        lv.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                val intent = Intent(this@RecordingActivity, AddRecordingActivity::class.java)
                intent.putExtra("Select", adapter.getItem(i)!!.toString())
                startActivity(intent)
            }
        }

        //End OF THE CODE

    }

    private fun databasePush() {
        var tag = "Database Ref"
        var db = FirebaseFirestore.getInstance()
        var docRef = db.collection("FoodList").whereEqualTo("brand", "養樂多").get()
            .addOnSuccessListener { reslut ->
                for (document in reslut) {
                    food.add(document.get("foodName").toString())
                }
            }.addOnFailureListener { exception ->
                Log.d(tag, "Error getting documents: ", exception)
            }
        var docRef2 = db.collection("FoodList").whereEqualTo("brand", "麥當勞").get()
            .addOnSuccessListener { reslut ->
                for (document in reslut) {
                    food.add(document.get("foodName").toString())
                }
            }.addOnFailureListener { exception ->
                Log.d(tag, "Error getting documents: ", exception)

            }
        docRef.isComplete
        docRef2.isComplete
    }
}
