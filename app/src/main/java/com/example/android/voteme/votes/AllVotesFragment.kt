package com.example.android.voteme.votes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.android.voteme.R
import com.example.android.voteme.model.Vote
import com.example.android.voteme.utils.Constants
import com.example.android.voteme.vote.VoteActivity

import kotlinx.android.synthetic.main.fragment_all_votes_list.*
import com.example.android.voteme.R.id.fab
import java.util.*
import java.util.function.Predicate
import kotlin.collections.ArrayList

//import com.sun.javaws.ui.SplashScreen.hide




/**
 * A fragment representing a list of Items.
 *
 *
 * Activities containing this fragment MUST implement the [OnListFragmentInteractionListener]
 * interface.
 */
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class AllVotesFragment : Fragment(),AllVotesContract.View {
    // TODO: Customize parameters
    private var mColumnCount = 1
    private var mListener: OnListFragmentInteractionListener? = null
    private var mPresenter : AllVotesContract.Presenter? = null
    private var mAdapter : AllVotesRecyclerViewAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var presenter = AllVotesPresenter(this)
        mAdapter = AllVotesRecyclerViewAdapter(context,ArrayList<Vote>(),object : AllVotesFragment.OnVoteClickListener {
            override fun onClick(id:String,title:String) {
                var intent = Intent(context, VoteActivity::class.java)
                intent.putExtra(Constants.VOTE_ID,id)
                intent.putExtra(Constants.TITLE,title)
                context.startActivity(intent)
            }
        })
        if (arguments != null) {
            mColumnCount = arguments.getInt(ARG_COLUMN_COUNT)
        }
    }
    override fun setPresenter(presenter: AllVotesContract.Presenter) {
        mPresenter = presenter;
    }

    override fun hideLoadingPanel() {
        if (loadingPanel.visibility == View.VISIBLE)
        loadingPanel.visibility = View.GONE
    }

    override fun hideEmptyVotesPanel() {
        if (empty_votesPanel.visibility == View.VISIBLE)
            empty_votesPanel.visibility = View.GONE
    }

    override fun showEmptyVotesPanel() {
        if (empty_votesPanel.visibility != View.VISIBLE)
        empty_votesPanel.visibility = View.VISIBLE
    }



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_all_votes_list, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.adapter  = mAdapter
        list.layoutManager = LinearLayoutManager(context)
        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    // Scroll Down
                    mListener!!.hideFab()
                } else if (dy < 0) {
                    // Scroll Up
                    mListener!!.showFab()
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mPresenter?.loadVotes()
        Log.d("PRESENTER",mPresenter.toString())
    }

    override fun showVotes(votes: ArrayList<Vote>) {
        mAdapter!!.mVotes.addAll(votes)
        mAdapter!!.notifyDataSetChanged()
    }

    override fun showAddedVote(vote: Vote) {
        mAdapter?.mVotes?.add(vote)
        mAdapter!!.mVotes.sortByDescending { it.timestamp }
        mAdapter?.notifyDataSetChanged()
    }

    override fun removeVote(id: String) {
        mAdapter?.mVotes = mAdapter?.mVotes?.filter { !it.id.equals(id) } as ArrayList<Vote>
        mAdapter?.notifyDataSetChanged()
    }

    override fun showError(msg: String?) {
        Toast.makeText(context,msg, Toast.LENGTH_LONG).show()
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnListFragmentInteractionListener")
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
    interface OnListFragmentInteractionListener {
        fun showFab()

        fun hideFab()
    }

    interface OnVoteClickListener{
        fun onClick(id:String,title:String)
    }

    companion object {

        // TODO: Customize parameter argument names
        private val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        fun newInstance(columnCount: Int): AllVotesFragment {
            val fragment = AllVotesFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.arguments = args
            return fragment
        }
    }
}
