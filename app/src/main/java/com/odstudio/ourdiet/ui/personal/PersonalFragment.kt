package com.odstudio.ourdiet.ui.personal

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.odstudio.ourdiet.R

class PersonalFragment : Fragment() {

    private lateinit var personalViewModel: PersonalViewModel
    private lateinit var genderOpt : Spinner
    private lateinit var genderText : String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        personalViewModel = ViewModelProviders.of(this).get(PersonalViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_personal, null)
        //gender spinner
        genderOpt = root.findViewById(R.id.spinnerGender)
        val OptOfGender = arrayListOf("男","女")
        genderOpt.adapter = ArrayAdapter<String>(activity!!.applicationContext,R.layout.spinner_layout,OptOfGender)
        genderOpt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Do nothing
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                genderText = OptOfGender[position]
            }
        }
        //Birthday Edit Text
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val birthdayText = root.findViewById<EditText>(R.id.et_birthday)
        val dpd = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            birthdayText.setText("" + year + "/" +  (monthOfYear+1) + "/" + dayOfMonth  )
        }, year, month, day)
        birthdayText.isClickable = true
        birthdayText.showSoftInputOnFocus = false
        birthdayText.setOnClickListener{
            dpd.show()
        }
        // Height

        //---------------------Return Root--------------------------------
        return root
    }
}