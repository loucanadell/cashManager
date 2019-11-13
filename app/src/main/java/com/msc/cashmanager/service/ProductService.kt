package com.msc.cashmanager.service

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.msc.cashmanager.model.Product
import com.msc.cashmanager.model.User
import org.json.JSONObject
import java.io.File

class ProductService {
    var result = ""
    var url :String = "http://3.81.154.236:8080/article/1"

    val cache = DiskBasedCache(File("./"), 1024 * 1024)
    val network = BasicNetwork(HurlStack())
    val requestQueue = RequestQueue(cache, network).apply {
        start()
    }

    fun getRequest(url: String) {
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                result = response
            },
            Response.ErrorListener { Log.d("ERROR", "$it") })
        requestQueue.add(stringRequest)
    }

    fun postRequest(product: Product) {
        val jsonobj = JSONObject()

        jsonobj.put("name", product.name)
        jsonobj.put("price", product.price)

        val stringRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonobj,
            Response.Listener { response ->
                result = response.toString()
            },
            Response.ErrorListener { Log.d("ERROR", "$it") })

        requestQueue.add(stringRequest)
    }
}