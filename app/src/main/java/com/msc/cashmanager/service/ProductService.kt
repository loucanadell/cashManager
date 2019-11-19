package com.msc.cashmanager.service

import android.content.Intent
import android.content.Intent.getIntent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.auth0.android.jwt.JWT
import com.msc.cashmanager.activity.HomeActivity
import com.msc.cashmanager.model.AuthSession
import com.msc.cashmanager.model.Product
import com.msc.cashmanager.model.User
import org.json.JSONObject
import java.io.File

class ProductService: AppCompatActivity() {
    var result = ""
    var url :String = "http://3.81.154.236:8080/article/"

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

    fun addArticleRequest(product: Product) {
        val jsonobj = JSONObject()


        jsonobj.put("name", product.name)
        jsonobj.put("price", product.price)

        val stringRequest = object: JsonObjectRequest(
            Request.Method.POST, url + AuthSession.userId, jsonobj,
            Response.Listener { response ->
                result = response.toString()
            },
            Response.ErrorListener { Log.d("ERROR", "$it") })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer " + AuthSession.accessToken
                return headers
            }
        }
        requestQueue.add(stringRequest)
    }

    fun deleteArticle(id :String) {
        val finalUrl :String = "http://3.81.154.236:8080/cart/" + AuthSession.userId + "/article/" + id
        val stringRequest = object: StringRequest(
            Request.Method.DELETE, finalUrl,
            Response.Listener<String> { response ->
                result = response
            },
            Response.ErrorListener { Log.d("ERROR", "$it") })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer " + AuthSession.accessToken
                return headers
            }
        }
        requestQueue.add(stringRequest)
    }
}