package com.example.lab01

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lab01.databinding.ActivityMainExtendedBinding
import com.google.android.material.snackbar.Snackbar

class MainActivityExtended : AppCompatActivity() {

    private lateinit var binding: ActivityMainExtendedBinding
    private var numDice: Int = 2
    private var isHoldEnabled: Boolean = true
    private val diceImgIdsArray =
        arrayOf(R.id.dice1ImgExt, R.id.dice2ImgExt, R.id.dice3ImgExt, R.id.dice4ImgExt, R.id.dice5ImgExt)
    private val diceStatesArray =
        arrayOf(false, false, false, false, false)
    private val diceValuesArray =
        arrayOf(1, 1, 1, 1, 1)
    private var currentPlayer = 0
    private val playerScores =
        arrayOf(0, 0)
    private var rollCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(localClassName, "OnCreate")
        binding = ActivityMainExtendedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        applySettings()

        binding.rollButtonExt.setOnClickListener {
            if (binding.rollButtonExt.text != getString(R.string.button_enabled)) {// button enabled?
                binding.rollButtonExt.text = getString(R.string.button_enabled)
                if(currentPlayer == 1 && rollCount == 0) {
                    resetTurn()
                }
                else {
                    binding.playerLabel.apply {
                        text = getString(R.string.player_label_format, currentPlayer)
                        visibility = View.VISIBLE
                    }
                }
                binding.rollResultTextExt.text = getString(R.string.click_roll)

                if(isHoldEnabled)
                    setDiceClick()
            } else {
                rollDices()
            }
        }
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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> startSettingsActivity()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun startSettingsActivity() {
        val intent: Intent = Intent(this, SettingsActivity::class.java).apply {
            putExtra(getString(R.string.num_dice_key),numDice)
            putExtra(getString(R.string.hold_enable_key), isHoldEnabled)
        }
        launchSettingsActivity.launch(intent)
    }

    private val launchSettingsActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.i(localClassName, "onActivityResult")
            if(result.resultCode == RESULT_OK) {
                result.data?.let { data ->
                    numDice = data.getIntExtra(getString(R.string.num_dice_key), 2)
                    isHoldEnabled = data.getBooleanExtra(getString(R.string.hold_enable_key), true)
                }
                applySettings()
                resetGame()
                Snackbar.make(
                    binding.root,
                    "Current settings: numDice: $numDice, isHoldEnabled: $isHoldEnabled",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

        }

    private fun applySettings() {
        val diceToHideBegin = numDice + 1
        for(num in 1 .. 5) {
            if(num in diceToHideBegin .. 5)
                findViewById<ImageView>(diceImgIdsArray[num - 1]).apply {
                    visibility = View.GONE
                    isClickable = false
                    isFocusable = false
                }
            else
                findViewById<ImageView>(diceImgIdsArray[num - 1]).apply {
                    visibility = View.VISIBLE
                    isClickable = isHoldEnabled
                    isFocusable = isHoldEnabled

            }
        }
    }

    private fun resetGame() {
        currentPlayer = 0
        playerScores[0] = 0
        playerScores[1] = 0
        rollCount = 0
        binding.rollResultTextExt.text = getString(R.string.click_start)
        binding.playerLabel.visibility = View.INVISIBLE
        binding.rollButtonExt.text = getString(R.string.button)
        resetTurn()
    }

    private fun resetTurn() {
        for(num in 0 .. 4) {
            diceValuesArray[num] = 1
            diceStatesArray[num] = false
            findViewById<ImageView>(diceImgIdsArray[num]).let {
                changeDiceTint(it, false)
                it.setImageResource(resolveDrawable(1))
            }
        }
        binding.playerLabel.apply {
            text = getString(R.string.player_label_format, currentPlayer)
        }
    }

    private fun changeDiceTint(img: ImageView, highlight: Boolean) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            img.imageTintList =
                ColorStateList.valueOf(getColor(if(highlight) R.color.yellow else R.color.white))
        } else {
            @Suppress("DEPRECATION")
            img.imageTintList =
                ColorStateList.valueOf(resources.getColor(if(highlight) R.color.yellow else R.color.white))
        }
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

    private fun setDiceClick() {
        for ((diceIdx, id) in diceImgIdsArray.withIndex()) {
            findViewById<ImageView>(id).setOnClickListener {
                val img = it as ImageView
                diceStatesArray[diceIdx] = !diceStatesArray[diceIdx]
                changeDiceTint(img, diceStatesArray[diceIdx])
            }
        }
    }

    private fun rollDices() {
        val dice = Dice()

        for (i in 0 until numDice) {
            if (!diceStatesArray[i]) {
                diceValuesArray[i] = dice.roll()
            }
        }
        updateTextAndImages()
        rollCount += 1
        if (rollCount == numDice) {
            if (currentPlayer == 1) {
                endGame()
            } else {
                currentPlayer = 1
                binding.rollButtonExt.text = getString(R.string.second_start)
            }
            rollCount = 0
        }
    }

    private fun updateTextAndImages() {
        val rolledText = StringBuilder(getString(R.string.rolled_msg))
        var sum = 0
        for (i in 0 until numDice) {
            val rollValue = diceValuesArray[i]
            sum+=rollValue
            rolledText.append("$rollValue")
            if(i != numDice - 1)
                rolledText.append(", ")
            findViewById<ImageView>(diceImgIdsArray[i]).setImageResource(resolveDrawable(rollValue))
        }
        playerScores[currentPlayer] = sum
        rolledText.append("\nSum: $sum")
        binding.rollResultTextExt.text = rolledText
    }

    private fun endGame() {
        binding.rollButtonExt.isEnabled = false
        binding.playerLabel.visibility = View.INVISIBLE
        val winner = when {
            playerScores[0] == playerScores[1] -> -1
            playerScores[0] > playerScores[1] -> 0
            else -> 1
        }
        Snackbar.make(
            binding.root,
            if(winner != -1) {
                val points = playerScores[winner]
                "Player $winner wins with $points points"
            } else {
                val points = playerScores[0]
                "Draw with $points point"
            },
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Start over") {
            binding.rollButtonExt.isEnabled = true
            resetGame()
        }.show()
    }

}