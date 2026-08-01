package com.example.cookbook.util

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.R
import com.example.cookbook.databinding.AboutAlertDialogBinding
import com.example.cookbook.databinding.CuisineAlertDialogBinding
import com.example.cookbook.databinding.IngredientAlertDialogBinding
import com.example.cookbook.databinding.LanguageAlertDialogBinding


object DialogUtil {

    fun showLanguageDialog(
        context: Context,
        layoutInflater: LayoutInflater,
        currentLanguage: String,
        onSave: (String) -> Unit
    ) {
        val binding = LanguageAlertDialogBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(context).setView(binding.root).create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        binding.rbEnglish.isChecked = true

        when(currentLanguage){
            "en" -> binding.rbEnglish.isChecked = true
            "de" -> binding.rbGerman.isChecked = true
            "tr" -> binding.rbTurkish.isChecked = true
            "fr" -> binding.rbFrench.isChecked = true
            "zh" -> binding.rbChinese.isChecked = true
            "ru" -> binding.rbRussian.isChecked = true
        }

        binding.buttonSave.setOnClickListener {
            val checkedId = binding.radioGroup.checkedRadioButtonId

            if (checkedId != -1) {
                val radioButton = binding.root.findViewById<RadioButton>(checkedId)
                onSave(radioButton.tag.toString())
            }

            dialog.dismiss()
        }

        binding.buttonCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun showCuisineDialog(
        context: Context,
        layoutInflater: LayoutInflater,
        currentCuisine: String,
        onSave: (String) -> Unit
    ) {

        val binding = CuisineAlertDialogBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(context).setView(binding.root).create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        when(currentCuisine){
            "Italian" -> binding.rbItalian.isChecked = true
            "Turkish" -> binding.rbTurkish.isChecked = true
            "Japanese" -> binding.rbJapanese.isChecked = true
            "Moroccan" -> binding.rbMoroccan.isChecked = true
            "Chinese" -> binding.rbChinese.isChecked = true
            "Russian" -> binding.rbRussian.isChecked = true
            "Thai" -> binding.rbThai.isChecked = true
            "Irish" -> binding.rbIrish.isChecked = true
            "Greek" -> binding.rbGreek.isChecked = true
            "Vietnamese" -> binding.rbVietnamese.isChecked = true
            "" -> {} // It is a normal behavior for new  users. No need to take action for that situation.

        }

        binding.buttonSave.setOnClickListener {
            val checkedId = binding.radioGroup.checkedRadioButtonId

            if (checkedId != -1) {
                val radioButton = binding.root.findViewById<RadioButton>(checkedId)
                onSave(radioButton.tag.toString())
            }

            dialog.dismiss()
        }

        binding.buttonCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun showIngredientsDialog(
        context: Context,
        layoutInflater: LayoutInflater,
        currentIngredient: String,
        onSave: (String) -> Unit
    ) {
        val binding = IngredientAlertDialogBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(context).setView(binding.root).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        when(currentIngredient){
            "Egg" -> binding.rbEgg.isChecked = true
            "Beef" -> binding.rbBeef.isChecked = true
            "Bread" -> binding.rbBread.isChecked = true
            "Sugar" -> binding.rbSugar.isChecked = true
            "Cheese" -> binding.rbCheese.isChecked = true
            "Cocoa" -> binding.rbCocoa.isChecked = true
            "Milk" -> binding.rbMilk.isChecked = true
            "Honey" -> binding.rbHoney.isChecked = true
            "Carrots" -> binding.rbCarrots.isChecked = true
            "Cucumber" -> binding.rbCucumber.isChecked = true
            "" -> {} // It is a normal behavior for new  users. No need to take action for that situation.
        }

        binding.buttonSave.setOnClickListener {
            val checkedId = binding.radioGroup.checkedRadioButtonId

            if (checkedId != -1) {
                val radioButton = binding.root.findViewById<RadioButton>(checkedId)
                onSave(radioButton.tag.toString())
            }
            dialog.dismiss()
        }

        binding.buttonCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun showAboutDialog(
        context: Context,
        layoutInflater: LayoutInflater,
    ) {

        val binding = AboutAlertDialogBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(context).setView(binding.root).create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        binding.buttonOk.setOnClickListener {
            dialog.dismiss()
        }

    }
}