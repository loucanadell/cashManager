package com.msc.cashmanager.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.ListView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.msc.cashmanager.model.ProductAdapter
import com.msc.cashmanager.R
import com.msc.cashmanager.model.SelectedProduct
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.msc.cashmanager.model.AuthSession
import com.msc.cashmanager.model.User
import kotlinx.android.synthetic.main.layout_cart.*


class CartActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (!AuthSession.IsLoggedIn) {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val paymentButton = findViewById<Button>(R.id.paymentButton)
        val bottomNavBar = findViewById<BottomNavigationView>(R.id.activity_main_bottom_navigation)
        val logoutButton = findViewById<FloatingActionButton>(R.id.logout)

        logoutButton.setOnClickListener {
            logout()
        }
        paymentButton.setOnClickListener {
            val cart = intent.getParcelableArrayListExtra<SelectedProduct>("cart")
            var bill = 0F
            for (element in cart) {
                bill += element.price
            }
            if (bill == 0F) {
                Toast.makeText(applicationContext, "Your cart is empty", Toast.LENGTH_SHORT).show()
            } else {
                val paymentIntent = Intent(this, PaymentActivity::class.java)
                startActivity(paymentIntent)
            }
        }
        bottomNavBar.menu.getItem(2).isChecked = true;
        bottomNavBar.setOnNavigationItemSelectedListener { item -> updateMainFragment(item.itemId) }
        populateProductList()
    }

    override fun onResume() {
        val bottomNavBar = findViewById<BottomNavigationView>(R.id.activity_main_bottom_navigation)
        super.onResume()
        bottomNavBar.menu.getItem(2).isChecked = true;
        populateProductList();
    }

    private fun populateProductList() {
        val cart = intent.getParcelableArrayListExtra<SelectedProduct>("cart")
        val adapter = ProductAdapter(this, cart)
        list_view.adapter = adapter
        list_view.deferNotifyDataSetChanged()
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
            R.id.action_profile -> {
                val profilIntent = Intent(this, ProfileActivity::class.java)
                startActivity(profilIntent)
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