package com.example.foodorderingapp.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderingapp.Adapter.MenuAdapter
import com.example.foodorderingapp.Model.MenuItem1
import com.example.foodorderingapp.databinding.FragmentBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class BottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetBinding
    private lateinit var database : FirebaseDatabase
    private lateinit var menuItems : MutableList<MenuItem1>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetBinding.inflate(layoutInflater, container, false)
        binding.buttonBack.setOnClickListener {
            dismiss()
        }
        // for only showing without model
//        val menuFood = listOf("Burger","Sandwitch","momo","samosa","xyz","jhhh")
//        val menuPrice = listOf("$4","$2","$5","$5","$9","$2")
//        val menuImage  = listOf(R.drawable.menu1,R.drawable.menu2,R.drawable.menu3,
//            R.drawable.menu4,R.drawable.menu5,R.drawable.menu6)
        // using model
        retriveMenuItem()
//        val adapter = MenuAdapter(ArrayList(menuFood),ArrayList(menuPrice),ArrayList(menuImage),requireContext())
//        binding.menuRecy.layoutManager = LinearLayoutManager(requireContext())
//        binding.menuRecy.adapter = adapter
        return binding.root
    }

    private fun retriveMenuItem() {
        database = FirebaseDatabase.getInstance()
        val foodRef = database.reference.child("menu")
        menuItems = mutableListOf()

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(foodSnapshot in snapshot.children){
                    val menuItem = foodSnapshot.getValue(MenuItem1::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                // once data recive  then set to adapter
                Log.d("items","ondataChange :data received")
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setAdapter() {
        if(menuItems.isNotEmpty()){
            val adapter = MenuAdapter(menuItems,requireContext())
            binding.menuRecy.layoutManager = LinearLayoutManager(requireContext())
            binding.menuRecy.adapter = adapter
            Log.d("items","setAdapter:data set")
        }else{
            Log.d("items","setAdapter:data set not set")
        }

    }

    companion object {

    }
}