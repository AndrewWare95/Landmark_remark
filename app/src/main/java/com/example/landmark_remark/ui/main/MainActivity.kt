package com.example.landmark_remark.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.landmark_remark.R

class MainActivity : AppCompatActivity() {

    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        username = intent.getStringExtra("username") ?: ""

        // Hide default action bar so we can use a custom one in the fragment
        supportActionBar?.hide()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.container, MainFragment.newInstance(username)).commitNow()
        }
    }
}