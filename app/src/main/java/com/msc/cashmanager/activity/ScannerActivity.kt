package com.msc.cashmanager.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Vibrator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import org.json.JSONObject
import com.android.volley.RequestQueue
import com.google.gson.Gson
import com.msc.cashmanager.model.Product
import com.msc.cashmanager.service.ProductService


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
        callToApi(result)
    }

    fun callToApi(result: String?) {
        val url = "https://api.upcitemdb.com/prod/trial/lookup?upc=" + result
        val rq = ProductService()
        rq.getRequest(url)
        rq.requestQueue.addRequestFinishedListener(
            RequestQueue.RequestFinishedListener<JSONObject>() {
                fetchProduct(rq.result)
            }
        )
    }

    fun fetchProduct(result: String) {
        val obj = JSONObject(result)
        val productArray = obj.getJSONArray("items")
        val product = Gson().fromJson(productArray[0].toString(), Product::class.java)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Product scanned")
        builder.setMessage(product.name + ": " + product.price.toString() + " €")
        builder.setPositiveButton("Add to cart") { _, _ ->
            val prod = Product(product.name, product.price)
            val rq = ProductService()
            rq.addArticleRequest(prod)
            val homeIntent = Intent(this, HomeActivity::class.java)
            startActivity(homeIntent)
            finish()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
            scannerView?.resumeCameraPreview(this@ScannerActivity)
        }
        val alert: AlertDialog = builder.create()
        alert.show()
    }
}