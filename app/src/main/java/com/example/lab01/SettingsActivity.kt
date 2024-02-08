package com.example.lab01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lab01.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val numDice = intent.getIntExtra(getString(R.string.num_dice_key), 2)
        val isHoldEnabled = intent.getBooleanExtra((getString(R.string.hold_enable_key)), true)

        binding.enableHoldSwitch.isChecked = isHoldEnabled

        if(numDice in 2 .. 5)
            binding.numDiceSpinner.setSelection(numDice - 2)

        binding.confirmButton.setOnClickListener() {
            val spinnerSelection = binding.numDiceSpinner.selectedItem.toString().toInt()
            val holdEnable = binding.enableHoldSwitch.isChecked
            val result = Intent().apply {
                putExtra(getString(R.string.num_dice_key), spinnerSelection)
                putExtra(getString(R.string.hold_enable_key), holdEnable)
            }
            setResult(RESULT_OK, result)
            finish()
        }
    }
}