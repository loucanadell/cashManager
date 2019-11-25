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
import com.msc.cashmanager.service.PaymentService

class PaypalActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paypal)
        val paymentButton :Button = findViewById(R.id.billButton)
        paymentButton.text = "Payment " + AuthSession.billAmount

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
}