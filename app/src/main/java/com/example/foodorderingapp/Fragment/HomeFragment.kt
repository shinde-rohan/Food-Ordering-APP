package com.example.foodorderingapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.foodorderingapp.Adapter.MenuAdapter
import com.example.foodorderingapp.Adapter.PopularAdapter
import com.example.foodorderingapp.Model.MenuItem1
import com.example.foodorderingapp.R
import com.example.foodorderingapp.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var database : FirebaseDatabase
    private lateinit var menuItems : MutableList<MenuItem1>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        binding.viewMenu.setOnClickListener {
            val bottomSheetDialog = BottomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager,"test")
        }

        // retrive and display popular fooditems
        retriveAndDisplayPoplarItem()
        return binding.root


    }

    private fun retriveAndDisplayPoplarItem() {
        // get reference to the database
        database = FirebaseDatabase.getInstance()
        val foodRef = database.reference.child("menu")
        menuItems = mutableListOf()
        //retrive menu item from database
        foodRef.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(foodSnapshot in snapshot.children){
                    val menuItem = foodSnapshot.getValue(MenuItem1::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                // dispaly random popular items
                randomPopularItems()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun randomPopularItems() {
        // creating random shuffle list
        val index = menuItems.indices.toList().shuffled()
        val numOfItems = 6
        val subSetMenuItem:List<MenuItem1> = index.take(numOfItems).map { menuItems[it] }
        // setPopularAdapter
        setAdapter(subSetMenuItem)
    }

    private fun setAdapter(subSetMenuItem: List<MenuItem1>) {
        val  adapter = MenuAdapter(subSetMenuItem,requireContext())
        binding.popularRcy.layoutManager = LinearLayoutManager(context);
        binding.popularRcy.adapter = adapter
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner3, ScaleTypes.FIT))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList,ScaleTypes.FIT)

        imageSlider.setItemClickListener(object : ItemClickListener{
            override fun doubleClick(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(position: Int) {
                val itemPosition = imageList[position]
                val itemMeassage = "Selected image $position"
                Toast.makeText(requireContext(),itemMeassage,Toast.LENGTH_LONG).show()
            }
        })
        // before firebase and model
//        val foodName = listOf("Burger","SandWitch","Momos","item")
//        val price = listOf("$5","$6","$8","$9")
//        val popularImg = listOf(R.drawable.menu1,R.drawable.menu2,R.drawable.menu3,R.drawable.menu4)
//        val adapter = PopularAdapter(foodName,price,popularImg,requireContext())
//        binding.popularRcy.layoutManager = LinearLayoutManager(context);
//        binding.popularRcy.adapter = adapter
    }

    companion object {

    }
}