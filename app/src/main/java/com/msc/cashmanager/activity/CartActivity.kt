package com.msc.cashmanager.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ListView
import com.msc.cashmanager.model.Product
import com.msc.cashmanager.model.ProductAdapter
import com.msc.cashmanager.R
import com.msc.cashmanager.model.SelectedProduct


class CartActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        populateProductList()
    }

    private fun populateProductList() {
        val cart = intent.getParcelableArrayListExtra<SelectedProduct>("cart")
        val adapter = ProductAdapter(this, cart)
        val mListView :ListView = findViewById(R.id.list_view)
        mListView.adapter = adapter
    }
}