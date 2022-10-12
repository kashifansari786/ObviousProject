package com.strawnetwork.obviousproject.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.strawnetwork.obviousproject.model.NasaModelClass
import com.strawnetwork.obviousproject.reprository.NasaRepository
import com.strawnetwork.obviousproject.reprository.NasaRepository.Companion.getInstance

class MainActivityViewModel : ViewModel() {
    //live data
    private var mNasaImagesdata: MutableLiveData<List<NasaModelClass>>? = null
    private var mRepo: NasaRepository? = null

    fun NasaImagesData(mainActivity: Context?)
    {
        //getting instance of NasaRepository
        mRepo = getInstance(mainActivity)
        mainActivity?.let {
            //getting data of nasa images from NasaRepository
            mNasaImagesdata=mRepo!!.nasaData
        }
    }

    val getNasaData: LiveData<List<NasaModelClass>>?
        get() = mNasaImagesdata

}