package com.msc.cashmanager.activity

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
import com.msc.cashmanager.model.AuthSession
import kotlinx.android.synthetic.main.layout_cart.*


class CartActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (!AuthSession.IsLoggedIn) {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        val bottomNavBar = findViewById<BottomNavigationView>(R.id.activity_main_bottom_navigation)
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
        }
        return true
    }
}