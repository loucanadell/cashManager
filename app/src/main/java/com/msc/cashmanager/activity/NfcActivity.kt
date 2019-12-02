package com.msc.cashmanager.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.msc.cashmanager.R
import com.msc.cashmanager.model.AuthSession
import com.msc.cashmanager.model.Product
import com.msc.cashmanager.model.Run
import com.msc.cashmanager.model.User
import com.msc.cashmanager.service.PaymentService
import com.msc.cashmanager.service.ProductService
import kotlinx.android.synthetic.main.activity_nfc.*
import kotlinx.android.synthetic.main.layout_nfc.*

class NfcActivity : AppCompatActivity(), NfcAdapter.ReaderCallback {

    private var nfcAdapter: NfcAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        if (!AuthSession.IsLoggedIn) {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)
        val logoutButton = findViewById<FloatingActionButton>(R.id.logout)

        logoutButton.setOnClickListener {
            logout()
        }
        billAmount.text = AuthSession.billAmount
        cancelNfc.setOnClickListener {
            val paymentIntent = Intent(this, PaymentActivity::class.java)
            startActivity(paymentIntent)
        }
    }
    public override fun onResume() {
        super.onResume()
        nfcAdapter?.enableReaderMode(this, this,
            NfcAdapter.FLAG_READER_NFC_A or
                    NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
            null)
    }

    public override fun onPause() {
        super.onPause()
        nfcAdapter?.disableReaderMode(this)
    }
    override fun onTagDiscovered(tag: Tag?) {
        runOnUiThread {
            val vibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(1000)
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
    }

    private fun callToAPI() {
        val rq = PaymentService()
        rq.postPayment()
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