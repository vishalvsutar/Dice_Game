package com.example.dicegame

import java.util.*
import kotlin.random.Random.Default.nextInt

data class Die(val numberOfSide: Int) {



    lateinit var name: String
//    private var numberOfSides: Int = 1
    private var currentSideUp: Int = 1

    public fun getCurrentSideUp(): Int  //getter
    {
return currentSideUp
    }


    init {
        currentSideUp = roll(numberOfSide)
    }


    private fun roll(numberOfSides: Int): Int { //function to roll the dice

        currentSideUp =
            (((Math.random() * numberOfSides) + 1).toInt()) //random method used generate random values for the dice when rolled
        return currentSideUp
    }
}