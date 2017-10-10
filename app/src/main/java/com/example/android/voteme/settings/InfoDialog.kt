package com.example.android.voteme.settings

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.preference.DialogPreference
import android.util.AttributeSet
import android.view.View
import com.example.android.voteme.R

/**
 * Created by Valentin on 10.10.2017.
 */
class InfoDialog(context: Context?, attrs: AttributeSet?) : DialogPreference(context, attrs) {
    init {
        setPersistent(false)
        setDialogLayoutResource(R.layout.info_dialog)
    }

    override fun onPrepareDialogBuilder(builder: AlertDialog.Builder?) {
        builder!!.setTitle(R.string.info_title)
        builder!!.setPositiveButton(android.R.string.ok){dialogInterface, i ->  }
        builder!!.setCancelable(false)
        super.onPrepareDialogBuilder(builder)
    }

    override fun showDialog(state: Bundle?) {
        super.showDialog(state)
        (dialog as AlertDialog).getButton(AlertDialog.BUTTON_NEGATIVE).visibility = View.INVISIBLE
    }
}