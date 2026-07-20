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
import com.example.cookbook.databinding.LanguageAlertDialogBinding


object DialogUtil {

    fun showLanguageDialog(
        context: Context,
        layoutInflater: LayoutInflater,
        onSave: (String) -> Unit
    ){
        val binding = LanguageAlertDialogBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(context).setView(binding.root).create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        binding.buttonSave.setOnClickListener {
          val checkedId = binding.radioGroup.checkedRadioButtonId

          if(checkedId != -1){
              val radioButton = binding.root.findViewById<RadioButton>(checkedId)
              onSave(radioButton.text.toString())
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
        onSave: (String) -> Unit
    ){

        val binding = CuisineAlertDialogBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(context).setView(binding.root).create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        binding.buttonSave.setOnClickListener {
            val checkedId = binding.radioGroup.checkedRadioButtonId

            if(checkedId != -1){
                val radioButton = binding.root.findViewById<RadioButton>(checkedId)
                onSave(radioButton.text.toString())
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
    ){

        val binding = AboutAlertDialogBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(context).setView(binding.root).create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        binding.buttonOk.setOnClickListener {
            dialog.dismiss()
        }

    }
}