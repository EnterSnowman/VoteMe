package com.example.android.voteme.votes

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.android.voteme.R
import com.example.android.voteme.model.Vote

import com.example.android.voteme.votes.MyVotesFragment.OnListFragmentInteractionListener


/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyVotesRecyclerViewAdapter(var mVotes: ArrayList<Vote>, private val mListener: MyVotesFragment.OnVoteClickListener?) : RecyclerView.Adapter<MyVotesRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.new_vote_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mVotes[position]
        holder.mContentView.text = mVotes[position].title

        holder.mView.setOnClickListener {
            mListener?.onClick(mVotes.get(position).id,mVotes.get(position).title)
        }
    }

    override fun getItemCount(): Int {
        return mVotes.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mContentView: TextView
        var mItem: Vote? = null

        init {
            //mIdView = mView.findViewById<TextView>(R.id.id) //as TextView
            mContentView = mView.findViewById<TextView>(R.id.content) //as TextView
        }

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
