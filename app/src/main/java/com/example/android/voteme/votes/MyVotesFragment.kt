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
import com.example.android.voteme.votes.dummy.DummyContent
import com.example.android.voteme.votes.dummy.DummyContent.DummyItem
import kotlinx.android.synthetic.main.fragment_my_votes_list.*
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
class MyVotesFragment : Fragment() ,MyVotesContract.View{

    // TODO: Customize parameters
    private var mColumnCount = 1
    private var mListener: OnListFragmentInteractionListener? = null
    private var mPresenter: MyVotesContract.Presenter? = null
    private var mAdapter : MyVotesRecyclerViewAdapter? = null
    override fun setPresenter(presenter: MyVotesContract.Presenter) {
        mPresenter = presenter;

    }

    override fun showVotes(votes: ArrayList<Vote>) {
        Log.d("VIEW","showVotes")
        for (vote in votes) {
            Log.d("VIEW",vote.title)
        }
        mAdapter?.mVotes = votes
        mAdapter?.notifyDataSetChanged()
    }

    override fun showError(msg: String?) {
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var presenter = MyVotesPresenter(this)
        mAdapter = MyVotesRecyclerViewAdapter(ArrayList<Vote>(),object : OnVoteClickListener{
            override fun onClick(id:String) {
                var intent = Intent(context, VoteActivity::class.java)
                intent.putExtra(Constants.VOTE_ID,id)
                context.startActivity(intent)
            }
        })
        if (arguments != null) {
            mColumnCount = arguments.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mPresenter?.loadVotes()
        Log.d("PRESENTER",mPresenter.toString())
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_my_votes_list, container, false)

        // Set the adapter

        /*if (view is RecyclerView) {
            val context = view.getContext()
            val recyclerView = view
            if (mColumnCount <= 1) {
                recyclerView.layoutManager = LinearLayoutManager(context)
            } else {
                recyclerView.layoutManager = GridLayoutManager(context, mColumnCount)
            }
            recyclerView.adapter = MyVotesRecyclerViewAdapter(DummyContent.ITEMS, mListener)
        }*/

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.adapter  = mAdapter
        list.layoutManager = LinearLayoutManager(context)
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
        // TODO: Update argument type and name
        fun onMyVotesListFragmentInteraction(id:String)
    }

    interface OnVoteClickListener{
        fun onClick(id:String)
    }

    companion object {


        private val ARG_COLUMN_COUNT = "column-count"


        fun newInstance(columnCount: Int): MyVotesFragment {
            val fragment = MyVotesFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.arguments = args
            return fragment
        }
    }
}
