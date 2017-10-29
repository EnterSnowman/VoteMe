package com.example.android.voteme.settings

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.preference.DialogPreference
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.android.voteme.R
import com.example.android.voteme.data.DataSource
import com.example.android.voteme.data.UserRepository
import com.example.android.voteme.utils.Utils
import java.lang.Exception

/**
 * Created by Valentin on 10.09.2017.
 */
class ChangePasswordDialog(context: Context?, attrs: AttributeSet?) : DialogPreference(context, attrs) {
    var newPassEdit: EditText? = null
    var oldPassEdit: EditText? = null
    var repeatPassEdit: EditText? = null
    var loadingBar: ProgressBar? = null
    init {
        setPersistent(false)
        setDialogLayoutResource(R.layout.change_password_form);
    }

    override fun onPrepareDialogBuilder(builder: AlertDialog.Builder?) {
        builder!!.setTitle(R.string.change_password)
        builder!!.setPositiveButton(android.R.string.ok){
            dialogInterface, i ->

        }
        super.onPrepareDialogBuilder(builder)
    }

    override fun showDialog(state: Bundle?) {
        super.showDialog(state)
        (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener{view ->
                loadingBar!!.visibility = View.VISIBLE
                if(oldPassEdit!!.text.toString().length<6){
                    oldPassEdit!!.error = context.getString(R.string.input_valid_password)
                    loadingBar!!.visibility = View.GONE
                }
                else if (newPassEdit!!.text.toString().length<6) {
                    newPassEdit!!.error = context.getString(R.string.input_valid_password)
                    loadingBar!!.visibility = View.GONE
                }
                    else if (newPassEdit!!.text.toString().equals(repeatPassEdit!!.text.toString()))
                    UserRepository.getInstance().changePassword(oldPassEdit!!.text.toString(),newPassEdit!!.text.toString(),
                            object : DataSource.ChangePasswordCallback {
                                override fun onSuccess() {
                                    loadingBar!!.visibility = View.GONE
                                    Log.d("FIREBASE_user","Changing password completed")
                                    dialog.dismiss()
                                    Toast.makeText(context,R.string.password_changed,Toast.LENGTH_SHORT).show()
                                }

                                override fun onWrongOldPassword(exception:Exception?) {
                                    if (Utils.handleException(exception!!).equals("ERROR_WRONG_PASSWORD"))
                                    oldPassEdit!!.error = context.getString(R.string.error_wrong_password)
                                    else
                                    Toast.makeText(context,Utils.getErrorText(Utils.handleException(exception!!),context),Toast.LENGTH_SHORT).show()
                                    loadingBar!!.visibility = View.GONE
                                }

                                override fun onFailure(exception:Exception?) {
                                    loadingBar!!.visibility = View.GONE
                                    Log.d("FIREBASE_user","Changing password failure")
                                    Toast.makeText(context,Utils.getErrorText(Utils.handleException(exception!!),context),Toast.LENGTH_SHORT).show()
                                }

                            })
                else{
                    loadingBar!!.visibility = View.GONE
                    newPassEdit!!.error = context.getString(R.string.different_passwords)

                } }
    }

    override fun onBindDialogView(view: View?) {
        newPassEdit = view!!.findViewById(R.id.new_password)
        oldPassEdit = view!!.findViewById(R.id.old_password)
        repeatPassEdit = view!!.findViewById(R.id.new_password_confirm)
        loadingBar = view!!.findViewById(R.id.loadingBar)
        super.onBindDialogView(view)
    }
}