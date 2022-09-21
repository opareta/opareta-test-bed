package com.mcleroy.oparetatestbed.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.mcleroy.oparetatestbed.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.NumberFormat
import java.util.*

object Utils {

    fun <T> observeOnce(observable: LiveData<T>, observer: Observer<T>) {
        val oneTimeObserver: Observer<T> = object : Observer<T> {
            override fun onChanged(t: T) {
                observable.removeObserver(this)
                observer.onChanged(t)
            }
        }
        observable.observeForever(oneTimeObserver)
    }
    

    fun getTimestamp(date: Date?): Long {
        return date?.time ?: 0L
    }

    fun makeToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun makeSnackBar(view: View, message: String) {
        Snackbar.make(
            ContextThemeWrapper(view.context, R.style.OparetaTheme),
            view,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    fun formatAmount(sAmount: String?, currencyCode: String?): String {
        return if (sAmount == null) "0" else try {
            val amount = sAmount.toDouble()
            val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
            if (currencyCode != null) String.format(
                "%s %s",
                currencyCode,
                numberFormat.format(amount)
            ) else numberFormat.format(amount)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            "0"
        }
    }

    @Throws(IOException::class)
    fun bitmapToFileUri(bitmap: Bitmap, outDir: File, fileName: String): Uri {
        val outFile = File(outDir, fileName)
        val fos = FileOutputStream(outFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.flush()
        fos.close()
        return Uri.fromFile(outFile)
    }
    
}