package com.pekyurek.emircan.presentation.core.dialog

import androidx.appcompat.app.AppCompatActivity
import com.pekyurek.emircan.domain.exception.base.BaseException
import com.pekyurek.emircan.presentation.core.dialog.base.BaseAlertDialog

class ExceptionDialog(
    activity: AppCompatActivity,
    exception: BaseException,
    finishActivityAfterErrorPopup: Boolean = false,
) : BaseAlertDialog(activity) {

    init {
        setCancelable(false)
        setTitle(exception.titleResId)
        setMessage(exception.message)
        setPositiveButton(android.R.string.ok) { dialog, _ ->
            if (finishActivityAfterErrorPopup) activity.finish()
            dialog.dismiss()
        }
        create()
    }
}