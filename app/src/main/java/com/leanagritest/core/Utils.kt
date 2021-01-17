package com.leanagritest.core

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.leanagritest.R
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun isInternetAvailable(): Boolean {
        val connectivityManager =
            AppObjectController.joshApplication.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }


    fun ImageView.setRoundImage(
        url: String,
        context: Context = AppObjectController.joshApplication
    ) {
        val multi = MultiTransformation(RoundedCornersTransformation(dpToPx(8), 0))
        Glide.with(context)
            .load(url)
            .apply(
                RequestOptions().placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_placeholder)
            )
            .override(Target.SIZE_ORIGINAL)
            .apply(RequestOptions.bitmapTransform(multi))
            .into(this)
    }

    fun ImageView.setImage(
        url: String,
        context: Context = AppObjectController.joshApplication
    ) {
        Glide.with(context)
            .load(url)
            .apply(
                RequestOptions().placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_placeholder)
            )
            .override(Target.SIZE_ORIGINAL)
            .into(this)
    }

    fun Int.getMovieTimeInPattern(): String {
        val hours: Int = this / 60 //since both are ints, you get an int
        val minutes: Int = this % 60
        return String.format("%d:%02d", hours, minutes)
    }

    @SuppressLint("SimpleDateFormat")
    fun String.getMovieInFormat(): String {
        if (this.isBlank()) {
            return this
        }
        val originalFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val targetFormat: DateFormat = SimpleDateFormat("MMMM dd, yyyy")
        val date: Date? = originalFormat.parse(this)
        return if (date == null) {
            this
        } else
            targetFormat.format(date)
    }

    fun openUrl(context: Context,url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            if (url.trim().startsWith("http://").not()) {
                intent.data = Uri.parse("http://" + url.replace("https://", "").trim())
            } else {
                intent.data = Uri.parse(url.trim())
            }
            context.startActivity(intent)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}
