package com.example.android.voteme.vote

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import com.example.android.voteme.R
import com.example.android.voteme.data.UserRepository
import com.example.android.voteme.loginregistration.LoginRegistrationFragment
import com.example.android.voteme.loginregistration.LoginRegistrationPresenter
import com.example.android.voteme.utils.Constants

class VoteActivity : AppCompatActivity(),VoteFragment.OnFragmentInteractionListener {

    var mPresenter: VotePresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vote)
        var view = VoteFragment.newInstance(intent.getStringExtra(Constants.VOTE_ID))
        supportFragmentManager.beginTransaction()
                .add(R.id.vote_container,view)
                .commit()
        mPresenter = VotePresenter(view)
    }

    override fun onFragmentInteraction(uri: Uri) {

    }

}
