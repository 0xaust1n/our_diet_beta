package com.odstudio.ourdiet.ui.friends


import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.odstudio.ourdiet.R



class ApplyAdapter(
    private val context: FragmentActivity?,
    private val ApplyList: ArrayList<String>,
    private val ApplyPosition: ArrayList<Int>
) : BaseAdapter() {


    override fun getCount(): Int {
       return ApplyPosition.size
    }

    // you can also ignore this
    override fun getItemId(position: Int): Long {
        return 0
    }


    override fun getItem(position: Int): Any {
        return  ApplyPosition[position]
    }


    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {1
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.applylist, null)
        var avatar = view.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.avatar)
        var nickName = view.findViewById<TextView>(R.id.nickName)
        var email = view.findViewById<TextView>(R.id.email)
        var acceptBtn = view.findViewById<Button>(R.id.acceptBtn)
        var rejectBtn  = view.findViewById<Button>(R.id.RejectBtn)
        var appleText = view.findViewById<TextView>(R.id.applyText)
        var avatarRef= FirebaseStorage.getInstance().getReference(ApplyList[ApplyPosition[position+0]]).downloadUrl.addOnSuccessListener {
            Glide.with(Activity())
                .asBitmap()
                .load(it)
                .into(avatar)
            nickName.text = ApplyList[ApplyPosition[position+1]]
            email.text= ApplyList[ApplyPosition[position+2]]
            if(ApplyList[ApplyPosition[position+3]] == "申請中")
            {
                acceptBtn.alpha = 0f
                rejectBtn.alpha = 0f
            }
            else{
                appleText.alpha = 0f
            }
        }
        avatarRef.isComplete
        return view
    }
}