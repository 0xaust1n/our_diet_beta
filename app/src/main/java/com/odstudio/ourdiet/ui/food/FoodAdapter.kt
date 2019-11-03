package com.odstudio.ourdiet.ui.food

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.odstudio.ourdiet.R

class FoodAdapter(
    private val context: FragmentActivity?,
    private val meal: List<String>,
    private val food: List<List<Task<QuerySnapshot>>>
) : BaseExpandableListAdapter() {
    override fun getGroup(groupPosition: Int): Any {
        return meal[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return food[groupPosition][childPosition]
    }

    override fun getGroupCount(): Int {
        return meal.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return food[groupPosition].size
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

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.food_group_item, null)
        val textView = view.findViewById<TextView>(R.id.txtDepartmentName)
        textView.text = meal[groupPosition]

        return view
    }
    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.food_child_item, null)
        val textView = view.findViewById<TextView>(R.id.txtClassName)
        textView.text = food[groupPosition][childPosition].toString()

        return view
    }
}
