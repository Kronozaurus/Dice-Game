package com.example.lab01

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(localClassName, "onCreate")
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.rollButton)

        button.setOnClickListener {
            rollDice()
        }
    }

    private fun rollDice() {
        val dice = Dice()
        val roll = dice.roll()
        val roll2 = dice.roll(false)
        val crash = findViewById<ConstraintLayout>(R.id.crash_test)
        val screenH = crash?.height
        updateText(roll, roll2)
        updateImg(roll, roll2)
    }

    private fun updateImg(roll: Int, roll2: Int) {
        val dice1Img: ImageView = findViewById(R.id.dice1Img)
        val dice2Img: ImageView = findViewById(R.id.dice2Img)
        dice1Img.setImageResource(resolveDrawable(roll))
        dice2Img.setImageResource(resolveDrawable(roll2))
    }

    private fun resolveDrawable(value: Int): Int {
        return when (value) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            6 -> R.drawable.dice_6
            else -> R.drawable.dice_1
        }
    }

    private fun updateText(roll: Int, roll2: Int) {
        val rollResultTxt = findViewById<TextView>(R.id.rollResultText)
        rollResultTxt.text = "Rolled: ${roll} & ${roll2}"
        val r: Int = (0..255).random()
        val g: Int = (((roll + roll2) / 12.0) * 255).toInt()
        val b: Int = (((roll * roll2) / 36.0) * 255).toInt()
        rollResultTxt.setTextColor(Color.rgb(r, g, b))
        Log.i(localClassName, "Text color: R:$r, G:$g, B:$b")
    }

    override fun onStart() {
        super.onStart()
        Log.i(localClassName, "onStart")
    }
    override fun onResume() {
        super.onResume()
        Log.i(localClassName, "onResume")
    }
    override fun onPause() {
        super.onPause()
        Log.i(localClassName, "onPause")
    }
    override fun onStop() {
        super.onStop()
        Log.i(localClassName, "onStop")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i(localClassName, "onDestroy")
    }
}