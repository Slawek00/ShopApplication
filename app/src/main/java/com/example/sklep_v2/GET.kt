package com.example.sklep_v2


import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL
import org.json.JSONArray



class GET {
    fun sendGET(): ArrayList<Products> {
        val client = OkHttpClient()
        val result: String?
        val url = URL("Your server IP")
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()

        result = response.body?.string()

        val json = JSONArray(result)
        val item = ArrayList<Products>()

        for(i in 0 until json.length()) {
            val jsonObject = json.getJSONObject(i)
            val id = jsonObject.optInt("id")
            val name = jsonObject.optString("Name")
            val price = jsonObject.optString("Price_in_PLN")
            val des = jsonObject.optString("Description")
            val available = jsonObject.optInt("Is_avaible")
            val image = jsonObject.optString("URL_image")
            item.add(Products(id,name, price,des,available,image))
        }

        return item
    }

}
