package com.msc.cashmanager.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.auth0.android.jwt.JWT
import com.google.gson.Gson
import com.msc.cashmanager.R
import com.msc.cashmanager.model.AuthSession
import com.msc.cashmanager.model.Product
import com.msc.cashmanager.model.Token
import com.msc.cashmanager.service.UserService
import kotlinx.android.synthetic.main.layout_settings.*
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        bindingView()
    }

    private fun bindingView() {
        register.setOnClickListener{
            if (password.text.toString().trim().isNotBlank() &&
                email.text.toString().trim().isNotBlank()) {
                login(email.text.toString(), password.text.toString())
            }
            else {
                Toast.makeText(applicationContext,"Missing fields", Toast.LENGTH_SHORT).show()
            }
        }

        signup.setOnClickListener{
            val signupIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signupIntent)
        }
    }

    fun login(email: String, password: String) {
        val rq = UserService()
        rq.loginRequest(email, password)
        rq.requestQueue.addRequestFinishedListener(
            RequestQueue.RequestFinishedListener<JSONObject>() {
                if (rq.result != "") {
                    val token = Gson().fromJson(rq.result, Token::class.java)
                    val jwt = JWT(token.token)
                    AuthSession.userId = jwt.subject
                    AuthSession.accessToken = token.token
                    AuthSession.password = password
                    AuthSession.IsLoggedIn = true;
                    val homeIntent = Intent(this, HomeActivity::class.java)
                    startActivity(homeIntent)
                } else {
                    Toast.makeText(applicationContext,"Could not connect", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}