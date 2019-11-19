package com.msc.cashmanager.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.ListView
import com.android.volley.RequestQueue
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.msc.cashmanager.model.Product
import com.msc.cashmanager.model.ProductAdapter
import com.msc.cashmanager.R
import com.msc.cashmanager.model.SelectedProduct
import com.msc.cashmanager.service.UserService
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONObject


class CartActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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

    fun populateProductList() {
        val cart = intent.getParcelableArrayListExtra<SelectedProduct>("cart")
        val adapter = ProductAdapter(this, cart)
        val mListView :ListView = findViewById(R.id.list_view)
        mListView.adapter = adapter
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