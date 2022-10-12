package com.strawnetwork.obviousproject.reprository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.strawnetwork.obviousproject.model.NasaModelClass
import org.json.JSONException
import java.io.IOException
import java.util.ArrayList

class NasaRepository {
    //initilize arraylist
    private var dataSet = ArrayList<NasaModelClass>()

    // Pretend to get data from a webservice or online source
    val nasaData: MutableLiveData<List<NasaModelClass>>
        get() {
            //get data from json object which is stored in the asset folder
            setNasaeImages()
            val data = MutableLiveData<List<NasaModelClass>>()
            Log.d("inside_data", "data:- " + dataSet.size)
            //post value in live data
            data.postValue(dataSet)
            return data
        }

    private fun setNasaeImages() {
        try {
            val obj = loadJSONFromAsset()
            Log.d("inside_data", "data:- " + obj.toString())
            //initilize Gson
            val gson = Gson()
            val listPersonType = object : TypeToken<List<NasaModelClass>>() {}.type
            //getting data from jsonfile and set to dataset arraylist
            dataSet = gson.fromJson(obj, listPersonType)
            Log.d("inside_data", "list:- " + dataSet.size)
        } catch (e: JSONException) {
            Log.d("inside_data", "exception:- " + e.localizedMessage)
            e.printStackTrace()
        }
    }

    fun loadJSONFromAsset(): String? {
        val jsonString: String
        try {
            //getting nasa.json file from assets and convert into string for further process
            jsonString = context!!.assets.open("nasa.json").bufferedReader().use { it.readText() }

        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    companion object {
        private var instance: NasaRepository? = null
        private var context: Context? = null
        @JvmStatic
        fun getInstance(context1: Context?): NasaRepository? {
            if (instance == null) {
                instance = NasaRepository()
                context = context1
            }
            return instance
        }
    }
}