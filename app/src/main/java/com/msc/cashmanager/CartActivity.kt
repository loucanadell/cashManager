package com.msc.cashmanager

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.ListView
import android.widget.ArrayAdapter



class CartActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        populateProductList()
    }

    private fun populateProductList() {
        val cart = intent.getParcelableArrayListExtra<Product>("cart")
        val adapter = ProductAdapter(this, cart)
        val mListView :ListView = findViewById(R.id.list_view)
        mListView.adapter = adapter
    }
}