package com.msc.cashmanager.model

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import com.android.volley.RequestQueue
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.msc.cashmanager.R
import com.msc.cashmanager.activity.CartActivity
import com.msc.cashmanager.service.CartService
import com.msc.cashmanager.service.ProductService
import com.msc.cashmanager.service.UserService
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.list_item.view.*
import org.json.JSONObject


class ProductAdapter(context: Context, products: ArrayList<SelectedProduct>) :
    ArrayAdapter<SelectedProduct>(context, 0, products) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val product = getItem(position)

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }

        val name = convertView!!.findViewById(R.id.productName) as TextView
        val price = convertView!!.findViewById(R.id.productPrice) as TextView



        val delete = convertView!!.findViewById(R.id.delete) as FloatingActionButton
        delete.setOnClickListener {
            val rq = ProductService()
            rq.deleteArticle(product!!.id.toString())
            rq.requestQueue.addRequestFinishedListener(
                RequestQueue.RequestFinishedListener<String>() {


                }
            )
        }

        name.text = product!!.name
        price.text = product!!.price.toString() + " €"

        convertView.setTag(position)

        return convertView
    }
}