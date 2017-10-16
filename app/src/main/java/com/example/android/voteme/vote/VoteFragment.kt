package com.example.android.voteme.vote

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment

import com.example.android.voteme.R
import com.example.android.voteme.model.Vote
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.fragment_vote.*
import android.R.attr.entries
import android.app.ProgressDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.Color
import android.support.v4.content.res.ResourcesCompat
import android.util.Log
import android.view.*
import android.widget.RadioButton
import android.widget.RelativeLayout
import android.widget.Toast
import com.example.android.voteme.utils.Constants
import com.example.android.voteme.utils.MyPieChartValueFormatter
import com.example.android.voteme.utils.Utils
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
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
    private var mPieChartData : ArrayList<PieEntry>? = null
    private var mPieDataSet: PieDataSet? = null
    private var mVote : Vote? = null
    private var mData : PieData?= null
    private var isVotesExists : Boolean? = null
    private var mIsVoted : Boolean? = null
    private var mTotalVotes : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("VoteFragment","onCreate")
        if (arguments != null) {
            mVoteId = arguments.getString(VOTE_ID)
        }
        mProgressDialog = ProgressDialog(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        Log.d("VoteFragment","onCreateView")
        setHasOptionsMenu(true)
        return inflater!!.inflate(R.layout.fragment_vote, container, false)
    }

    override fun onPause() {
        super.onPause()
        mPresenter!!.removeChildListener(mVoteId!!)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isVotesExists = false
        mPresenter?.joinToVote(mVoteId!!)
        mPresenter?.loadVote(mVoteId!!)
        vote_button.setOnClickListener{
            if(vote_variants.checkedRadioButtonId!=-1){
            var r  = vote_variants.findViewById<RadioButton>(vote_variants.checkedRadioButtonId)
            mPresenter?.chooseVariant(mVoteId!!,r.text.toString())
            }
            else
                Toast.makeText(context,R.string.select_option,Toast.LENGTH_SHORT).show()

        }
        loadingPanel.visibility = View.VISIBLE
    }

    override fun showProgressBar() {
        electing_progressBar.visibility = View.VISIBLE

    }

    override fun showError(errorCode: String) {
        Toast.makeText(context,Utils.getErrorText(errorCode,context),Toast.LENGTH_LONG).show()
    }

    override fun hideProgressBar() {
        electing_progressBar.visibility = View.INVISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun showVote(vote: Vote, isVoted: Boolean, choosenVariant: String?) {
        if (choosenVariant!=null)
        Log.d("FIREBASE choosen ", choosenVariant)
        else
            Log.d("FIREBASE choosen ", "NOPE")
        mVote = vote
        mIsVoted=  isVoted
        mListener?.showVoteTitle(mVote!!.title)
        mPieChartData = ArrayList<PieEntry>()
        for (v in vote.variants.entries) {
            if (v.value!=0){
                mTotalVotes+=v.value
            mPieChartData!!.add(PieEntry(v.value.toFloat(), v.key))
            isVotesExists = true
            }
            val r  = activity.layoutInflater.inflate(R.layout.custom_radiobutton,null) as RadioButton
            r.setText(v.key)
            vote_variants.addView(r)
            if (isVoted&&v.key.equals(choosenVariant))
                vote_variants.check(r.id)
        }
        Log.d("FIREBASE isVoted", isVoted.toString().plus(" "+vote.id))
        Log.d("FIREBASE isOpen",mVote!!.isOpen.toString())
        Log.d("FIREBASE isRevotable",mVote!!.isRevotable.toString())
        mPieDataSet = PieDataSet(mPieChartData, "")
        mPieDataSet!!.colors = ColorTemplate.MATERIAL_COLORS.toList()
        mPieDataSet!!.colors.addAll(ColorTemplate.JOYFUL_COLORS.toList())
        mData = PieData(mPieDataSet)
        mData?.setValueTextSize(16f)
        mData?.setValueFormatter(MyPieChartValueFormatter())

        var d = Description()
        d.text = ""
        val paint = vote_stats_chart.getPaint(Chart.PAINT_INFO)
        paint.textSize = 40F
        vote_stats_chart.setNoDataText(getString(R.string.make_choise))
        vote_stats_chart.setNoDataTextColor(Color.BLACK)
        vote_stats_chart.description = d
        vote_stats_chart.invalidate()
        showPieChart(isVoted)
        hideVotingPanel(isVoted)
        loadingPanel.visibility = View.GONE
        content.visibility = View.VISIBLE
    }

    override fun hideVotingPanel(isVoted:Boolean) {
        if (!mVote!!.isRevotable&&isVoted){
        vote_variants.visibility = View.GONE
        vote_button.visibility  =View.GONE
        }
    }

    override fun showPieChart(isVoted: Boolean) {
        if((mVote!!.isOpen||isVoted)&&isVotesExists!!){
            mIsVoted = isVoted
            vote_stats_chart.layoutParams = RelativeLayout.LayoutParams(vote_stats_chart.width,vote_stats_chart.width)
            vote_stats_chart.setData(mData)
            vote_stats_chart.setEntryLabelColor(R.color.colorPrimaryDark)
            vote_stats_chart.setEntryLabelTextSize(16f)
            vote_stats_chart.setEntryLabelTypeface(ResourcesCompat.getFont(context,R.font.roboto_bold))
            vote_stats_chart.legend.form = Legend.LegendForm.CIRCLE
            vote_stats_chart.legend.textSize = 16f
            vote_stats_chart.legend.typeface = ResourcesCompat.getFont(context,R.font.roboto_bold)
            vote_stats_chart.legend.position = Legend.LegendPosition.BELOW_CHART_CENTER
            vote_stats_chart.holeRadius = 25F
            mPieDataSet!!.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            vote_stats_chart.transparentCircleRadius= 30F
            vote_stats_chart.setUsePercentValues(true)
            vote_stats_chart.centerText = getString(R.string.totalVotes).plus(mTotalVotes.toString())
            vote_stats_chart.setCenterTextTypeface(ResourcesCompat.getFont(context,R.font.roboto_thin_italic))
            vote_stats_chart.invalidate()
        }
    }

    override fun updateVote(variant: String, newCount: Int) {
        mVote?.variants?.put(variant,newCount)
        mPieChartData?.clear()
        mTotalVotes = 0
        for (v in mVote?.variants?.entries!!){
            if (v.value!=0){
                mTotalVotes+=v.value
            mPieChartData!!.add(PieEntry(v.value.toFloat(),v.key))
            isVotesExists = true
            }
        }
        showPieChart(mIsVoted!!)
        if (mIsVoted!!||mVote!!.isOpen){
            vote_stats_chart.centerText = getString(R.string.totalVotes).plus(mTotalVotes.toString())
        vote_stats_chart.data.notifyDataChanged()
        vote_stats_chart.notifyDataSetChanged()
        vote_stats_chart.invalidate()
        }
    }
    override fun setPresenter(presenter: VoteContract.Presenter) {
        mPresenter = presenter
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home->{
                activity.onBackPressed()
                return true
            }
            R.id.copy_link->{
                var clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                var clip = ClipData.newPlainText(getString(R.string.id),getString(R.string.scheme)
                        .plus(getString(R.string.host))
                        .plus(getString(R.string.path))
                        .plus(activity.intent.getStringExtra(Constants.VOTE_ID)))
                clipboard.setPrimaryClip(clip)
                Toast.makeText(activity,"Link copied ot clipboard", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?,  inflater:MenuInflater) {
        inflater.inflate(R.menu.menu_vote,menu)

    }


    // TODO: Rename method, update argument and hook method into UI event


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
        //fun setActionBarTitle(title:String)

        /*fun showLoadingBar()

        fun hideLoadingBar()*/

        fun showVoteTitle(title:String)
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
