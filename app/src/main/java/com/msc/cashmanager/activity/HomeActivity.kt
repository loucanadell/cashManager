package com.msc.cashmanager.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.auth0.android.jwt.JWT
import com.google.gson.Gson
import com.msc.cashmanager.model.Product
import com.msc.cashmanager.R
import com.msc.cashmanager.model.AuthSession
import com.msc.cashmanager.model.SelectedProduct
import com.msc.cashmanager.model.Token
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

    fun getCurrentCart() {
        val rq = UserService()
        rq.getCurrentCart()
        rq.requestQueue.addRequestFinishedListener(
            RequestQueue.RequestFinishedListener<JSONObject>() {
                val obj = JSONObject(rq.result)
                val articlesArray = obj.getJSONArray("articles")
                for (i in 0 until articlesArray.length()) {
                    val item = articlesArray.getJSONObject(i)
                    val product = Gson().fromJson(item.toString(), SelectedProduct::class.java)
                    cart.add(SelectedProduct(product.id, product.name.toString(), product.price))
                }
                for (element in cart) {
                    bill += element.price
                }
                billAmount.setText(bill.toString() + " €");
            }
        )
    }

    fun bindingView() {
        val cartButton = findViewById<Button>(R.id.cartButton);
        val scanButton = findViewById<Button>(R.id.scanButton);
        val paymentButton = findViewById<Button>(R.id.paymentButton);

        cartButton.setOnClickListener {
            val cartIntent = Intent(this, CartActivity::class.java)
            cartIntent.putExtra("cart", cart)
            startActivity(cartIntent)
        }

        scanButton.setOnClickListener {
            val scanIntent = Intent(this, ScannerActivity::class.java)
            startActivity(scanIntent)
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
}