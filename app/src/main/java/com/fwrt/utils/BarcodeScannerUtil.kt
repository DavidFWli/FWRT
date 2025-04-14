package com.fwrt.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.EditText
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

object BarcodeScannerUtil {

    fun scanBarcode(activity: Activity) {
        val integrator = IntentIntegrator(activity)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.setPrompt("Scan a barcode")
        integrator.setBeepEnabled(true)
        integrator.setBarcodeImageEnabled(true)
        integrator.initiateScan()
    }

    fun handleScanResult(
        activity: Activity,
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        onScanned: (String) -> Unit
    ) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null && result.contents != null) {
            onScanned(result.contents) // 扫码结果回调
        }
    }
}
