package com.example.sklep_v2

import android.content.Context
import android.view.View
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException



class POST {
    fun sendPOST(jsonArray: JSONArray) {
        val client = OkHttpClient()
        val formBody = MultipartBody.Builder()
            .addFormDataPart("Data", jsonArray.toString())
            .build()
        val request = Request.Builder()
            .url("Your server IP")
            .post(formBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            println(response.body!!.string())
            }
    }


        fun getList(view: View):JSONArray{

            val items = ArrayList<Products>()
            val objectData = JSONArray()
            val preferences = view.context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
            val preferencesData = preferences.getString("List","")
            val json = JSONArray(preferencesData)


            for(i in 0 until json.length()){
                val jsonObject = json.getJSONObject(i)
                val id = jsonObject.optInt("id")
                val name = jsonObject.optString("name")
                val price = jsonObject.optString("price")
                val des = jsonObject.optString("des")
                val available = jsonObject.optInt("available")
                val image = jsonObject.optString("url")
                items.add(Products(id,name,price,des,available,image))
            }


            for(i in 0 until items.size){
                objectData.apply {
                    put(JSONObject().apply {
                        put("Product_id", items.elementAt(i).id.toString())
                        put("Amount", items.elementAt(i).available.toString())
                    })
                }
            }

            return objectData
        }
}
