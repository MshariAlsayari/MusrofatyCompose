package com.msharialsayari.musrofaty.ui_component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.msharialsayari.musrofaty.R

@Composable
fun SortedByBottomSheet(
    modifier: Modifier = Modifier,
    selectedSortedByAmount:SortedByAmount? =null,
    onSelectAmount:(SortedByAmount) ->Unit,
){

    Column(
        modifier = modifier
    ) {

        TextComponent.HeaderText(
            text = stringResource(id = R.string.common_sorted_by),
            modifier = Modifier.padding(dimensionResource(id = R.dimen.default_margin16))
        )

        DividerComponent.HorizontalDividerComponent()

        SortedByAmount.entries.map {
            RadioButtonComponent(
                value = stringResource(id = it.value),
                selected = selectedSortedByAmount == it,
                onSelect = {
                    onSelectAmount(it)
                }
            )
        }

    }

}

@Composable
private fun RadioButtonComponent(
    modifier: Modifier=Modifier,
    value:String,
    selected:Boolean = false,
    onSelect:()->Unit
){

    Row (
        modifier =modifier.fillMaxWidth().clickable {
            onSelect()
        },
        verticalAlignment = Alignment.CenterVertically
    ){
        RadioButton(selected = selected, onClick = onSelect)
        TextComponent.BodyText(
            text = value,
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.default_margin16))
        )
    }


}


enum class SortedByAmount(val value:Int){
    HIGHEST(R.string.sorted_by_highest),LEAST(R.string.sorted_by_least)
}
