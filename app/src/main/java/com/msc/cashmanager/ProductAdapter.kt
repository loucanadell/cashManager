package com.msc.cashmanager

import android.content.Context
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter


class ProductAdapter(context: Context, products: ArrayList<Product>) :
    ArrayAdapter<Product>(context, 0, products) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val product = getItem(position)

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }

        val name = convertView!!.findViewById(R.id.productName) as TextView
        val price = convertView!!.findViewById(R.id.productPrice) as TextView

        name.setText(product!!.name)
        price.setText(product!!.price.toString() + " €")

        return convertView
    }
}