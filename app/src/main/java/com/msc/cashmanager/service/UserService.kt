package com.msc.cashmanager.service

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.google.gson.stream.JsonToken
import com.msc.cashmanager.model.AuthSession
import com.msc.cashmanager.model.Product
import com.msc.cashmanager.model.User
import org.json.JSONObject
import java.io.File

class UserService {
    var result = ""
    var url :String = "http://3.81.154.236:8080/"

    val cache = DiskBasedCache(File("./"), 1024 * 1024)
    val network = BasicNetwork(HurlStack())
    val requestQueue = RequestQueue(cache, network).apply {
        start()
    }

    fun getCurrentCart() {
        val finalUrl :String = url + "user/opencart/" + AuthSession.userId
        val stringRequest = object: StringRequest(
            Request.Method.GET, finalUrl,
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

    fun getUser() {
        val finalUrl :String = url + "user/" + AuthSession.userId
        val stringRequest = object: StringRequest(
            Request.Method.GET, finalUrl,
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

    fun updateUser(user: User) {
        val jsonobj = JSONObject()

        jsonobj.put("username", user.username)
        jsonobj.put("lastname", user.lastname)
        jsonobj.put("address", user.address)
        jsonobj.put("email", user.email)
        jsonobj.put("password", user.password)

        val stringRequest = object: JsonObjectRequest(
            Request.Method.POST, url + "update/user/" + AuthSession.userId, jsonobj,
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

    fun signupRequest(user: User) {
        val jsonobj = JSONObject()

        jsonobj.put("username", user.username)
        jsonobj.put("lastname", user.lastname)
        jsonobj.put("email", user.email)
        jsonobj.put("password", user.password)
        jsonobj.put("address", user.address)

        val stringRequest = JsonObjectRequest(
            Request.Method.POST, url + "signup", jsonobj,
            Response.Listener { response ->
                result = response.toString()
            },
            Response.ErrorListener { Log.d("ERROR", "$it") })

        requestQueue.add(stringRequest)
    }

    fun loginRequest(email: String, password: String) {
        val jsonobj = JSONObject()

        jsonobj.put("email", email)
        jsonobj.put("password", password)

        val stringRequest = JsonObjectRequest(
            Request.Method.POST, url + "login", jsonobj,
            Response.Listener { response ->
                result = response.toString()
            },
            Response.ErrorListener { Log.d("ERROR", "$it") })

        requestQueue.add(stringRequest)
    }
}