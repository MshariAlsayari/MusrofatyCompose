package com.msharialsayari.musrofaty.ui_component

import androidx.compose.runtime.Composable
import com.google.android.material.datepicker.MaterialDatePicker
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

object DatePickerComponent {


    @Composable
    fun RangeDatePicker(){
        val dialogState = rememberMaterialDialogState()
        MaterialDialog(
            dialogState = dialogState,
            buttons = {
                positiveButton("Ok")
                negativeButton("Cancel")
            }
        ) {
            datepicker { date ->


            }

        }


        dialogState.show()

    }
}