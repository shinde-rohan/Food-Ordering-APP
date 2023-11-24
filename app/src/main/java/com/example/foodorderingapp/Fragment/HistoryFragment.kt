package com.example.foodorderingapp.Fragment

import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodorderingapp.Adapter.BuyAgainAdapter
import com.example.foodorderingapp.Model.OrderDetails
import com.example.foodorderingapp.R
import com.example.foodorderingapp.databinding.FragmentHistoryBinding
import com.example.foodorderingapp.recentOrderItemActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var auth : FirebaseAuth
    private lateinit var userId : String
    private  var listOfOrderItem : MutableList<OrderDetails> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater,container, false)
//        setUpRecyclerView()
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        // retrive and display user history
        retriveBuyHistory()
        binding.recentBuyItem.setOnClickListener {
            seeItemRecentBuy()
        }
        return binding.root
    }

    private fun seeItemRecentBuy() {
        listOfOrderItem.firstOrNull()?.let { recentBuy ->
            val intent = Intent(requireContext(),recentOrderItemActivity::class.java)

        }
    }

    private fun retriveBuyHistory() {
        binding.recentBuyItem.visibility = View.INVISIBLE
        userId = auth.currentUser?.uid?:""
        val buyItemRef = database.reference.child("user").child(userId).child("BuyHistory")
        val shortingQuery = buyItemRef.orderByChild("currentTime")
        shortingQuery.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(buySnapshot in snapshot.children){
                    val buyHistory = buySnapshot.getValue(OrderDetails::class.java)
                    buyHistory?.let {
                        listOfOrderItem.add(it)
                    }
                    listOfOrderItem.reverse()
                    if(listOfOrderItem.isNotEmpty()){
                        setDataInRecentBuy()
                        setPreviousBuyItems()
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setDataInRecentBuy() {
        binding.recentBuyItem.visibility = View.VISIBLE
        val recentOrderItem = listOfOrderItem.firstOrNull()
        recentOrderItem?.let {
            with(binding){
                buyName.text= it.foodName?.firstOrNull()?:""
                buyPrice.text= it.foodPrices?.firstOrNull()?:""
                val image = it.foodImages?.firstOrNull()?:""
                val uri = Uri.parse(image)
                Glide.with(requireContext()).load(uri).into(buyImg)

                listOfOrderItem.reverse()
                if(listOfOrderItem.isNotEmpty()){

                }
            }
        }
    }

    private fun setPreviousBuyItems() {
        val buyFood = mutableListOf<String>()
        val buyPrice =mutableListOf<String>()
        val buyImage  = mutableListOf<String>()
        for(i in 1 until listOfOrderItem.size){
            listOfOrderItem[i].foodName?.firstOrNull()?.let { buyFood.add(it) }
            listOfOrderItem[i].foodPrices?.firstOrNull()?.let { buyPrice.add(it) }
            listOfOrderItem[i].foodImages?.firstOrNull()?.let { buyImage.add(it) }
        }
        val rv = binding.historyRecy
        rv.layoutManager = LinearLayoutManager(requireContext())
        buyAgainAdapter = BuyAgainAdapter(buyFood,buyPrice,buyImage,requireContext())
    }

    private fun setUpRecyclerView(){
        // before model
//        val buyFood = arrayListOf("Burger","Sandwitch","momo","samosa","xyz","jhhh")
//        val buyPrice = arrayListOf("$4","$2","$5","$5","$9","$2")
//        val buyImage  = arrayListOf(R.drawable.menu1,R.drawable.menu2,R.drawable.menu3,
//            R.drawable.menu4,R.drawable.menu5,R.drawable.menu6)
//
//        buyAgainAdapter = BuyAgainAdapter(buyFood,buyPrice,buyImage)
//        binding.historyRecy.layoutManager = LinearLayoutManager(requireContext())
//        binding.historyRecy.adapter = buyAgainAdapter
    }

    companion object {

    }
}