package com.example.sklep_v2

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.text.method.TextKeyListener.clear
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_shop_list.*
import java.nio.charset.Charset

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main)
        supportActionBar!!.title = "Sklep"


        createFragment(ShopList())


        if (Build.VERSION.SDK_INT > 9) {
            val policy = ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

    }

    private fun createFragment(shopList: Fragment){
        val fragmentManager= supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.frame,shopList)
        transaction.commit()
    }

    override fun onStart() {
        val preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.clear().apply()
        val prefAvailable = getSharedPreferences("ProductsAvalible", Context.MODE_PRIVATE)
        val editorAV = prefAvailable.edit()
        editorAV.clear().apply()
        val preferencesCart = getSharedPreferences("cart_cost", Context.MODE_PRIVATE)
        val editorCart = preferencesCart.edit()
        editorCart.clear().apply()
        super.onStart()
    }

    override fun onDestroy(){
        val preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.clear().apply()
        val prefAvailable = getSharedPreferences("ProductsAvalible", Context.MODE_PRIVATE)
        val editorAV = prefAvailable.edit()
        editorAV.clear().apply()
        val preferencesCart = getSharedPreferences("cart_cost", Context.MODE_PRIVATE)
        val editorCart = preferencesCart.edit()
        editorCart.clear().apply()
        super.onDestroy()
    }

}








