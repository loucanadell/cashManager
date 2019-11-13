package com.msc.cashmanager.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.msc.cashmanager.R
import com.msc.cashmanager.model.User
import com.msc.cashmanager.service.UserService
import org.json.JSONObject

class SignUpActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        bindingView()
    }

    fun bindingView() {
        val signupButton = findViewById<Button>(R.id.signup)
        val username = findViewById<EditText>(R.id.username)
        val lastname = findViewById<EditText>(R.id.lastname)
        val address = findViewById<EditText>(R.id.address)
        val password = findViewById<EditText>(R.id.password)
        val confirmPassword = findViewById<EditText>(R.id.confirmPassword)
        val email = findViewById<EditText>(R.id.email)

        signupButton.setOnClickListener{
            if (lastname.text.toString().trim().isNotBlank() &&
                username.text.toString().trim().isNotBlank() &&
                address.text.toString().trim().isNotBlank() &&
                password.text.toString().trim().isNotBlank() &&
                confirmPassword.text.toString().trim().isNotBlank() &&
                email.text.toString().trim().isNotBlank()) {
                if (password.text.toString() == confirmPassword.text.toString()) {
                    createUser(username.text.toString(), lastname.text.toString(),
                        address.text.toString(), password.text.toString(), email.text.toString())
                } else {
                    Toast.makeText(applicationContext,"Password not matching", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(applicationContext,"Missing fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun createUser(username :String, lastname :String, address :String, password :String, email :String) {
        val user = User(username, lastname, email, password, address)
        val rq = UserService()
        rq.signupRequest(user)
        rq.requestQueue.addRequestFinishedListener(
            RequestQueue.RequestFinishedListener<JSONObject>() {
                val homeIntent = Intent(this, HomeActivity::class.java)
                homeIntent.putExtra("user", user)
                startActivity(homeIntent)
            }
        )
    }
}