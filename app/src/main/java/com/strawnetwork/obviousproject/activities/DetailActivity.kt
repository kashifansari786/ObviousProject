package com.strawnetwork.obviousproject.activities

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.strawnetwork.obviousproject.R
import com.strawnetwork.obviousproject.model.NasaModelClass
import com.strawnetwork.obviousproject.viewModels.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
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
            if(position==0)
                priviousImage.visibility=View.GONE
            else
                priviousImage.visibility=View.VISIBLE
            if(position==arraySize-1)
                nextImage.visibility=View.GONE
            else
                nextImage.visibility=View.VISIBLE
            val data=arrayList.get(position)
            Glide.with(this)
                .load(data.hdurl)
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        progressDetail.visibility = View.GONE
                        return false
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        progressDetail.visibility = View.GONE
                        return false
                    }
                })
                .into(mainImage)
            copyright.text=data.copyrights
            date.text=data.date
            detailTitle.text=data.title
            topTitle.text=data.title
            description.text=data.explanation
        }

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