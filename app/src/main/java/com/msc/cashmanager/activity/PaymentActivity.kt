package com.msc.cashmanager.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.msc.cashmanager.R
import com.msc.cashmanager.model.AuthSession
import com.msc.cashmanager.model.User
import kotlinx.android.synthetic.main.activity_payment.*

class PaymentActivity :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (!AuthSession.IsLoggedIn) {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        bindingView()
    }

    private fun bindingView() {
        val paypalButton = findViewById<Button>(R.id.paypalButton);
        val nfcButton = findViewById<Button>(R.id.nfcButton);

        logout.setOnClickListener {
            logout()
        }
        paypalButton.setOnClickListener {
            val paypalIntent = Intent(this, PaypalActivity::class.java)
            startActivity(paypalIntent)
        }

        nfcButton.setOnClickListener {
            val nfcIntent = Intent(this, NfcActivity::class.java)
            startActivity(nfcIntent)
        }
    }

    private fun logout() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("You're about to logout")
        builder.setPositiveButton("Confirm") { _, _ ->
            AuthSession.accessToken = ""
            AuthSession.userId = ""
            AuthSession.billAmount = ""
            AuthSession.password = ""
            AuthSession.user = User("", "", "", "", "")
            AuthSession.IsLoggedIn = false
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        val alert: AlertDialog = builder.create()
        alert.show()
    }
}