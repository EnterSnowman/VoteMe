package com.example.android.voteme.addvote

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.android.voteme.R
import kotlinx.android.synthetic.main.fragment_add_vote.*
/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AddVoteFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AddVoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddVoteFragment : Fragment(), AddVoteContract.View {

    // TODO: Rename and change types of parameters

    private var mPresenter: AddVoteContract.Presenter? = null
    private var mListener: OnFragmentInteractionListener? = null
    private var mAdapter : VariantsAdapter? = null
    private var mProgressDialog : ProgressDialog? = null

    override fun setPresenter(presenter: AddVoteContract.Presenter) {
        mPresenter = presenter
    }

    override fun deleteVarinat(positon: Int) {
        mAdapter?.mVariants?.removeAt(positon)
        mAdapter?.notifyDataSetChanged()
    }
    override fun addVote() {
        mPresenter?.addVote(voteTitleEdit.text.toString(), mAdapter!!.mVariants)
    }

    override fun showLoading() {
        mProgressDialog?.show()
    }

    override fun hideLoading() {
        mProgressDialog?.hide()
    }



    override fun showAddedVariant(variant: String) {
        mAdapter?.mVariants?.add(variant)
        mAdapter?.notifyDataSetChanged()
    }

    interface OnDeleteListener{
        fun deleteVariant(position : Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mProgressDialog = ProgressDialog(context)
        if (arguments != null) {

        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_add_vote, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = VariantsAdapter(ArrayList<String>(),object : OnDeleteListener{
            override fun deleteVariant(position: Int) {
                mPresenter?.deleteVariant(position)
            }

        })
        listOfVariants.layoutManager = LinearLayoutManager(view?.context)
        listOfVariants.adapter = mAdapter
        addVariantButton.setOnClickListener{
            mPresenter?.addVarinat(variantEdit.text.toString())
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
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @param param1 Parameter 1.
         * *
         * @param param2 Parameter 2.
         * *
         * @return A new instance of fragment AddVoteFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(): AddVoteFragment {

            val fragment = AddVoteFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
