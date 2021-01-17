package com.leanagritest.core

import android.app.Application
import android.content.Context
import android.widget.Toast
import com.leanagritest.R
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.showAppropriateMsg(application: Application = AppObjectController.joshApplication) {
    when (this) {
        is HttpException -> {
            showToast(application, application.getString(R.string.something_went_wrong))
        }
        is SocketTimeoutException, is UnknownHostException -> {
            showToast(application, application.getString(R.string.internet_not_available_msz))
        }
        else -> {
            showToast(application, application.getString(R.string.something_went_wrong))
        }
    }
}

fun showToast(context: Context, message: String) {
    AppObjectController.uiHandler.post {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
