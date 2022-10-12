package com.strawnetwork.obviousproject.activities


import android.arch.lifecycle.ViewModelProviders
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.strawnetwork.obviousproject.R
import com.strawnetwork.obviousproject.viewModels.MainActivityViewModel


class MainActivity : AppCompatActivity() {
    private var mSwipeRefresh: SwipeRefreshLayout? = null
    private var mMainActivityViewModel: MainActivityViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        mMainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        //pass refrence of activity to viewmodel
        mMainActivityViewModel!!.NasaImagesData(this@MainActivity)

    }

}