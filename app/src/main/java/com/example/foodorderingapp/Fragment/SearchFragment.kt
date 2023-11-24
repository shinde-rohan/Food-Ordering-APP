package com.example.foodorderingapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderingapp.Adapter.MenuAdapter
import com.example.foodorderingapp.R
import com.example.foodorderingapp.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {
    private lateinit var binding : FragmentSearchBinding
    private lateinit var adapter : MenuAdapter
    private val searchFoodName = listOf("Burger","Sandwitch","momo","samosa","xyz","jhhh")
   private val searchPrice = listOf("$4","$2","$5","$5","$9","$2")
   private val searchImage  = listOf(R.drawable.menu1,R.drawable.menu2,R.drawable.menu3,
        R.drawable.menu4,R.drawable.menu5,R.drawable.menu6)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private val filterMenuFoodName = mutableListOf<String>()
    private val filterMenuPrice = mutableListOf<String>()
    private val filterMenuImage = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater,container,false)
//        adapter = MenuAdapter(filterMenuFoodName,filterMenuPrice,filterMenuImage,requireContext()
//        )
        binding.searchRecy.layoutManager = LinearLayoutManager(requireContext())
        binding.searchRecy.adapter = adapter

        //setting up searchview
        setUpSearchView()
        //show all menu item
        showAllMenuItem()
        return binding.root
    }

    private fun showAllMenuItem() {
        filterMenuFoodName.clear()
        filterMenuPrice.clear()
        filterMenuImage.clear()

        filterMenuFoodName.addAll(searchFoodName)
        filterMenuPrice.addAll(searchPrice)
        filterMenuImage.addAll(searchImage)

        adapter.notifyDataSetChanged()
    }

    private fun setUpSearchView() {
        binding.searchbar.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                filterMenuItem(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterMenuItem(newText)
                return  true
            }
        })
    }

    private fun filterMenuItem(query: String) {
        filterMenuFoodName.clear()
        filterMenuPrice.clear()
        filterMenuImage.clear()

        searchFoodName.forEachIndexed { index, foodName ->
            if(foodName.contains(query,ignoreCase = true)){
                filterMenuFoodName.add(foodName)
                filterMenuPrice.add(searchPrice[index])
                filterMenuImage.add(searchImage[index])
            }
        }
        adapter.notifyDataSetChanged()
    }

    companion object {

    }
}