package com.msc.cashmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log


class HomeActivity: AppCompatActivity() {

    var cart = ArrayList<Product>()
    var bill: Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val cartButton = findViewById<Button>(R.id.cartButton);

        cartButton.setOnClickListener {
            val cartIntent = Intent(this, CartActivity::class.java)
            cartIntent.putExtra("cart", cart)
            startActivity(cartIntent)
        }

        val scanButton = findViewById<Button>(R.id.scanButton);

        scanButton.setOnClickListener {
            val scanIntent = Intent(this, ScannerActivity::class.java)
            startActivity(scanIntent)
        }

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
}