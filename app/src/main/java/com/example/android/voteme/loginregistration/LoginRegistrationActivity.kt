package com.example.android.voteme.loginregistration

import android.app.Fragment
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.android.voteme.R
import com.example.android.voteme.data.UserRepository
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_main.*

class LoginRegistrationActivity : AppCompatActivity(),LoginRegistrationFragment.OnFragmentInteractionListener{
    var mLoginRegistrationPresenter : LoginRegistrationPresenter? = null
    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val FRAG_TAG = "frag_tag"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var view = LoginRegistrationFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .add(R.id.login_registration_frame,view)
                .commit()
        mLoginRegistrationPresenter = LoginRegistrationPresenter(view,UserRepository.getInstance())

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
