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
import com.msc.cashmanager.R
import com.msc.cashmanager.model.AuthSession
import com.msc.cashmanager.model.Product
import com.msc.cashmanager.model.Run
import com.msc.cashmanager.service.PaymentService
import com.msc.cashmanager.service.ProductService

class NfcActivity : AppCompatActivity(), NfcAdapter.ReaderCallback {

    private var nfcAdapter: NfcAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)
        val billAmount = findViewById<TextView>(R.id.billAmount)
        val cancel = findViewById<Button>(R.id.cancelNfc)

        billAmount.text = AuthSession.billAmount
        cancel.setOnClickListener {
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
}