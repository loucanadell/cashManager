package com.msc.cashmanager.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.auth0.android.jwt.JWT
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.msc.cashmanager.model.Product
import com.msc.cashmanager.R
import com.msc.cashmanager.model.AuthSession
import com.msc.cashmanager.model.SelectedProduct
import com.msc.cashmanager.model.Token
import com.msc.cashmanager.service.ProductService
import com.msc.cashmanager.service.UserService
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONObject

class HomeActivity: AppCompatActivity() {

    var cart = ArrayList<SelectedProduct>()
    var bill: Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        bindingView()
        getCurrentCart()
    }


    override fun onResume() {
        val bottomNavBar = findViewById<BottomNavigationView>(R.id.activity_main_bottom_navigation)

        super.onResume()
        bottomNavBar.menu.getItem(0).isChecked = true;
    }

    private fun getCurrentCart() {
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
                    for (element in cart) {
                        bill += element.price
                    }
                }
                val formattedBill = "%.2f".format(bill)
                AuthSession.billAmount = "$formattedBill €"
                billAmount.text = AuthSession.billAmount;
            }
        )
    }

    private fun bindingView() {
        val cartButton = findViewById<Button>(R.id.cartButton);
        val scanButton = findViewById<Button>(R.id.scanButton);
        val paymentButton = findViewById<Button>(R.id.paymentButton);
        val logoutButton = findViewById<FloatingActionButton>(R.id.logout)
        val bottomNavBar = findViewById<BottomNavigationView>(R.id.activity_main_bottom_navigation)

        bottomNavBar.menu.getItem(0).isChecked = true;
        bottomNavBar.setOnNavigationItemSelectedListener { item -> updateMainFragment(item.itemId) }
        cartButton.setOnClickListener {
            val cartIntent = Intent(this, CartActivity::class.java)
            cartIntent.putExtra("cart", cart)
            startActivity(cartIntent)
        }

        scanButton.setOnClickListener {
            val scanIntent = Intent(this, ScannerActivity::class.java)
            startActivity(scanIntent)
        }

        logoutButton.setOnClickListener {
            logout()
        }

        paymentButton.setOnClickListener {
            if (bill == 0F) {
                Toast.makeText(applicationContext,"Your cart is empty", Toast.LENGTH_SHORT).show()
            } else {
                val paymentIntent = Intent(this, PaymentActivity::class.java)
                startActivity(paymentIntent)
            }
        }
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

    private fun updateMainFragment(integer: Int): Boolean {
        when (integer) {
            R.id.action_scanner -> {
                val scanIntent = Intent(this, ScannerActivity::class.java)
                startActivity(scanIntent)
            }
            R.id.action_cart -> {
                val cartIntent = Intent(this, CartActivity::class.java)
                cartIntent.putExtra("cart", cart)
                startActivity(cartIntent)
            }
        }
        return true
    }
}