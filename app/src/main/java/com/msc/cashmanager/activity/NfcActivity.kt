package com.msc.cashmanager.activity

import android.content.Intent
import android.content.Intent.getIntent
import android.nfc.NfcAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.msc.cashmanager.R
import com.pro100svitlo.creditCardNfcReader.CardNfcAsyncTask
import com.pro100svitlo.creditCardNfcReader.utils.CardNfcUtils

abstract class NfcActivity :AppCompatActivity(), CardNfcAsyncTask.CardNfcInterface {

    abstract var mCardNfcUtils: CardNfcUtils
    abstract var mNfcAdapter: NfcAdapter
    abstract var mIntentFromCreate: Boolean

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
        } else {
            mCardNfcUtils = CardNfcUtils(this);
            mIntentFromCreate = true;
            onNewIntent(intent);
        }
    }

    override fun onResume() {
        super.onResume();
        mIntentFromCreate = false;
        if (mNfcAdapter != null && !mNfcAdapter.isEnabled){
        } else if (mNfcAdapter != null){
            mCardNfcUtils.enableDispatch()
        }
    }

    override fun onPause() {
        super.onPause()
        if (mNfcAdapter != null) {
            mCardNfcUtils.disableDispatch()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent);
        if (mNfcAdapter != null && mNfcAdapter.isEnabled) {
            val mCardNfcAsyncTask = CardNfcAsyncTask.Builder(this, intent, mIntentFromCreate) .build()
        }
    }
}