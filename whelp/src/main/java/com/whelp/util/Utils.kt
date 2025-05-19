package com.whelp.util

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.internal.and
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object Utils {

    fun hmac(
        data: String?,
        key: String?
    ): String {

        return try {
            val signingKey = SecretKeySpec(key?.toByteArray(Charsets.UTF_8), "HmacSHA256")
            val mac: Mac = Mac.getInstance("HmacSHA256")
            mac.init(signingKey)
            val rawHmac: ByteArray = mac.doFinal(data?.toByteArray(Charsets.UTF_8))
            val hexArray = byteArrayOf(
                '0'.code.toByte(),
                '1'.code.toByte(),
                '2'.code.toByte(),
                '3'.code.toByte(),
                '4'.code.toByte(),
                '5'.code.toByte(),
                '6'.code.toByte(),
                '7'.code.toByte(),
                '8'.code.toByte(),
                '9'.code.toByte(),
                'a'.code.toByte(),
                'b'.code.toByte(),
                'c'.code.toByte(),
                'd'.code.toByte(),
                'e'.code.toByte(),
                'f'.code.toByte()
            )
            val hexChars = ByteArray(rawHmac.size * 2)
            for (j in rawHmac.indices) {
                val v: Int = rawHmac[j] and 0xFF
                hexChars[j * 2] = hexArray[v ushr 4]
                hexChars[j * 2 + 1] = hexArray[v and 0x0F]
            }
            String(hexChars)
        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }
    }

    fun Context.isChromeInstalledAndVersionGreaterThan80(): Boolean {
        val pInfo: PackageInfo = try {
            this.packageManager.getPackageInfo("com.android.chrome", 0)
        } catch (e: PackageManager.NameNotFoundException) {
            //chrome is not installed on the device
                e.printStackTrace()
            return false
        }

        //using the first dot we find in the string
        val firstDotIndex = pInfo.versionName.orEmpty().indexOf(".")
        //take only the number before the first dot excluding the dot itself
        val majorVersion = pInfo.versionName.orEmpty().substring(0, firstDotIndex)

        return majorVersion.toInt() > 80
    }

}