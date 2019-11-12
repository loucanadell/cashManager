package com.msc.cashmanager

import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.android.volley.VolleyError
import org.json.JSONObject
import com.android.volley.toolbox.JsonObjectRequest
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley


class ScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    private val REQUEST_CAMERA = 1
    private var scannerView: ZXingScannerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scannerView = ZXingScannerView(this)
        setContentView(scannerView)

        if (!checkPermission())
            requestPermission()
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this@ScannerActivity,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {

        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.CAMERA),
            REQUEST_CAMERA
        )
    }

    override fun onResume() {
        super.onResume()
        if (checkPermission()) {
            if (scannerView == null) {
                scannerView = ZXingScannerView(this)
                setContentView(scannerView)
            }
            scannerView?.setResultHandler(this)
            scannerView?.startCamera()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        scannerView?.stopCamera()
    }

    override fun handleResult(p0: Result?) {
        val result: String? = p0?.text
        val vibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(1000)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Result")
        builder.setPositiveButton("ok") { dialog, which ->
            scannerView?.resumeCameraPreview(this@ScannerActivity)
            startActivity(intent)
        }
        builder.setMessage(result)
        val alert: AlertDialog = builder.create()
        alert.show()
        val url = "https://api.upcitemdb.com/prod/trial/lookup?upc=" + result
        APIWrapper().getRequest(url, this)
        val response = APIWrapper().result
        Log.d("DEBUG", response)
    }

}