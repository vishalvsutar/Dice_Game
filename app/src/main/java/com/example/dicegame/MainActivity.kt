package com.example.dicegame

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.INFO
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.addTextChangedListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.logging.Level.INFO


class MainActivity : AppCompatActivity() {
    private val basicSideslist = arrayListOf(4, 6, 8, 10, 12, 20)
    lateinit var dice1: Die
    lateinit var dice2: Die
    lateinit var roll1EditText: TextView
    lateinit var roll2EditText: TextView
    lateinit var roll1Button: Button
    lateinit var roll2Button: Button
    lateinit var clearButton: Button
    private lateinit var enterNoOfSidesEditText: EditText
    lateinit var addSidesButton: Button
    lateinit var spinner: Spinner
    var sides: Int = 0
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var historytextview: TextView
    private var mIsSaveSwitchON = false
    private var mIsNightSwitchON = false
    private val arrayList = ArrayList<Int>()


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getViews()

        sharedPreferences = getSharedPreferences(Constants.SHOULD_SAVE_SCORE, MODE_PRIVATE)

        setSpinnerAdapter()
        setSpinnerListener()

        setSideEditTextChangeListener()
        setAddButtonListener()
        addSidesButton.isEnabled = false

        setRoll1ButtonListener()
        setRoll2ButtonListener()
        setClearButtonListener()
    }

    private fun setClearButtonListener() {

        clearButton.setOnClickListener {
            historytextview.text = ""
            clearPutInSharedPref()

        }
    }

    private fun setRoll1ButtonListener() {
        roll1Button.setOnClickListener {
            onRoll1Button()
        }
    }

    private fun onRoll1Button() {
        sides = spinner.selectedItem.toString().toInt()
        dice1 = Die(sides)
        roll1EditText.text = dice1.getCurrentSideUp().toString()
        roll2EditText.visibility = View.GONE
        addRollVal(roll1EditText.text.toString().toInt())
        println(arrayList.joinToString(" ,"))
        Log.i("vvs", arrayList.joinToString(" ,"))
        historytextview.text = arrayList.joinToString(", ")
    }

    private fun setRoll2ButtonListener() {
        roll2Button.setOnClickListener {
            sides = spinner.selectedItem.toString()
                .toInt() //showDiceSidesTextView.text.toString().toInt()
            dice2 = Die(sides)
            roll2EditText.text = dice2.getCurrentSideUp().toString()
            addRollVal(roll2EditText.text.toString().toInt())
            historytextview.text = arrayList.joinToString(", ")
//            println(arrayList.joinToString(" ,"))
            Log.i("vvs", "data: " + arrayList.joinToString(" ,"))
            onRoll1Button()
            roll2EditText.visibility = View.VISIBLE
        }
    }

    private fun setAddButtonListener() {
        addSidesButton.setOnClickListener() { //adding number of sides of dice
            sides = enterNoOfSidesEditText.text.toString().toInt() //get val from entered text

            if (basicSideslist.contains(sides)) {
                Toast.makeText(
                    applicationContext,
                    "This value is already added in list",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                basicSideslist.add(sides)
                spinner.setSelection(basicSideslist.size - 1)
                enterNoOfSidesEditText.setText("")
            }
        }
    }

    private fun setSideEditTextChangeListener() {
        try {
            enterNoOfSidesEditText.addTextChangedListener {
                val text = enterNoOfSidesEditText.text.toString()
                addSidesButton.isEnabled = !(enterNoOfSidesEditText.text.toString() == ""
                        || enterNoOfSidesEditText.text.toString().toInt() == 0
                        || basicSideslist.contains(enterNoOfSidesEditText.text.toString().toInt()))
            }
        } catch (e: java.lang.NumberFormatException) {
            Toast.makeText(baseContext, "Something went wrong", Toast.LENGTH_LONG).show()
            enterNoOfSidesEditText.setText("")
        }
    }

    private fun setSpinnerListener() {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                sides = p0?.getItemAtPosition(p2).toString()
                    .toInt(); //storing selected value in spinner to a variable
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                spinner.setSelection(0); //by default selects 1 to add or subtract score
            }
        }
    }

    private fun setSpinnerAdapter() {
        val scoreAdapter = ArrayAdapter<Int>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            basicSideslist
        ) //adding array of values to spinner
        spinner.adapter = scoreAdapter
    }

    private fun getViews() {
        addSidesButton = findViewById(R.id.addSidesButton)
        enterNoOfSidesEditText = findViewById(R.id.enterNoOfSidesEditText)
        roll1Button = findViewById(R.id.roll1Button)
        roll1EditText = findViewById(R.id.roll1EditText)
        roll2Button = findViewById(R.id.roll2Button)
        roll2EditText = findViewById(R.id.roll2EditText)
        spinner = findViewById(R.id.spinner)
        historytextview = findViewById(R.id.historyTextView)
        clearButton = findViewById(R.id.clearTextButton)
    }

    private fun putInSharedPref() {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json: String? = gson.toJson(arrayList)
        editor.putString(Constants.SCORE_KEY, json)
        editor.apply()
    }

    private fun clearPutInSharedPref() {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json: String? = gson.toJson(arrayList.clear())
        editor.putString(Constants.SCORE_KEY,json)
        editor.apply()
    }

    fun getFromSharedPref(key: String?): ArrayList<Int>? {
        val gson = Gson()
        val json: String? = sharedPreferences.getString(key, null)
        val type: Type = object : TypeToken<java.util.ArrayList<Int?>?>() {}.getType()
        return gson.fromJson(json, type)
    }

    override fun onResume() {
        super.onResume()
        mIsSaveSwitchON = sharedPreferences.getBoolean(Constants.SWITCH_KEY, false)
        mIsNightSwitchON = sharedPreferences.getBoolean(Constants.NIGHT_KEY, false)
        if (mIsNightSwitchON) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        arrayList.clear()
        if (mIsSaveSwitchON) {
            val savedList: ArrayList<Int>? = getFromSharedPref(Constants.SCORE_KEY)
            savedList?.let {
                arrayList.addAll(it)
            }
            historytextview.text = savedList?.joinToString(", ")
            Log.i("vvs", "History in resume: " + historytextview.text.toString())
        }
    }

    override fun onPause() {
        super.onPause()
        mIsSaveSwitchON = sharedPreferences.getBoolean(Constants.SWITCH_KEY, false)
        if (mIsSaveSwitchON) {
            putInSharedPref()
        }

    }

    fun addRollVal(rollVal: Int) {
        if (mIsSaveSwitchON) {
            arrayList.add(rollVal)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> {
                startActivity(Intent(this, Settings_Activity::class.java))
            }
            R.id.menu_about -> {
                Toast.makeText(
                    applicationContext,
                    "this is a final exam app by vishal",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return true
    }
}
