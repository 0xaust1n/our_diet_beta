package com.odstudio.ourdiet


import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView


data class FriendRecordingAdapter(
    private val context: Activity,
    private val meal: List<String>,
    private val food: List<List<String>>,
    private val child: List<List<Int>>

) : BaseExpandableListAdapter() {

    override fun getGroup(groupPosition: Int): Any {
        return meal[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return child[groupPosition][childPosition]
    }

    override fun getGroupCount(): Int {
        return meal.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return (food[groupPosition].size / 4)
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
        textView.text = meal[groupPosition]
        return view
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View? {

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_food_two, null)
        var foodName = view.findViewById<TextView>(R.id.foodName)
        var brand = view.findViewById<TextView>(R.id.brand)
        var calories = view.findViewById<TextView>(R.id.calories)
        var serving = view.findViewById<TextView>(R.id.serving)
        var po = child[groupPosition][childPosition]
        foodName.text = food[groupPosition][po+ 0]
        brand.text = food[groupPosition][po + 1]
        calories.text = food[groupPosition][po + 2]
        serving.text = food[groupPosition][po + 3]
        return view

    }
}
