package com.msc.cashmanager.model

import android.content.Context
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.msc.cashmanager.R
import com.msc.cashmanager.service.ProductService

@Suppress("NAME_SHADOWING")
class ProductAdapter(context: Context, products: ArrayList<SelectedProduct>) :
    ArrayAdapter<SelectedProduct>(context, 0, products) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val product = getItem(position)

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }

        val name = convertView!!.findViewById(R.id.productName) as TextView
        val price = convertView.findViewById(R.id.productPrice) as TextView



        val delete = convertView.findViewById(R.id.delete) as FloatingActionButton
        delete.setOnClickListener {
            val rq = ProductService()
            rq.deleteArticle(product!!.id.toString())
            convertView.isVisible = false
        }

        name.text = product!!.name
        val priceText = product.price.toString() + " €"
        price.text = priceText

        convertView.tag = position

        return convertView
    }
}