package com.example.android.voteme.vote

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import com.example.android.voteme.R
import com.example.android.voteme.data.UserRepository
import com.example.android.voteme.loginregistration.LoginRegistrationActivity
import com.example.android.voteme.loginregistration.LoginRegistrationFragment
import com.example.android.voteme.loginregistration.LoginRegistrationPresenter
import com.example.android.voteme.utils.Constants
import kotlinx.android.synthetic.main.activity_vote.*
import java.lang.reflect.AccessibleObject.setAccessible
import java.net.URL


class VoteActivity : AppCompatActivity(),VoteFragment.OnFragmentInteractionListener {
    var id : String = ""
    var mPresenter: VotePresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_vote)
        setSupportActionBar(my_toolbar)
        enableMarquee()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        if (intent.getStringExtra(Constants.TITLE)!=null)
        supportActionBar?.title = intent.getStringExtra(Constants.TITLE)
        val data = this.intent.data
        id = ""
        if (data!=null){
            Log.d("INTERNAL LINKS","scheme ${data.scheme}")
            Log.d("INTERNAL LINKS","host ${data.host}")
            Log.d("INTERNAL LINKS","path ${data.path}")
            id = data.path.split("/").last()
            if(UserRepository.getInstance().isCurrentUserExists()){
            addFragment()
            }
            else{
                startActivityForResult(Intent(this,LoginRegistrationActivity::class.java),Constants.LOGIN_BEFORE_JOIN_TO_VOTE)
            }
        }
        else{
            id = intent.getStringExtra(Constants.VOTE_ID).split("/").last()
            Log.d("INTERNAL LINKS","in app link")
            addFragment()
        }


    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        intent?.putExtra("requestCode", requestCode)
        super.startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode== Activity.RESULT_OK)
            addFragment()
    }

    fun addFragment(){
        var view = VoteFragment.newInstance(id)
        supportFragmentManager.beginTransaction()
                .add(R.id.vote_container,view)
                .commitAllowingStateLoss()
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
