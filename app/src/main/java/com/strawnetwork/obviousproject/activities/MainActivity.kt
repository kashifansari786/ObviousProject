package com.strawnetwork.obviousproject.activities


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.strawnetwork.obviousproject.R
import com.strawnetwork.obviousproject.adapters.NasaImagesAdapter
import com.strawnetwork.obviousproject.viewModels.MainActivityViewModel
import kotlinx.android.synthetic.main.content_main.*


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
       // setUpNavigation()
        observeData()
    }
    private fun observeData() {
        //observe data from viewmodel when live data have data(means when livedata post the value)
        mMainActivityViewModel!!.getNasaData?.observe(this) { nicePlaces ->
            recyclerView.apply {
                setHasFixedSize(true)
                //set grid layout manager to recyclerview with count 2
                layoutManager= GridLayoutManager(this@MainActivity,2)
                //add decoration to recyclerview horizxonral line between 2 cards
                addItemDecoration(
                    DividerItemDecoration(recyclerView.context,
                        DividerItemDecoration.HORIZONTAL)
                )
                addItemDecoration(
                    DividerItemDecoration(recyclerView.context,
                        DividerItemDecoration.VERTICAL)
                )
                //initilize the adapter with arraylist of data and set to recyclerview
                adapter= NasaImagesAdapter(this@MainActivity,nicePlaces)
            }
        }
    }
}