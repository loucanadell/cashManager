package com.msc.cashmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val registerButton = findViewById<Button>(R.id.register);
        val ipAddress = findViewById<EditText>(R.id.ipAddress);
        val password = findViewById<EditText>(R.id.password);
        val email = findViewById<EditText>(R.id.email);

        registerButton.setOnClickListener{
            if (ipAddress.text.toString().trim().isNotBlank() &&
                password.text.toString().trim().isNotBlank() &&
                email.text.toString().trim().isNotBlank()) {
                val homeIntent = Intent(this, HomeActivity::class.java)
                startActivity(homeIntent)
            }
        }
    }
}