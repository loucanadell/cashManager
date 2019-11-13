package com.msc.cashmanager.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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