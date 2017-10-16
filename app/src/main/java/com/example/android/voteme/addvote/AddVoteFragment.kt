package com.example.android.voteme.addvote

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.SyncStateContract
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.Toast

import com.example.android.voteme.R
import com.example.android.voteme.utils.Constants
import kotlinx.android.synthetic.main.fragment_add_vote.*
import android.support.v4.view.MenuItemCompat
import com.example.android.voteme.utils.Utils


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AddVoteFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AddVoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddVoteFragment : Fragment(), AddVoteContract.View {


    private var mPresenter: AddVoteContract.Presenter? = null
    private var mListener: OnFragmentInteractionListener? = null
    private var mAdapter : VariantsAdapter? = null
    private var mProgressDialog : ProgressDialog? = null
    private var mPopupMenu: PopupMenu? = null
    private var mOpen:Boolean = true
    private var mRevotable:Boolean = true
    override fun showError(type: Int) {
        when (type){
            Constants.NON_UNIQUE_VARIANT -> variantEdit.error = "Please, input unique option"
            Constants.EMPTY_TITLE -> voteTitleEdit.error = getString(R.string.please_input_vote_title)
            Constants.NOT_FULL_LIST -> Toast.makeText(context,R.string.two_variants,Toast.LENGTH_SHORT).show()
        }
    }

    override fun showConnectivityError(errorCode: String) {
        Toast.makeText(context,Utils.getErrorText(errorCode,context),Toast.LENGTH_LONG).show()
    }

    override fun isVariantExists(variant: String): Boolean  = mAdapter!!.mVariants.contains(variant)

    override fun setPresenter(presenter: AddVoteContract.Presenter) {
        mPresenter = presenter
    }

    override fun deleteVarinat(positon: Int) {
        mAdapter?.mVariants?.removeAt(positon)
        mAdapter?.notifyDataSetChanged()
    }
    override fun addVote() {
        mPresenter?.addVote(voteTitleEdit.text.toString(), mAdapter!!.mVariants,mOpen,mRevotable)
    }

    override fun showLoading() {
        mProgressDialog?.show()
    }

    override fun hideLoading() {
        mProgressDialog?.hide()
    }

    override fun finish() {
        activity.finish()
    }



    override fun showAddedVariant(variant: String) {
        mAdapter?.mVariants?.add(variant)
        mAdapter?.notifyDataSetChanged()
        variantEdit.setText("")
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
        setHasOptionsMenu(true)
        return inflater!!.inflate(R.layout.fragment_add_vote, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Log.d("AddVoteFrag","onViewCreated")
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("AddVoteFrag","onActivityCreated")
    }

    override fun onCreateOptionsMenu(menu: Menu,inflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_add_vote, menu)
        Log.d("AddVoteFrag","onCreateOptionsMenu")

    }



    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        Log.d("AddVoteFrag","onOptionsItemSelected")
        when(item?.itemId){
            android.R.id.home -> {
                activity.onBackPressed()
                return true
            }
            R.id.add_vote ->{
                addVote()
                return true
            }
            R.id.tune_vote->{
                if (mPopupMenu==null)
                    createPopupMenu(activity.findViewById(R.id.tune_vote))
                showPopupMenu()
                return true
            }
            else ->  return super.onOptionsItemSelected(item)
        }
    }



    fun createPopupMenu(v:View){
        mPopupMenu = PopupMenu(activity, v)
        mPopupMenu!!.inflate(R.menu.menu_popup_add_vote)
        mPopupMenu!!.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId){
                    R.id.isOpen->{
                        item.isChecked = !item.isChecked
                        mOpen = item.isChecked
                    }
                    R.id.isRevotable->{
                        item.isChecked = !item.isChecked
                        mRevotable =  item.isChecked;
                    }
                }
                item!!.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW)
                item!!.setActionView(View(context))
                MenuItemCompat.setOnActionExpandListener(item, object : MenuItemCompat.OnActionExpandListener {
                    override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                        return false
                    }

                    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                        return false
                    }
                })
                return false
            }

        })
    }

    fun showPopupMenu(){
        mPopupMenu!!.show()
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
