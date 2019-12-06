package com.odstudio.ourdiet.ui.friends

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.odstudio.ourdiet.R

class FriendsAdapter(
    private val context: FragmentActivity?,
    private val group: List<String>,
    private val friendList: List<List<String>>,
    private val child: List<List<Int>>

) : BaseExpandableListAdapter() {


    override fun getGroup(groupPosition: Int): Any {
        return group[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return child[groupPosition][childPosition]
    }

    override fun getGroupCount(): Int {
        return group.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return (friendList[groupPosition].size / 4)
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return (groupPosition * 100 + childPosition).toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val view =
            convertView ?: LayoutInflater.from(context).inflate(R.layout.food_group_item, null)
        val textView = view.findViewById<TextView>(R.id.txtDepartmentName)
        textView.text = group[groupPosition]
        return view
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.applylist, null)
        var email = view.findViewById<TextView>(R.id.eMail)
        var nick = view.findViewById<TextView>(R.id.nickName)
        var status = view.findViewById<TextView>(R.id.status)
        var po = child[groupPosition][childPosition]
        var uid =  friendList[groupPosition][po+0]
        email.text = friendList[groupPosition][po + 1]
        nick.text = friendList[groupPosition][po + 2]
        status.text = friendList[groupPosition][po + 3]
        return view

    }

}

