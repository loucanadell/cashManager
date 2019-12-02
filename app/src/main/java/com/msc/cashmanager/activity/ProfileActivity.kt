package com.msc.cashmanager.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.msc.cashmanager.R
import com.msc.cashmanager.model.AuthSession
import com.msc.cashmanager.model.User
import com.msc.cashmanager.service.UserService
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_signup.*
import org.json.JSONObject

class ProfileActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!AuthSession.IsLoggedIn) {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val logoutButton = findViewById<Button>(R.id.logout)
        val updateButton = findViewById<Button>(R.id.updateButton)
        val bottomNavBar = findViewById<BottomNavigationView>(R.id.activity_main_bottom_navigation)

        bottomNavBar.menu.getItem(3).isChecked = true;
        bottomNavBar.setOnNavigationItemSelectedListener { item -> updateMainFragment(item.itemId) }
        getUser()
        logoutButton.setOnClickListener {
            logout()
        }
        updateButton.setOnClickListener {
            val updateIntent = Intent(this, UpdateActivity::class.java)
            startActivity(updateIntent)
        }
    }

    override fun onResume() {
        val bottomNavBar = findViewById<BottomNavigationView>(R.id.activity_main_bottom_navigation)

        super.onResume()
        bottomNavBar.menu.getItem(3).isChecked = true;
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
                    AuthSession.user = User(firstnameValue, lastnameValue, emailValue, AuthSession.password, addressValue)
                    firstName.text = firstnameValue
                    lastName.text = lastnameValue
                    address.text = addressValue
                    email.text = emailValue
                }
            })
    }

    private fun updateMainFragment(integer: Int): Boolean {
        when (integer) {
            R.id.action_home -> {
                val homeIntent = Intent(this, HomeActivity::class.java)
                startActivity(homeIntent)
            }
            R.id.action_scanner -> {
                val scanIntent = Intent(this, ScannerActivity::class.java)
                startActivity(scanIntent)
            }
            R.id.action_cart -> {
                val cartIntent = Intent(this, CartActivity::class.java)
                startActivity(cartIntent)
            }
        }
        return true
    }

    private fun logout() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("You're about to logout")
        builder.setPositiveButton("Confirm") { _, _ ->
            AuthSession.accessToken = ""
            AuthSession.userId = ""
            AuthSession.billAmount = ""
            AuthSession.password = ""
            AuthSession.user = User("", "", "", "", "")
            AuthSession.IsLoggedIn = false
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