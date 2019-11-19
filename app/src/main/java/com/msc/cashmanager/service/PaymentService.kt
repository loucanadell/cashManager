package com.msc.cashmanager.service

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.msc.cashmanager.model.AuthSession
import com.msc.cashmanager.model.Product
import org.json.JSONObject
import java.io.File

class PaymentService {
    var result = ""
    var url :String = "http://3.81.154.236:8080/payment/"

    val cache = DiskBasedCache(File("./"), 1024 * 1024)
    val network = BasicNetwork(HurlStack())
    val requestQueue = RequestQueue(cache, network).apply {
        start()
    }

    fun postPayment() {
        val finalUrl :String = url + AuthSession.userId + "/" + AuthSession.idCart
        val stringRequest = object: StringRequest(
            Request.Method.POST, finalUrl,
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