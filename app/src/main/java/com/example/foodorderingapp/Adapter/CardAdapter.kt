package com.example.foodorderingapp.Adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderingapp.databinding.CardItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CardAdapter(
    private val cardItems: MutableList<String>,
    private val cardItemPrice: MutableList<String>,
    private val cardDescription:MutableList<String>,
    private val cardImages:MutableList<String>,
    private val cardQuantity:MutableList<Int>,
    private val cardIngredient:MutableList<String>,

    private val context: Context
            ):RecyclerView.Adapter<CardAdapter.CardViewHolder>(){

    private  val auth = FirebaseAuth.getInstance()

    init{
        val databse = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid?:""
        val cardItemNumber = cardItems.size

        itemQuantities = IntArray(cardItemNumber){1}
        cardItemRef  = databse.reference.child("user").child(userId).child("CartItems")
    }
    companion object{
        private  var itemQuantities : IntArray = intArrayOf()
        private lateinit var cardItemRef :DatabaseReference
    }
//    private val itemQuantities = IntArray(CardItems.size)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = CardItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CardViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cardItems.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(position)
    }

    fun getUpdateItemsQuantities(): MutableList<Int> {
        val itemQuantity = mutableListOf<Int>()
        itemQuantity.addAll(cardQuantity)
        return itemQuantity
    }

    inner class CardViewHolder(private val binding: CardItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            Log.d("CardAdapter", "Position: $position, List Sizes: ${cardItems.size}, ${cardItemPrice.size}, ${cardDescription.size}, ${cardImages.size}, ${cardQuantity.size}, ${cardIngredient.size}")
            if (position >= 0 && position < cardItems.size) {
                binding.apply {
                    val quantities = itemQuantities[position]
                    cardFood.text = cardItems[position]
                    cardPrice.text = cardItemPrice[position]
//                cardImg.setImageResource(CardImages[position])
                    cardValue.text = quantities.toString()

                    // load image using glide
                    val uriString = cardImages[position]
                    val uri = Uri.parse(uriString)
                    Glide.with(context).load(uri).into(cardImg)

                    minus.setOnClickListener{
                        decreaseQuantity(position)
                    }
                    plus.setOnClickListener {
                        increaseQuantity(position)
                    }
                    cardDelete.setOnClickListener {
                        val itemPosition = adapterPosition
                        if(itemPosition != RecyclerView.NO_POSITION){
                            deleteItem(itemPosition)
                        }

                    }

                }
            }else {
                Log.d("CardAdapter", "Invalid position: $position")
            }

        }
       private fun decreaseQuantity(position: Int){
           if(itemQuantities[position]>1){
               itemQuantities[position]--
               cardQuantity[position] = itemQuantities[position]
               binding.cardValue.text = itemQuantities[position].toString()
           }
       }
       private fun increaseQuantity(position: Int){
           if(itemQuantities[position]<10){
               itemQuantities[position]++
               cardQuantity[position] = itemQuantities[position]
               binding.cardValue.text = itemQuantities[position].toString()
           }
       }
       private fun deleteItem(position: Int){
//           CardItems.removeAt(position)
//           CardItemPrice.removeAt(position)
//           CardImages.removeAt(position)
//           notifyItemRemoved(position)
//           notifyItemRangeChanged(position,CardItems.size)

           val positionRetrive = position
           getUniqueKeyAtPosition(positionRetrive){uniqueKey ->
               if (uniqueKey != null){
                   removeItem(position,uniqueKey)
               }
           }
       }

       private fun removeItem(position: Int, uniqueKey: String) {
           if(uniqueKey != null){
               cardItemRef.child(uniqueKey).removeValue().addOnSuccessListener {
                   if(position >= 0 && position<cardItems.size){
                       cardItems.removeAt(position)
                       cardDescription.removeAt(position)
                       cardImages.removeAt(position)
                       cardItemPrice.removeAt(position)
                       cardQuantity.removeAt(position)
                       cardIngredient.removeAt(position)
                       // update itemquantity
                       itemQuantities = itemQuantities.filterIndexed { index, i -> index!= position }.toIntArray()
                       notifyItemRemoved(position)
                       notifyItemRangeChanged(position,cardItems.size)
                       Toast.makeText(context,"Item deleted",Toast.LENGTH_SHORT).show()
                   }else{

                       Toast.makeText(context, "Invalid position", Toast.LENGTH_SHORT).show()

                   }


               }.addOnFailureListener {
                   Toast.makeText(context,"Failed to delete",Toast.LENGTH_SHORT).show()
               }
           }
       }

       private fun getUniqueKeyAtPosition(positionRetrive: Int,onComplete:(String?)->Unit) {
           cardItemRef.addListenerForSingleValueEvent(object :ValueEventListener{
               override fun onDataChange(snapshot: DataSnapshot) {
                   var uniqueKey : String? = null
                   snapshot.children.forEachIndexed{index, dataSnapshot ->
                       if(index == positionRetrive){
                           uniqueKey = dataSnapshot.key
                           return@forEachIndexed
                       }
                   }
                   onComplete(uniqueKey)
               }

               override fun onCancelled(error: DatabaseError) {
                   TODO("Not yet implemented")
               }

           })
       }
   }
}