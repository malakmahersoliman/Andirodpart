package com.example.milkchequedemo.domain.model

object MyCart {

    val cart: MutableList<CartModel> = mutableListOf()


    fun addItem(item: CartModel){
        cart.add(item)
    }

    fun removeItem(item: CartModel){
        cart.remove(item)
    }

    fun update(item: CartModel){
        cart.remove(
            cart.first{
                it.name==item.name
            }
        )
        if(item.qnt!=0) {
            cart.add(item)
        }
    }

    fun clear(){
        cart.clear()
    }

}

data class CartModel(
    val id: Long,
    val name: String,
    val price: Double,
    val description: String?,
    val iconUrl: String?,
    val qnt: Int
)