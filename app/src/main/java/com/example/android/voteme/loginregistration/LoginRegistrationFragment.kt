package com.example.android.voteme.loginregistration

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast

import com.example.android.voteme.R
import com.example.android.voteme.base.BaseView
import com.example.android.voteme.utils.Constants
import com.example.android.voteme.utils.Utils
import com.example.android.voteme.vote.VoteActivity
import com.example.android.voteme.votes.VotesActivity
import kotlinx.android.synthetic.main.fragment_login_registration.*
/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LoginRegistrationFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LoginRegistrationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginRegistrationFragment : Fragment(),LoginRegistrationContract.View {

    private var mListener: OnFragmentInteractionListener? = null
    var mPresenter : LoginRegistrationContract.Presenter? = null
    var mProgressDialog : ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mProgressDialog = ProgressDialog(context)
        mPresenter?.autoLogin()
    }
    override fun setPresenter(presenter: LoginRegistrationContract.Presenter) {
        mPresenter = presenter;

    }

    override fun showLoading( msg : String) {
        mProgressDialog?.setMessage(msg)
        mProgressDialog?.show()
    }

    override fun hideLoading() {
        mProgressDialog?.hide()
    }

    override fun showEmailVerificationMessage() {
        var alertDialogBuilder =  AlertDialog.Builder(context)
                .setTitle(R.string.sign_up_completed)
                .setMessage(R.string.email_verif_msg)
                .setPositiveButton(android.R.string.ok){dialogInterface, i -> goToVotesActivity() }
            alertDialogBuilder.create().show()
    }


    override fun makeToast(msg: String) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }

    override fun goToVotesActivity() {
        if (activity.intent.getIntExtra("requestCode",-111)==Constants.LOGIN_BEFORE_JOIN_TO_VOTE)
            activity.setResult(Activity.RESULT_OK)
        else
            startActivity(Intent(context,VotesActivity::class.java))
            activity.finish()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater!!.inflate(R.layout.fragment_login_registration, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        login.setOnClickListener{
            if (isValidInputData(emailEdit.text.toString(),passwordEdit.text.toString())){
            mPresenter?.signIn(emailEdit.text.toString(),passwordEdit.text.toString())
            showLoading(getString(R.string.signIn))
            }
            //Toast.makeText(context,"Login click",Toast.LENGTH_SHORT).show()
        }
        registration.setOnClickListener{
            if (isValidInputData(emailEdit.text.toString(),passwordEdit.text.toString())) {
                mPresenter?.signUp(emailEdit.text.toString(), passwordEdit.text.toString())
                showLoading(getString(R.string.signUp))
            }
        }
        forget_password.setOnClickListener {
            var alertDialogBuilder  = android.support.v7.app.AlertDialog.Builder(context)
            alertDialogBuilder.setTitle(R.string.restore_password)
            alertDialogBuilder.setView(R.layout.input_email_form)
            alertDialogBuilder.setMessage(R.string.restore_password_msg)
            alertDialogBuilder.setPositiveButton(android.R.string.ok)
            {dialogInterface, i ->
                var emailEdit2 = (dialogInterface as android.support.v7.app.AlertDialog).findViewById<EditText>(R.id.link_input)
                /*emailEdit2?.text = emailEdit.text
                Log.d("Firebase_user","First email ${emailEdit?.text.toString()}")*/
                /*var intent  = Intent(context, VoteActivity::class.java)
                intent.putExtra(Constants.VOTE_ID,linkEdit!!.text.toString())
                startActivity(intent)*/
                if (Utils.isEmailValid(emailEdit2?.text.toString())){
                    mPresenter!!.sendRestorePasswordEmail(emailEdit2?.text.toString())
                    Log.d("Firebase_user","try send email to ${emailEdit2?.text.toString()}}")
                }
                else
                    emailEdit2?.error = getString(R.string.input_valid_email)
            }
                    .setNegativeButton(android.R.string.cancel){dialogInterface, i ->}
            alertDialogBuilder.create().show()
        }

    }

    fun isValidInputData(email:String,password:String):Boolean{
        var res = true
        if (!Utils.isEmailValid(email)){
            emailEdit.error = getString(R.string.input_valid_email)
            res = false
        }
        if (!Utils.isPasswordValid(password)){
            passwordEdit.error = getString(R.string.input_valid_password)
            res = false
        }
        return res
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @param param1 Parameter 1.
         * *
         * @param param2 Parameter 2.
         * *
         * @return A new instance of fragment LoginRegistrationFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(): LoginRegistrationFragment {
            val fragment = LoginRegistrationFragment()
            val args = Bundle()

            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
