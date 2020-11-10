package com.example.madtap

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main_menu.*

class MainMenu : AppCompatActivity() {

    private val TAG = "MainMenu"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val categories = resources.getStringArray(R.array.category_array)
        val spinner = findViewById<Spinner>(R.id.spinner_category)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        spinner.adapter = adapter

        

        b_start.setOnClickListener{
            Log.d(TAG, "Start Game Button Clicked")
            val intent = Intent(this, MainActivity::class.java)
            val extras = Bundle()
            extras.putBoolean("TRADITIONAL", s_traditional.isChecked)
            extras.putBoolean("ZHUYIN", s_zhuyin.isChecked)
            extras.putBoolean("PINYIN", s_pinyin.isChecked)
            extras.putString("CATEGORY", spinner_category.selectedItem.toString())
            intent.putExtras(extras)
            startActivity(intent)
        }
    }
}