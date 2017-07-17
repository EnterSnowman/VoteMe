package com.example.android.voteme.addvote

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.android.voteme.R
import com.example.android.voteme.votes.AllVotesFragment
import com.example.android.voteme.votes.AllVotesRecyclerViewAdapter

/**
 * Created by Valentin on 17.07.2017.
 */
class VariantsAdapter(var mVariants: ArrayList<String>,  val mListener: AddVoteFragment.OnDeleteListener?) : RecyclerView.Adapter<VariantsAdapter.ViewHolder>(){
    override fun getItemCount(): Int = mVariants.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.variant_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mVarinat.text  = mVariants[position]
        holder.mDeleteButton.setOnClickListener{
            mListener?.deleteVariant(position)
        }
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView){
        val mVarinat : TextView
        val mDeleteButton : Button
        init {
            mVarinat = mView.findViewById(R.id.variant)
            mDeleteButton = mView.findViewById(R.id.deleteVariantButton)
        }
    }
}