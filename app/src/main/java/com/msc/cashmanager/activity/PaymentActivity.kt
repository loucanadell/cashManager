package com.msc.cashmanager.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.msc.cashmanager.R
import com.msc.cashmanager.model.AuthSession

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

        paypalButton.setOnClickListener {
            val paypalIntent = Intent(this, PaypalActivity::class.java)
            startActivity(paypalIntent)
        }

        nfcButton.setOnClickListener {
            val nfcIntent = Intent(this, NfcActivity::class.java)
            startActivity(nfcIntent)
        }
    }
}