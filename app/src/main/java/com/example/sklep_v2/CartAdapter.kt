package com.example.sklep_v2

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import java.net.URL


class CartAdapter(private val productsList: ArrayList<Products>) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    var amount = 0

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        private val textViewQuant = itemView.findViewById<TextView>(R.id.textView)

        @SuppressLint("SetTextI18n")
        fun bind(products: Products, index: Int) {

            val imageView = itemView.findViewById<ImageView>(R.id.image_cart)
            val textViewName = itemView.findViewById<TextView>(R.id.text_cart_name)
            val textViewPrice = itemView.findViewById<TextView>(R.id.textView_price)
            val url = URL(products.url)
            val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            val button = itemView.findViewById<ImageButton>(R.id.imageButton_delete)
            val buttonAdd = itemView.findViewById<ImageButton>(R.id.imageButton4)
            val buttonSub = itemView.findViewById<ImageButton>(R.id.imageButton3)

            textViewName.text = products.name
            textViewQuant.text = products.available.toString()
            textViewPrice.text = "Cena: " + products.price + " zł"
            imageView.setImageBitmap(bmp)

            button.setOnClickListener(){
                deleteItem(index, itemView)
            }

            buttonAdd.setOnClickListener(){
                updateAdd(itemView, index)
                textViewQuant.text = products.available.toString()
            }

            buttonSub.setOnClickListener(){
                updateSub(itemView, index)
                textViewQuant.text = products.available.toString()
            }

            calculateCost(itemView)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)

        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(productsList[position], position)
    }


    override fun getItemCount(): Int {
        return productsList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteItem(index: Int, view: View){

        productsList.removeAt(index)
        notifyDataSetChanged()

        val preferences = view.context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = preferences.edit()

        editor.clear().apply()
        editor.putString("List", Gson().toJson(productsList)).apply()
    }



    fun calculateCost(view: View){

        var price = 0f

        var cost = 0f

        for(i in 0 until productsList.size){
            price += productsList.elementAt(i).price.replace(",", ".").toFloat() * productsList.elementAt(i).available
            cost = price
        }

        val preferences = view.context.getSharedPreferences("cart_cost", Context.MODE_PRIVATE)
        val editor = preferences.edit()

        editor.clear().apply()
        editor.putString("cart", cost.toString()).apply()
    }

    fun updateAdd(view: View, index: Int){
        amount = productsList.elementAt(index).available
        amount++

        val prefAvailable = view.context.getSharedPreferences("ProductsAvalible", Context.MODE_PRIVATE)
        val quantity = Integer.parseInt(prefAvailable.getString("quantity",""))

        if(quantity<=productsList.elementAt(index).available){

            productsList.elementAt(index).available = quantity
            calculateCost(view)

        }else{

            productsList.elementAt(index).available = amount
            calculateCost(view)

        }

        val preferences = view.context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = preferences.edit()

        editor.clear().apply()
        editor.putString("List", Gson().toJson(productsList)).apply()

    }



    fun updateSub(view: View, index: Int){

        amount = productsList.elementAt(index).available
        amount--

        if(productsList.elementAt(index).available <= 1){

                productsList.elementAt(index).available = 1
                calculateCost(view)

        }else{

            productsList.elementAt(index).available = amount
            calculateCost(view)

        }

        val preferences = view.context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = preferences.edit()

        editor.clear().apply()
        editor.putString("List", Gson().toJson(productsList)).apply()

    }


}