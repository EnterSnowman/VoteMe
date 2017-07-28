package com.example.android.voteme.vote

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.android.voteme.R
import com.example.android.voteme.model.Vote
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.fragment_vote.*
import android.R.attr.entries
import android.app.ProgressDialog
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.utils.ColorTemplate


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [VoteFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [VoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VoteFragment : Fragment(),VoteContract.View {

    // TODO: Rename and change types of parameters
    private var mVoteId: String? = null
    private var mPresenter: VoteContract.Presenter? = null
    private var mListener: OnFragmentInteractionListener? = null
    private var mProgressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mVoteId = arguments.getString(VOTE_ID)
        }
        mProgressDialog = ProgressDialog(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_vote, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mPresenter?.loadVote(mVoteId!!)
        mProgressDialog?.show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun showVote(vote: Vote) {
        mProgressDialog?.hide()
        var pieChartData = ArrayList<PieEntry>()
        for (v in vote.variants.entries){
            pieChartData.add(PieEntry(v.value.toFloat(),v.key))
        }

        val set = PieDataSet(pieChartData, "Election Results")
        set.colors = ColorTemplate.MATERIAL_COLORS.asList()
        val data = PieData(set)
        vote_stats_chart.setData(data)
        vote_stats_chart.invalidate()
    }

    override fun setPresenter(presenter: VoteContract.Presenter) {
        mPresenter = presenter
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
        private val VOTE_ID = "param1"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @param param1 Parameter 1.
         * *
         * @param param2 Parameter 2.
         * *
         * @return A new instance of fragment VoteFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(id: String): VoteFragment {
            val fragment = VoteFragment()
            val args = Bundle()
            args.putString(VOTE_ID, id)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
