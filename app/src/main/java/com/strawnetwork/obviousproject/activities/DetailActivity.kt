package com.strawnetwork.obviousproject.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.strawnetwork.obviousproject.R
import com.strawnetwork.obviousproject.adapters.ViewPagerAdapter
import com.strawnetwork.obviousproject.model.NasaModelClass
import com.strawnetwork.obviousproject.viewModels.MainActivityViewModel
import kotlinx.android.synthetic.main.detail_layout.*


class DetailActivity:AppCompatActivity(),View.OnClickListener {

    private var mMainActivityViewModel: MainActivityViewModel? = null
    private lateinit var  arrayList: List<NasaModelClass>
    private var position:Int=-1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_layout)
        val extras = intent.extras
        if (extras != null) {
            position = extras.getInt("position")
            //The key argument here must match that used in the other activity
        }
        setSupportActionBar(detaoilToolbar);
        detaoilToolbar.setNavigationOnClickListener(View.OnClickListener {
            // back button pressed
            finish()
        })
        mMainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        //pass refrence of activity to viewmodel
        mMainActivityViewModel!!.NasaImagesData(this@DetailActivity)
        setClickListener()
        observeData()

    }

    private fun setClickListener() {
        nextImage.setOnClickListener(this)
        priviousImage.setOnClickListener(this)
    }


    private fun populatedata() {
        val arraySize=arrayList.size
        if(position!=-1 && position<arraySize)
        {

            val mViewPagerAdapter = ViewPagerAdapter(this, arrayList)

            // Adding the Adapter to the ViewPager

            // Adding the Adapter to the ViewPager
            viewPager.setAdapter(mViewPagerAdapter)
            viewPager.setCurrentItem(position,true)
            viewPager.addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}
                override fun onPageScrolled(
                    pos: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    Log.e("inside_position","scroll:- "+position+", "+pos)
                    position=pos
                    setTitledata(arraySize)
                }

                override fun onPageSelected(pos: Int) {
                    // Check if this is the page you want.
                    Log.e("inside_position","pos:- "+position+", "+pos)

                }
            })

        }

    }

     fun setTitledata(arraySize:Int) {
        if(position==0)
            priviousImage.visibility=View.GONE
        else
            priviousImage.visibility=View.VISIBLE
        if(position==arraySize-1)
            nextImage.visibility=View.GONE
        else
            nextImage.visibility=View.VISIBLE
        val data=arrayList.get(position)

        topTitle.text=data.title
    }

    private fun observeData() {
        //observe data from viewmodel when live data have data(means when livedata post the value)
        mMainActivityViewModel!!.getNasaData?.observe(this) { nicePlaces ->
            arrayList=nicePlaces
            populatedata()
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.nextImage->{
                position++
                populatedata()

            }
            R.id.priviousImage->{
                position--
                populatedata()

            }
            else -> {
                // else condition
            }
        }
    }
}