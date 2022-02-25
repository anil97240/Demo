package com.tchs.demotask.Ratrofit

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.tchs.demotask.R

object Utils {
    var progressDialog: ProgressDialog? = null
    fun showProgress(activity: Activity?) {
        if (!isProgressShowing()) {
            progressDialog = ProgressDialog(activity)
            try {
                progressDialog!!.show()
            } catch (e: WindowManager.BadTokenException) {
            }
            progressDialog!!.setCancelable(false)
            progressDialog!!.window!!
                .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressDialog!!.setContentView(R.layout.progressdialog)
        }
    }

    private fun isProgressShowing(): Boolean {
        if (progressDialog == null)
            return false
        else {
            return progressDialog!!.isShowing
        }
    }

    fun dismissProgress() {
        try {
            if (progressDialog != null && progressDialog!!.isShowing) progressDialog!!.dismiss() else Log.i(
                "Dialog",
                "already dismissed"
            )
        } catch (e: Exception) {
        }
    }

    fun hideInputSoftKey(activity: Activity?) {
        try {
            val inputManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                activity!!.currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        } catch (aE: Exception) {
            aE.printStackTrace()
        }

    }

}