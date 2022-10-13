package com.strawnetwork.obviousproject.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.strawnetwork.obviousproject.R
import com.strawnetwork.obviousproject.model.NasaModelClass
import com.strawnetwork.obviousproject.utils.ClickInterface
import com.strawnetwork.obviousproject.utils.UtilClass
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class NasaImagesAdapter(private val context: Context, private val arrayList: List<NasaModelClass>,val clickInterface: ClickInterface,val layoutManager: GridLayoutManager? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //it is a static block like in java and it is execute first when adapter is loaded
    companion object {
        private val LIST_ITEM = 0
        private val GRID_ITEM = 1
    }

    val TAG:String="adsapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)
        Log.e("inside_create","type:- "+viewType)
        when (viewType) {
            GRID_ITEM -> {
                val layoutImage = inflater.inflate(
                    R.layout.image_nasa_row, parent,
                    false
                )
                viewHolder=ImageViewHolder(layoutImage)
            }

            LIST_ITEM -> {
                val layoutVideo = inflater.inflate(
                    R.layout.image_nasa_list, parent,
                    false
                )
                viewHolder= ImageViewHolder(layoutVideo)
            }
        }
        return viewHolder!!
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data=arrayList[position]
        when (holder.itemViewType) {

            GRID_ITEM,LIST_ITEM -> {
                (holder as ImageViewHolder).nasaTitle.text = data.title
                //let is used if data is not null then it will execute for null safty
                holder.nasadate.text= data.date?.let { UtilClass.formatData(it) }
                Glide.with(context)
                    .load(data.url)
                    .apply( RequestOptions().override(100, 100))
                    .listener(object : RequestListener<Drawable> {
                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            holder.progressBar.visibility = View.GONE
                            return false
                        }

                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            holder.progressBar.visibility = View.GONE
                            return false
                        }
                    })
                    .into(holder.nasaImage)
            }
        }
        holder.itemView.setOnClickListener(View.OnClickListener {
            if(clickInterface!=null)
                clickInterface.onClickData(position)
        })
    }

    override fun getItemViewType(position: Int): Int {
        return if (layoutManager?.spanCount == 1) LIST_ITEM
        else GRID_ITEM
    }

    override fun getItemCount(): Int {
        //return number of element present in the list
        return arrayList.size
    }

    inner class ImageViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {

        var nasaTitle: TextView
        var nasaImage: ImageView
        var progressBar:ProgressBar
        var nasadate:TextView

        init {
            nasaTitle = convertView.findViewById(R.id.nasaTitle)
            nasaImage=convertView.findViewById(R.id.nasaImage)
            progressBar=convertView.findViewById(R.id.progressBar)
            nasadate=convertView.findViewById(R.id.nasadate)
        }
    }






}