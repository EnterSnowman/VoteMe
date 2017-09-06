package com.example.android.voteme.vote

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import com.example.android.voteme.R
import com.example.android.voteme.data.UserRepository
import com.example.android.voteme.loginregistration.LoginRegistrationFragment
import com.example.android.voteme.loginregistration.LoginRegistrationPresenter
import com.example.android.voteme.utils.Constants
import kotlinx.android.synthetic.main.activity_vote.*
import java.lang.reflect.AccessibleObject.setAccessible




class VoteActivity : AppCompatActivity(),VoteFragment.OnFragmentInteractionListener {

    var mPresenter: VotePresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_vote)
        setSupportActionBar(my_toolbar)
        enableMarquee()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        if (intent.getStringExtra(Constants.TITLE)!=null)
        supportActionBar?.title = intent.getStringExtra(Constants.TITLE)
        var view = VoteFragment.newInstance(intent.getStringExtra(Constants.VOTE_ID))
        supportFragmentManager.beginTransaction()
                .add(R.id.vote_container,view)
                .commit()
        mPresenter = VotePresenter(view)

    }

/*    override fun showLoadingBar() {

    }

    override fun hideLoadingBar() {

    }*/

    override fun showVoteTitle(title: String) {
        supportActionBar?.title = title
    }

    fun enableMarquee(){
        try {
            val f = my_toolbar.javaClass.getDeclaredField("mTitleTextView")
            f.setAccessible(true)
            val toolbarTextView = f.get(my_toolbar) as TextView
            toolbarTextView.ellipsize = TextUtils.TruncateAt.MARQUEE
            toolbarTextView.isFocusable = true
            toolbarTextView.isFocusableInTouchMode = true
            toolbarTextView.requestFocus()
            toolbarTextView.setSingleLine(true)
            toolbarTextView.isSelected = true
            toolbarTextView.marqueeRepeatLimit = -1
        } catch (e: NoSuchFieldException) {
        } catch (e: IllegalAccessException) {
        }

    }


    /*override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home->{
                onBackPressed()
                return true
            }
            R.id.copy_link->{
                var clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                var clip = ClipData.newPlainText(getString(R.string.id),intent.getStringExtra(Constants.VOTE_ID))
                clipboard.setPrimaryClip(clip)
                Toast.makeText(this,"Link copied ot clipboard",Toast.LENGTH_SHORT).show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_vote,menu)
        return true
    }*/



}
