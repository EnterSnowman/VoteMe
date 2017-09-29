package com.example.android.voteme.settings

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.preference.DialogPreference
import android.util.AttributeSet
import android.widget.Toast
import com.example.android.voteme.R
import com.example.android.voteme.data.DataSource
import com.example.android.voteme.data.UserRepository
import java.lang.Exception

/**
 * Created by Valentin on 29.09.2017.
 */
class EmailVerificationDialog(context: Context?, attrs: AttributeSet?)  : DialogPreference(context, attrs) {
    init {
        setPersistent(false)
    }

    override fun onPrepareDialogBuilder(builder: AlertDialog.Builder?) {
        builder!!.setMessage(R.string.do_you_want_send_confirm_email)
        builder.setTitle(R.string.email_verif_title)
        builder.setPositiveButton(android.R.string.ok){dialogInterface, i ->
            UserRepository.getInstance().sendVerificationEmail(object : DataSource.EmailVerificationCallback {
                override fun onSended() {
                    Toast.makeText(context,R.string.success_email_verif,Toast.LENGTH_LONG).show()
                }

                override fun onSendedFailure(exception: Exception?) {
                    Toast.makeText(context,R.string.failure_email_verif,Toast.LENGTH_LONG).show()
                }
            })}
        builder.setNegativeButton(android.R.string.cancel){dialogInterface, i ->  }
        super.onPrepareDialogBuilder(builder)
    }

    override fun showDialog(state: Bundle?) {
        super.showDialog(state)
    }
}