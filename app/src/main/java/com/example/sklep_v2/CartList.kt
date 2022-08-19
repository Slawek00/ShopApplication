package com.example.sklep_v2

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CartList.newInstance] factory method to
 * create an instance of this fragment.
 */
class CartList : Fragment() {
    // TODO: Rename and change types of parameters
    private var aCart = ArrayList<Products>()
    private lateinit var mAdapter: CartAdapter
    private var param1: String? = null
    private var param2: String? = null
    private var request = POST()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart_list, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CartList.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartList().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){

        val layoutManager = LinearLayoutManager(context)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView2)
        recyclerView.layoutManager = layoutManager
        val preferences = view.context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val button_list = view.findViewById<FloatingActionButton>(R.id.viewList_button)
        val button_pay = view.findViewById<FloatingActionButton>(R.id.button_pay)

        if(preferences.contains("List")){
            val list = preferences.getString("List","")
            val json = JSONArray(list)
            for(i in 0 until json.length()){
                val jsonObject = json.getJSONObject(i)
                val id = jsonObject.optInt("id")
                val name = jsonObject.optString("name")
                val price = jsonObject.optString("price")
                val des = jsonObject.optString("des")
                val available = jsonObject.optInt("available")
                val image = jsonObject.optString("url")
                aCart.add(Products(id,name,price,des,available,image))
            }
        }

        button_list.setOnClickListener(){
            val fragment = ShopList()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame,fragment)
            transaction.disallowAddToBackStack()
            transaction.commit()
        }

        button_pay.setOnClickListener(){
            pays(view)
        }

        mAdapter = CartAdapter(aCart)
        recyclerView.adapter = mAdapter
        super.onViewCreated(view, savedInstanceState)
    }

    private fun pays(view: View){
        val preferences = view.context.getSharedPreferences("cart_cost", Context.MODE_PRIVATE)
        val cost = preferences.getString("cart","")
        val builder = AlertDialog.Builder(view.context)
        builder.setTitle("Wartość koszyka")
        if(aCart.isEmpty()){
            builder.setMessage("0.00 zł")
        }else{
            builder.setMessage(cost.toString()+" zł")
        }
        builder.setIcon(R.drawable.cart)


        builder.setPositiveButton("Potwierdź"){ _, _ ->
            request.sendPOST(request.getList(view))
        }

        builder.setNegativeButton("Wróć"){ _, _ ->

        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

    }
}