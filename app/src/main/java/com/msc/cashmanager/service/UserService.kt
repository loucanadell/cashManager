package com.msc.cashmanager.service

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.msc.cashmanager.model.User
import org.json.JSONObject
import java.io.File

class UserService {
    var result = ""
    var url :String = "http://3.81.154.236:8080/user"

    val cache = DiskBasedCache(File("./"), 1024 * 1024)
    val network = BasicNetwork(HurlStack())
    val requestQueue = RequestQueue(cache, network).apply {
        start()
    }

    fun postRequest(user: User) {
        val jsonobj = JSONObject()

        jsonobj.put("username", user.username)
        jsonobj.put("lastname", user.lastname)
        jsonobj.put("address", user.address)
        jsonobj.put("email", user.email)
        jsonobj.put("password", user.password)

        val stringRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonobj,
            Response.Listener { response ->
                result = response.toString()
            },
            Response.ErrorListener { Log.d("ERROR", "$it") })

        requestQueue.add(stringRequest)
    }
}