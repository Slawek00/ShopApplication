package com.example.sklep_v2

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import org.json.JSONArray
import java.net.URL


//obsługa recylerview (listy wyświetlającej produkty), tworzenie klasy adaptera i holdera
class RecyclerViewAdapter(private val productsList: ArrayList<Products>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productsList[position])
    }


    override fun getItemCount(): Int {
        return productsList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private var list = ArrayList<Products>()
        private var amount: Int = 0


        @SuppressLint("SetTextI18n")
        fun bind(products: Products) {
            val buttonAdd: ImageButton = itemView.findViewById(R.id.image_plus)
            val buttonSub: ImageButton = itemView.findViewById(R.id.image_subtraction)
            val button: Button = itemView.findViewById(R.id.button)
            val textViewName = itemView.findViewById<TextView>(R.id.name)
            val textViewPrice = itemView.findViewById<TextView>(R.id.price)
            val textViewDes = itemView.findViewById<TextView>(R.id.description)
            val textViewAvail = itemView.findViewById<TextView>(R.id.available)
            val imageView = itemView.findViewById<ImageView>(R.id.image)
            val price = products.price
            val string = "Cena: "
            val currency = " zł"
            val sztuk = "Liczba dostepnych sztuk: "
            val dost = products.available.toString()
            val url = URL(products.url)
            val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            val quauntitys = itemView.findViewById<TextView>(R.id.quantity)

            textViewName.text = products.name
            textViewDes.text = products.des

            button.isEnabled  = products.available != 0

            textViewPrice.text = "$string $price $currency"
            textViewAvail.text = "$sztuk $dost"

            imageView.setImageBitmap(bmp)

            buttonAdd.setOnClickListener {
                amount++

                if(amount<=0){

                    quauntitys.text = "0"
                    amount = 0

                }else{

                    quauntitys.text = amount.toString()

                    if(amount>products.available){
                        quauntitys.text = products.available.toString()
                        amount = products.available
                    }

                }
            }

            buttonSub.setOnClickListener {
                amount--

                if(amount<=0){

                    quauntitys.text = "0"
                    amount = 0

                }else{

                    quauntitys.text = amount.toString()

                    if(amount>products.available){
                        quauntitys.text = products.available.toString()
                        amount = products.available
                    }

                }
            }

            button.setOnClickListener {
                addToCart(products)
            }

        }


        private fun addToCart(products: Products){
            if(amount!=0 && loadData()==false){
                    list.add(Products(products.id, products.name, products.price, products.des, amount, products.url))

                    val preferences = itemView.context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
                    val preferencesAvalible = itemView.context.getSharedPreferences("ProductsAvalible", Context.MODE_PRIVATE)

                    val editor = preferences.edit()
                    val editorAvalible = preferencesAvalible.edit()

                    editorAvalible.putString("quantity", products.available.toString())
                    editorAvalible.apply()

                    editor.putString("List", Gson().toJson(list))
                    editor.apply()

                    val toast = Toast.makeText(itemView.context, "Dodano do koszyka", Toast.LENGTH_LONG)
                    toast.setGravity(Gravity.BOTTOM, 0, 140)
                    toast.show()
            }else{
                if(amount!=0){
                    val preferences = itemView.context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
                    val items = preferences.getString("List","")
                    val json = JSONArray(items)

                    for(i in 0 until json.length()){
                        val jsonObject = json.getJSONObject(i)
                        val id = jsonObject.optInt("id")
                        val name = jsonObject.optString("name")
                        val price = jsonObject.optString("price")
                        val des = jsonObject.optString("des")
                        val available = jsonObject.optInt("available")
                        val image = jsonObject.optString("url")
                        list.add(Products(id,name,price,des,available,image))
                    }

                    if( CheckDuplicates(list,products) == false ){
                        list.add(Products(products.id,products.name,products.price,products.des,amount,products.url))
                        val editor = preferences.edit()
                        editor.clear().apply()
                        editor.putString("List", Gson().toJson(list)).apply()
                        val toast = Toast.makeText(itemView.context, "Dodano do koszyka", Toast.LENGTH_LONG)
                        toast.setGravity(Gravity.BOTTOM, 0, 140)
                        toast.show()
                    }

                    val preferencesAvalible = itemView.context.getSharedPreferences("ProductsAvalible", Context.MODE_PRIVATE)
                    val editorAvalible = preferencesAvalible.edit()

                    editorAvalible.putString("quantity", products.available.toString())
                    editorAvalible.apply()
                }
            }
        }

        fun loadData():Boolean{
            val preferences = itemView.context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
            return preferences.contains("List")
        }

        fun CheckDuplicates(list: ArrayList<Products>, products: Products):Boolean{
            var container = false

            for (i in 0 until list.size) {
                if(products.name == list.elementAt(i).name) {
                    container = true
                }
            }
            return container
        }
    }
}




