package com.example.android.voteme.votes

import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

import android.widget.TextView
import android.widget.Toast

import com.example.android.voteme.R
import com.example.android.voteme.addvote.AddVoteActivity
import com.example.android.voteme.data.DataSource
import com.example.android.voteme.data.UserRepository
import com.example.android.voteme.loginregistration.LoginRegistrationActivity
import com.example.android.voteme.utils.Constants
import com.example.android.voteme.vote.VoteActivity
import kotlinx.android.synthetic.main.activity_votes.*

class VotesActivity : AppCompatActivity(),AllVotesFragment.OnListFragmentInteractionListener,MyVotesFragment.OnListFragmentInteractionListener {



    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * [FragmentPagerAdapter] derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var mMyVotesPresenter : MyVotesPresenter? = null
    private var mUserRepository = UserRepository.getInstance()
    /**
     * The [ViewPager] that will host the section contents.
     */
    private var mViewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_votes)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById<View>(R.id.container) as ViewPager
        mViewPager!!.adapter = mSectionsPagerAdapter

        val tabLayout = findViewById<View>(R.id.tabs) as TabLayout

        mViewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(mViewPager))

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->

            mUserRepository.isUserVerified(object : DataSource.IsVerifiedCallback{
                override fun onResult(isVerified: Boolean) {
                    if (isVerified)
                        startActivity(Intent(view.context,AddVoteActivity::class.java))
                    else
                        Toast.makeText(view.context,R.string.vote_creating_condition,Toast.LENGTH_LONG).show()
                }
            }

            )

        }
        /*mMyVotesPresenter  = MyVotesPresenter(mSectionsPagerAdapter!!.getItem(1) as MyVotesContract.View)

        Log.d("VIEW",(mSectionsPagerAdapter!!.getItem(1) as MyVotesContract.View).toString())*/


    }


    override fun showFab() {
        if(!fab.isShown)
            fab.show()
    }

    override fun hideFab() {
        if(fab.isShown)
            fab.hide()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_votes, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        when(id){
            R.id.signout->{
                var alertDialogBuilder  = AlertDialog.Builder(this)
                alertDialogBuilder.setTitle(R.string.logout)
                alertDialogBuilder.setMessage(R.string.confirm_logout)
                alertDialogBuilder.setPositiveButton(android.R.string.ok)
                {dialogInterface, i ->
                    UserRepository.getInstance().mAuth.signOut()
                    finish()
                    startActivity(Intent(this,LoginRegistrationActivity::class.java))
                }
                        .setNegativeButton(android.R.string.cancel){dialogInterface, i ->}
                alertDialogBuilder.create().show()
                return true
            }

            R.id.join_vote ->{
                var alertDialogBuilder  = AlertDialog.Builder(this)
                alertDialogBuilder.setTitle(R.string.join_vote)
                alertDialogBuilder.setView(R.layout.input_link_form)
                alertDialogBuilder.setMessage(R.string.input_link)
                alertDialogBuilder.setPositiveButton(android.R.string.ok)
                {dialogInterface, i ->
                    var linkEdit = (dialogInterface as AlertDialog).findViewById<EditText>(R.id.link_input)
                    var intent  = Intent(this,VoteActivity::class.java)
                    intent.putExtra(Constants.VOTE_ID,linkEdit!!.text.toString())
                    startActivity(intent)
                }
                        .setNegativeButton(android.R.string.cancel){dialogInterface, i ->}
                alertDialogBuilder.create().show()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }



    }

    /**
     * A placeholder fragment containing a simple view.
     */
   /* class PlaceholderFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater!!.inflate(R.layout.fragment_votes, container, false)
            val textView = rootView.findViewById<View>(R.id.section_label) as TextView
            textView.text = getString(R.string.section_format, arguments.getInt(ARG_SECTION_NUMBER))
            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }*/

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            when (position){
                0 -> return AllVotesFragment.newInstance(1)
                1 -> return MyVotesFragment.newInstance(1)
                else ->  return MyVotesFragment.newInstance(1)
            }

        }

        override fun getCount(): Int {
            return 2
        }
    }
}
