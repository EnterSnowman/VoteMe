package com.example.android.voteme.loginregistration

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.android.voteme.R
import com.example.android.voteme.base.BaseView
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

    override fun makeToast(msg: String) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }

    override fun goToVotesActivity() {
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
            mPresenter?.signIn(emailEdit.text.toString(),passwordEdit.text.toString())
            showLoading(getString(R.string.signIn))
            //Toast.makeText(context,"Login click",Toast.LENGTH_SHORT).show()
        }
        registration.setOnClickListener{
            mPresenter?.signUp(emailEdit.text.toString(),passwordEdit.text.toString())
            showLoading(getString(R.string.signUp))
        }
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
