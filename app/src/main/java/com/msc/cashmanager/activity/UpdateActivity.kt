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
import com.msc.cashmanager.model.AuthSession
import com.msc.cashmanager.model.User
import com.msc.cashmanager.service.UserService
import kotlinx.android.synthetic.main.layout_update.*
import org.json.JSONObject

class UpdateActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!AuthSession.IsLoggedIn) {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        val user = AuthSession.user
        var usernameValue = user.username
        var lastnameValue = user.lastname
        var emailValue = user.email
        var passwordValue = user.password
        var addressValue = user.address

        username.hint = "Firstname : $usernameValue"
        lastname.hint = "Lastname : $lastnameValue"
        email.hint = "Email : $emailValue"
        address.hint = "Address : $addressValue"
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
                }
            }
            if (address.text.toString().trim().isNotBlank())
                addressValue = address.text.toString()
            val userFinal = User(usernameValue, lastnameValue, emailValue, passwordValue, addressValue)
            val rq = UserService()
            rq.updateUser(userFinal)
            rq.requestQueue.addRequestFinishedListener(
                RequestQueue.RequestFinishedListener<JSONObject>() {
                    Toast.makeText(applicationContext,"Account successfully updated", Toast.LENGTH_SHORT).show()
                    val homeIntent = Intent(this, HomeActivity::class.java)
                    startActivity(homeIntent)
                }
            )
        }
    }
}