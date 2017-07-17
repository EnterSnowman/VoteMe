package com.example.android.voteme.addvote

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.android.voteme.R
import com.example.android.voteme.loginregistration.LoginRegistrationFragment
import kotlinx.android.synthetic.main.activity_add_vote.*

class AddVoteActivity : AppCompatActivity(),AddVoteFragment.OnFragmentInteractionListener {
    var mPresenter : AddVotePresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vote)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(getString(R.string.new_vote))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        var view = AddVoteFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .add(R.id.add_vote_container,view)
                .commit()
        mPresenter = AddVotePresenter(view)
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> { onBackPressed()
            return true
            }
            else ->  return super.onOptionsItemSelected(item)
        }
    }
}
