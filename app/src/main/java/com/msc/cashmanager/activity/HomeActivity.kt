package com.msc.cashmanager.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.msc.cashmanager.model.Product
import com.msc.cashmanager.R
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity: AppCompatActivity() {

    var cart = ArrayList<Product>()
    var bill: Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        bindingView()

        // MOCKING DATA -> CALL TO API
        cart.add(Product("iphone", 10F))
        cart.add(Product("samsung", 12F))
        cart.add(Product("pizza", 13F))
        cart.add(Product("tv", 10F))
        // END


        for (element in cart) {
            bill += element.price
        }
        billAmount.setText(bill.toString() + " €");
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