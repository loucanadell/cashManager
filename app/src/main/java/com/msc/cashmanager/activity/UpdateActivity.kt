package com.msc.cashmanager.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.msc.cashmanager.R
import com.msc.cashmanager.model.User
import com.msc.cashmanager.service.UserService
import org.json.JSONObject

class UpdateActivity: AppCompatActivity() {
    var usernameValue = ""
    var lastnameValue = ""
    var emailValue = ""
    var passwordValue = ""
    var addressValue = ""
    var check = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        val updateButton = findViewById<Button>(R.id.updateButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        val username = findViewById<EditText>(R.id.username)
        val lastname = findViewById<EditText>(R.id.lastname)
        val address = findViewById<EditText>(R.id.address)
        val password = findViewById<EditText>(R.id.password)
        val confirmPassword = findViewById<EditText>(R.id.confirmPassword)
        val email = findViewById<EditText>(R.id.email)

        cancelButton.setOnClickListener {
            val homeIntent = Intent(this, HomeActivity::class.java)
            startActivity(homeIntent)
        }

        updateButton.setOnClickListener {
            if (username.text.toString().trim().isNotBlank())
                usernameValue = username.text.toString()
            if (lastname.text.toString().trim().isNotBlank())
                lastnameValue = lastname.text.toString()
            if (email.text.toString().trim().isNotBlank())
                emailValue = email.text.toString()
            if (password.text.toString().trim().isNotBlank() && confirmPassword.text.toString().trim().isNotBlank()) {
                if (password.text.toString() == confirmPassword.text.toString()) {
                    passwordValue = password.text.toString()
                    check = true;
                }
            }
            if (address.text.toString().trim().isNotBlank())
                addressValue = address.text.toString()
            val user = User(usernameValue, lastnameValue, emailValue, passwordValue, addressValue)
            val rq = UserService()
            rq.updateUser(user)
            rq.requestQueue.addRequestFinishedListener(
                RequestQueue.RequestFinishedListener<JSONObject>() {
                    if (rq.result != "") {
                        if (check === true) {
                            Toast.makeText(applicationContext,"Please login with your new credentials", Toast.LENGTH_LONG).show()
                            val loginIntent = Intent(this, LoginActivity::class.java)
                            startActivity(loginIntent)
                        } else {
                            Toast.makeText(applicationContext,"Account successfully updated", Toast.LENGTH_SHORT).show()
                            val homeIntent = Intent(this, HomeActivity::class.java)
                            startActivity(homeIntent)
                        }
                    }
                }
            )
        }
    }
}