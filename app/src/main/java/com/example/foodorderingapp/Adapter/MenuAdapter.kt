package com.example.foodorderingapp.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderingapp.DetailsActivity
import com.example.foodorderingapp.Model.MenuItem1
import com.example.foodorderingapp.databinding.MenuItemBinding

class MenuAdapter(
    // for sample without model
//    private val menuItems:MutableList<String>,
//    private val menuPrices:MutableList<String>,
//    private val menuImages : MutableList<Int>,
//    private val requireContext: Context

    // using model
    val menuItems: List<MenuItem1>,
    private val requireContext: Context
        ):RecyclerView.Adapter<MenuAdapter.MenuViewHolder>(){
//   private val itemClickListner : OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuAdapter.MenuViewHolder {
       val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuAdapter.MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
       return menuItems.size
    }
    inner class MenuViewHolder(private val binding : MenuItemBinding):RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
//                    itemClickListner?.onItemClick(position)
                    openDetailsActivity(position)
                }
                //setOnclicklistener to open detail
//                val intent = Intent(requireContext,DetailsActivity::class.java)
//                intent.putExtra("menuItemName",menuItems.get(position))
//                intent.putExtra("menuItemImg",menuImages.get(position))
//                requireContext.startActivity(intent)
            }
        }

        private fun openDetailsActivity(position: Int) {
            val itemMenu = menuItems[position]
            val intent = Intent(requireContext,DetailsActivity::class.java).apply {
                putExtra("MenuItemName",itemMenu.foodName)
                putExtra("MenuItemPrice",itemMenu.price)
                putExtra("MenuItemDescription",itemMenu.description)
                putExtra("MenuItemIngredients",itemMenu.ingredients)
                putExtra("MenuItemImage",itemMenu.image)
            }
            requireContext.startActivity(intent)

        }


        fun bind(position: Int) {
            //set data into recyclerview
            val itemMenu = menuItems[position]
            binding.apply {
                menuName.text = itemMenu.foodName
                menuPrice.text = itemMenu.price
                val uri = Uri.parse(itemMenu.image)
                Glide.with(requireContext).load(uri).into(menuImg)

            }
        }

    }



//    interface OnClickListener {
//        fun onItemClick(position: Int){
//
//        }
//    }
}



