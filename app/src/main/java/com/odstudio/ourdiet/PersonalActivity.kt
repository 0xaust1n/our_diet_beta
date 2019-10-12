package com.odstudio.ourdiet

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.odstudio.ourdiet.Data_Class.User2
import kotlinx.android.synthetic.main.activity_personal.uploadButton
import java.util.*
import com.bumptech.glide.Glide
import  com.bumptech.glide.request.RequestListener
import android.graphics.Bitmap
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.Target

class PersonalActivity : AppCompatActivity() {
    // Global Declare
    private lateinit var genderOpt: Spinner
    private lateinit var genderText: String
    private val tag = "RegisterActivity"
    private var  uploaded = false

    // Below Starting OnCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal)
        //Global
        var et_birthday = findViewById<EditText>(R.id.et_birthday)
        var et_weight = findViewById<EditText>(R.id.et_weight)
        var et_height = findViewById<EditText>(R.id.et_height)
        var et_nick = findViewById<EditText>(R.id.et_nickname)
        var nick = et_nick?.text.toString()
        var birthday = et_birthday?.text.toString()
        var weight = et_weight?.text.toString()
        var height = et_height?.text.toString()

        //Gender Spinner Below
        genderOpt = findViewById(R.id.spinnerGender)
        val optionGender = arrayListOf("男", "女")
        genderOpt.adapter = ArrayAdapter<String>(this, R.layout.spinner_layout, optionGender)
        genderOpt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Do nothing
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                genderText = optionGender[position]
            }
        }
        //Birthday Edit Text
        val c = Calendar.getInstance()
        val y = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val birthdayText = findViewById<EditText>(R.id.et_birthday)
        val dpd = DatePickerDialog(
            this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                birthdayText.setText("" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth)
            },
            y,
            month,
            day
        )
        birthdayText.isClickable = true
        birthdayText.showSoftInputOnFocus = false
        birthdayText.setOnClickListener {
            dpd.show()
        }

        // upload Photo
        var upload = findViewById<Button>(R.id.uploadButton)
        upload.setOnClickListener {
            Log.d(tag, "Try to Get Back Your Fucking Photo")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
        // Send Button
        var send = findViewById<Button>(R.id.btnPersonSend)
        send.setOnClickListener {
            uploadImageToFirebaseStorage()
            saveUserToFirebaseDatabase()
            backtohome()
            Toast.makeText(this, "已更新用戶資料", Toast.LENGTH_SHORT).show()
        }

    }
    override fun onResume() {
        super.onResume()
        getUserFromFirebaseDatabase()
    }

    private var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // proceed and check what the selected image was....
            Log.d(tag, "Photo was selected")
            uploaded = true
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            var uploadView =
                findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.uploadView)
            uploadView.setImageBitmap(bitmap)
            uploadButton.alpha = 0f
        }
    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return

        val filename = FirebaseAuth.getInstance().currentUser!!.uid + "_avatar"
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d(tag, "Successfully uploaded image")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d(tag, "File Location: $it")
                }
            }
            .addOnFailureListener {
                Log.d(tag, "Failed to upload image to storage")
            }
    }

    private fun saveUserToFirebaseDatabase() {
        var et_birthday = findViewById<EditText>(R.id.et_birthday)
        var et_weight = findViewById<EditText>(R.id.et_weight)
        var et_height = findViewById<EditText>(R.id.et_height)
        var et_nick = findViewById<EditText>(R.id.et_nickname)
        //Using DataClass push to Firebase Database
        val avatar = "/images/"+FirebaseAuth.getInstance().currentUser!!.uid + "_avatar"
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val nick = et_nick!!.text.toString()
        val birthday = et_birthday!!.text.toString()
        val weight:Int = Integer.parseInt(et_weight!!.text.toString())
        val height =Integer.parseInt( et_height!!.text.toString())
        val bmi : Double  = (weight / (height*height/10000)).toDouble()
        val account =User2(avatar,nick,birthday,height,weight,bmi,genderText)
        FirebaseFirestore.getInstance().collection("Users").document(uid).set(account, SetOptions.merge())
    }

    private fun backtohome(){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun getUserFromFirebaseDatabase() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        var et_birthday = findViewById<EditText>(R.id.et_birthday)
        var et_weight = findViewById<EditText>(R.id.et_weight)
        var et_height = findViewById<EditText>(R.id.et_height)
        var et_nick = findViewById<EditText>(R.id.et_nickname)

        var avatarView =
            findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.uploadView)
        val filename = FirebaseAuth.getInstance().currentUser!!.uid + "_avatar"
        var avatarRef= FirebaseStorage.getInstance().getReference("/images/$filename").downloadUrl.addOnSuccessListener {

            Glide.with(applicationContext)
                .asBitmap()
                .load(it)
                .listener(object : RequestListener<Bitmap> {
                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        avatarView.setImageBitmap(resource)
                        return false;
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Toast.makeText(applicationContext, "取得頭像圖檔發生錯誤:"+e?.message, Toast.LENGTH_SHORT).show()

                        return false;
                    }
                }
                ).submit()

        }

        FirebaseFirestore.getInstance().collection("Users").whereEqualTo("uid",uid).get().addOnSuccessListener {
            it.forEach {
                et_nick.apply{
                    setText(it.get("nick").toString())
                }
                et_birthday.apply {
                    setText(it.get("birthday").toString())
                }
                et_weight.apply {
                    setText(it.get("weight").toString())
                }
                et_height.apply {
                    setText(it.get("height").toString())
                }
                val filename = FirebaseAuth.getInstance().currentUser!!.uid + "_avatar"
                FirebaseStorage.getInstance().getReference("/images/$filename").downloadUrl.addOnSuccessListener { itUri ->
                    Toast.makeText(this, "頭像圖檔位置"+itUri.path, Toast.LENGTH_LONG).show()

                }.addOnFailureListener {  exception ->
                    Toast.makeText(this, "取得頭像圖檔發生錯誤:"+exception.message, Toast.LENGTH_SHORT).show()

               }

            }
        }


        //Using DataClass push to Firebase Database
        /*
        val avatar = "/images/"+FirebaseAuth.getInstance().currentUser!!.uid + "_avatar"

        val nick = et_nick!!.text.toString()
        val birthday = et_birthday!!.text.toString()
        val weight:Int = Integer.parseInt(et_weight!!.text.toString())
        val height =Integer.parseInt( et_height!!.text.toString())
        val bmi : Double  = (weight / (height*height/10000)).toDouble()
        val account =User2(avatar,nick,birthday,height,weight,bmi,genderText)*/
    }
}
