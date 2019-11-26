package com.msc.cashmanager.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.msc.cashmanager.R
import com.msc.cashmanager.model.AuthSession
import com.msc.cashmanager.model.User
import com.msc.cashmanager.service.UserService
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_signup.*
import org.json.JSONObject

class ProfileActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val logoutButton = findViewById<Button>(R.id.logout)
        val updateButton = findViewById<Button>(R.id.updateButton)

        getUser()
        logoutButton.setOnClickListener {
            logout()
        }
        updateButton.setOnClickListener {
            val updateIntent = Intent(this, UpdateActivity::class.java)
            startActivity(updateIntent)
        }
    }

    private fun getUser() {
        val firstName = findViewById<TextView>(R.id.usernameValue)
        val lastName = findViewById<TextView>(R.id.lastnameValue)
        val address = findViewById<TextView>(R.id.addressValue)
        val email = findViewById<TextView>(R.id.emailValue)

        val rq = UserService()
        rq.getUser()
        rq.requestQueue.addRequestFinishedListener(
            RequestQueue.RequestFinishedListener<JSONObject>() {
                if (rq.result != "") {
                    val obj = JSONObject(rq.result)
                    val firstnameValue = obj.getString("username")
                    val lastnameValue = obj.getString("lastname")
                    val addressValue = obj.getString("address")
                    val emailValue = obj.getString("email")
                    val passValue = obj.getString("password")
                    AuthSession.user = User(firstnameValue, lastnameValue, emailValue, passValue, addressValue)
                    firstName.text = firstnameValue
                    lastName.text = lastnameValue
                    address.text = addressValue
                    email.text = emailValue
                }
            })
    }

    private fun logout() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("You're about to logout")
        builder.setPositiveButton("Confirm") { _, _ ->
            AuthSession.accessToken = ""
            AuthSession.userId = ""
            AuthSession.billAmount = ""
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        val alert: AlertDialog = builder.create()
        alert.show()
    }
}