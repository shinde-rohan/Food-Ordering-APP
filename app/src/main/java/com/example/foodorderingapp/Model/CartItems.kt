package com.example.foodorderingapp.Model

import android.media.Image

data class CartItems(
    var foodName : String? = null,
    var foodPrice : String? = null,
    var foodDescription : String? = null,
    var foodImage: String? = null,
    var foodQuantity : Int? = null,
    var foodIngredient : String? = null,
)
