package com.odstudio.ourdiet.ui.food

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.odstudio.ourdiet.R

class FoodAdapter(
    private val context: FragmentActivity?,
    private val meal: List<String>,
    private val list4Breakfast: ArrayList<String>,
    private val list4Lunch: ArrayList<String>,
    private val list4Dinner: ArrayList<String>,
    private val list4Other: ArrayList<String>,
    private val food: List<List<String>>

) : BaseExpandableListAdapter() {
    private var i = 0
    private var j = 0
    private var k = 0
    private var l = 0

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
    ): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_food, null)
        var bSum = list4Breakfast.size
        Log.d("Fucking sum 4b", bSum.toString())
        var lSum = list4Lunch.size
        Log.d("Fucking sum 4l", lSum.toString())
        var dSum = list4Dinner.size
        Log.d("Fucking sum 4d", dSum.toString())
        var oSum = list4Other.size
        Log.d("Fucking sum 4o", oSum.toString())
        var foodName = view.findViewById<TextView>(R.id.foodName)
        var brand = view.findViewById<TextView>(R.id.brand)
        var calories = view.findViewById<TextView>(R.id.calories)
        var serving = view.findViewById<TextView>(R.id.serving)

        while (i < bSum) {
            foodName.text = list4Breakfast[i + 0]
            brand.text = list4Breakfast[i + 1]
            calories.text = list4Breakfast[i + 2]
            serving.text = list4Breakfast[i + 3]
            i += 4
            return view
        }

        while (j < lSum) {
            foodName.text = list4Lunch[j + 0]
            brand.text = list4Lunch[j + 1]
            calories.text = list4Lunch[j + 2]
            serving.text = list4Lunch[j + 3]
            j += 4
            return view
        }
        while (k < dSum) {
            foodName.text = list4Dinner[k + 0]
            brand.text = list4Dinner[k + 1]
            calories.text = list4Dinner[k + 2]
            serving.text = list4Dinner[k + 3]
            k += 4
            return view
        }

        while (l < oSum) {
            foodName.text = list4Other[l + 0]
            brand.text = list4Other[l + 1]
            calories.text = list4Other[l + 2]
            serving.text = list4Other[l + 3]
            k += 4
            return view
        }

        return view
    }
}
