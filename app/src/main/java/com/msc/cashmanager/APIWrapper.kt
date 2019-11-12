package com.msc.cashmanager

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import org.json.JSONObject
import java.io.File
import kotlin.coroutines.coroutineContext

class APIWrapper {
    var result = ""

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
}