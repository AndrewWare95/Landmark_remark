package com.example.landmark_remark

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.landmark_remark.databinding.LoginActivityBinding
import com.example.landmark_remark.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val enterUsername = binding.enterUsername
        val enterButton = binding.enterButton

        enterButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("username", enterUsername.text.toString())
            startActivity(intent)
        }
    }
}