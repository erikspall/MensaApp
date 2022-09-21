package de.erikspall.mensaapp.domain.utils

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.radiobutton.MaterialRadioButton
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.StringResEnum
import kotlin.streams.toList

object Dialogs {
    fun createSettingsRadioDialog(
        context: Context,
        inflater: LayoutInflater,
        title: String,
        message: String,
        items: List<StringResEnum>,
        selectedValue: StringResEnum,
        onSave: (StringResEnum) -> (Unit)
    ): MaterialAlertDialogBuilder {
        val dialogView = inflater.inflate(R.layout.dialog_settings, null)
        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radio_group)

        val radioButtons = items.stream().map { stringRes ->
            val rb = MaterialRadioButton(context)
            rb.text = context.getString(stringRes.getValue())
            radioGroup.addView(rb)
            rb
        }.toList()

        val selectedValueString = context.getString(selectedValue.getValue())

        radioGroup.check(radioButtons.first{
            it.text.toString() == selectedValueString
        }.id)

        return MaterialAlertDialogBuilder(context, R.style.SettingsMaterialAlertDialog)
            .setTitle(title)
            .setMessage(message)
            .setView(dialogView)
            .setNegativeButton("Zurück") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Speichern") { dialog, _ ->
                val checkedRadioButton = radioGroup.findViewById<RadioButton>(radioGroup.checkedRadioButtonId)
                val index = radioGroup.indexOfChild(checkedRadioButton)
                onSave(items[index])
            }
            .setIcon(AppCompatResources.getDrawable(context, R.drawable.ic_role))
    }
}