package com.msc.cashmanager.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.msc.cashmanager.R
import com.msc.cashmanager.model.AuthSession
import com.msc.cashmanager.model.Run
import com.msc.cashmanager.model.User
import com.msc.cashmanager.service.PaymentService
import kotlinx.android.synthetic.main.activity_paypal.*
import kotlinx.android.synthetic.main.layout_paypal.*

class PaypalActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (!AuthSession.IsLoggedIn) {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paypal)
        val paymentButton :Button = findViewById(R.id.billButton)
        paymentButton.text = "Payment " + AuthSession.billAmount

        logout.setOnClickListener {
            logout()
        }
        cancelPaypal.setOnClickListener {
            val paymentIntent = Intent(this, PaymentActivity::class.java)
            startActivity(paymentIntent)
        }
        paymentButton.setOnClickListener {
            handlePayment()
        }
    }

    fun handlePayment() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Payment status")
        builder.setMessage("Pending ...")
        Run.after(2000) {
            if (true) {
                //builder.setIcon(R.drawable.ic_validate)
                builder.setMessage("Validate !")
                builder.setPositiveButton("Done") { _, _ ->
                    callToAPI()
                    AuthSession.billAmount = ""
                    val homeIntent = Intent(this, HomeActivity::class.java)
                    startActivity(homeIntent)
                }
                val alert: AlertDialog = builder.create()
                alert.show()
            }
        }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    fun callToAPI() {
        val rq = PaymentService()
        rq.postPayment()
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.visa ->
                if (checked) {
                }
                R.id.mastercard ->
                if (checked) {
                }
            }
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