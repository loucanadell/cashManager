package com.msc.cashmanager.activity

import android.content.Intent
import android.content.Intent.getIntent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.msc.cashmanager.R
import com.msc.cashmanager.tools.Utils
import com.pro100svitlo.creditCardNfcReader.CardNfcAsyncTask
import com.pro100svitlo.creditCardNfcReader.utils.CardNfcUtils
import kotlinx.android.synthetic.main.activity_cart.*

class NfcActivity : AppCompatActivity(), NfcAdapter.ReaderCallback {

    private var nfcAdapter: NfcAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
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
        val isoDep = IsoDep.get(tag)
        isoDep.connect()
        runOnUiThread { NfcRead.append("\nCard Response: "
                + Utils.toHex(isoDep.tag.id))}
        isoDep.close()
    }
}
