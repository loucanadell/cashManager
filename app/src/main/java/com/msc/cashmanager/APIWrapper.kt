package com.msc.cashmanager

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class APIWrapper {
    var result = ""

    fun getRequest(url: String, context: Context) {
        val queue = Volley.newRequestQueue(context)

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                result = response
            },
            Response.ErrorListener { Log.d("ERROR", "$it") })
        queue.add(stringRequest)
    }
}