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
import com.android.volley.RequestQueue
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.msc.cashmanager.model.AuthSession
import com.msc.cashmanager.model.User
import com.msc.cashmanager.service.UserService
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_cart.*
import kotlinx.android.synthetic.main.layout_navigation.*
import org.json.JSONObject


class CartActivity: AppCompatActivity() {
    var cart = ArrayList<SelectedProduct>()

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!AuthSession.IsLoggedIn) {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val logoutButton = findViewById<FloatingActionButton>(R.id.logout)

        logoutButton.setOnClickListener {
            logout()
        }
        refreshCart.setOnClickListener {
            refreshList()
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
        activity_main_bottom_navigation.menu.getItem(2).isChecked = true;
        activity_main_bottom_navigation.setOnNavigationItemSelectedListener { item -> updateMainFragment(item.itemId) }
        populateProductList()
    }

    override fun onResume() {
        super.onResume()
        activity_main_bottom_navigation.menu.getItem(2).isChecked = true;
    }

    private fun populateProductList() {
        val cart = intent.getParcelableArrayListExtra<SelectedProduct>("cart")
        val adapter = ProductAdapter(this, cart)
        list_view.adapter = adapter
        list_view.deferNotifyDataSetChanged()
    }

    private fun refreshList() {
        val rq = UserService()
        rq.getCurrentCart()
        rq.requestQueue.addRequestFinishedListener(
            RequestQueue.RequestFinishedListener<JSONObject>() {
                if (rq.result != "") {
                    val obj = JSONObject(rq.result)
                    AuthSession.idCart = obj.getString("id")
                    val articlesArray = obj.getJSONArray("articles")
                    for (i in 0 until articlesArray.length()) {
                        val item = articlesArray.getJSONObject(i)
                        val product = Gson().fromJson(item.toString(), SelectedProduct::class.java)
                        cart.add(
                            SelectedProduct(
                                product.id,
                                product.name.toString(),
                                product.price
                            )
                        )
                    }
                    val adapter = ProductAdapter(this, cart)
                    list_view.adapter = adapter
                    list_view.deferNotifyDataSetChanged()
                }
            }
        )
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
                profilIntent.putExtra("cart", cart)
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