package com.example.foodorderingapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingapp.DetailsActivity
import com.example.foodorderingapp.databinding.PopularItemlistBinding


class PopularAdapter(private val item :List<String>,private val price: List<String>,private val image :List<Int>,private val requireContext:Context): RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {
    class PopularViewHolder(private val binding:PopularItemlistBinding):RecyclerView.ViewHolder(binding.root) {
       private  val imageView = binding.popularImg
        fun bind(item: String, images: Int,price:String) {
            binding.populatItem.text = item
            binding.popularPrice.text = price
            imageView.setImageResource(images)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
       return PopularViewHolder(PopularItemlistBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        val item = item[position]
        val images = image[position]
        val price = price[position]
        holder.bind(item,images,price)
        holder.itemView.setOnClickListener {
            val intent = Intent(requireContext, DetailsActivity::class.java)
            intent.putExtra("menuItemName",item)
            intent.putExtra("menuItemImg",images)
            requireContext.startActivity(intent)
        }
    }
}