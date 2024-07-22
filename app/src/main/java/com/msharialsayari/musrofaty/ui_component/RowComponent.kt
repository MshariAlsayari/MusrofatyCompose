package com.msharialsayari.musrofaty.ui_component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreAndCategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.utils.StringsUtils
import com.msharialsayari.musrofaty.utils.mirror

const val EXPANSTION_TRANSITION_DURATION = 450
const val EXPAND_ANIMATION_DURATION = 450

object RowComponent {


    @Composable
    fun SenderRow(displayName: String = "", totalSms: Int = 0, onClick: () -> Unit = {}) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.row_height100))
                .clickable {
                    onClick()
                },
        ) {

            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = dimensionResource(id = R.dimen.default_margin16)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    TextComponent.HeaderText(
                        text = displayName
                    )

                    TextComponent.PlaceholderText(
                        text = stringResource(id = R.string.number_of_sms) + ": " + totalSms.toString()
                    )
                }

                Icon(
                    painter = painterResource(id = R.drawable.ic_navigation),
                    contentDescription = ""
                )

            }

        }

    }


    @Composable
    fun PreferenceRow(
        modifier: Modifier=Modifier,
        @DrawableRes iconId: Int?=null,
        header: String = "",
        body: String = "",
        onClick: () -> Unit = {}
    ) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.row_height100))
                .clickable {
                    onClick()
                },
        ) {

            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = dimensionResource(id = R.dimen.default_margin16)),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                iconId?.let {
                    Icon(
                        painter = painterResource(id = iconId),
                        tint =  MusrofatyTheme.colors.onBackgroundIconColor,
                        contentDescription = ""
                    )
                }


                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(dimensionResource(id = R.dimen.default_margin16)),
                    verticalArrangement = Arrangement.Center
                ) {
                    TextComponent.HeaderText(
                        text = header
                    )

                    TextComponent.PlaceholderText(
                        text = body
                    )
                }


            }

        }

    }




    @Composable
    fun CategoryStatisticsRow(
        modifier: Modifier = Modifier,
        model: CategoryStatisticsModel,
        onClicked:(CategoryStatisticsModel)->Unit
        )  {

        val openDialog = rememberSaveable{ mutableStateOf(false) }

        if (openDialog.value){
            DialogComponent.MessageDialog(
                message = R.string.no_currency_message,
                onDismiss = {
                    openDialog.value = false
                })
        }


        Column (
            modifier = modifier
                .fillMaxSize()
                .clickable {
                    onClicked(model)
                }
        ){


            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.default_margin16)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(RectangleShape)
                        .background(Color(model.color))
                )

                TextComponent.PlaceholderText(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(2f),
                    text = model.category,
                    alignment = TextAlign.Start
                )

                TextComponent.PlaceholderText(
                    modifier = Modifier.weight(1f),
                    text = StringsUtils.formatNumberWithComma(model.totalAmount.toString()),
                    alignment = TextAlign.Center
                )

                TextComponent.PlaceholderText(
                    modifier = Modifier.weight(1f),
                    text = StringsUtils.formatDecimalNumber(model.percent) + " %",
                    alignment = TextAlign.Center
                )

                if (model.currency.isEmpty()) {
                    TextComponent.ClickableText(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                openDialog.value = true
                            },
                        text = stringResource(id = R.string.common_click),
                        alignment = TextAlign.End
                    )
                } else {
                    TextComponent.PlaceholderText(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .weight(1f),
                        text = model.currency,
                        alignment = TextAlign.End
                    )
                }

                Icon(
                    Icons.Default.KeyboardArrowRight,
                    modifier = Modifier
                        .mirror(),
                    contentDescription = null
                )
            }

            DividerComponent.HorizontalDividerComponent()
        }

    }

    @Composable
    fun FilterWordRow(
        modifier: Modifier = Modifier,
        title: String,
        value: String? = null,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextComponent.BodyText(
                modifier = Modifier.weight(1f),
                text = title
            )

            if (value != null)
                TextComponent.ClickableText(
                    modifier = Modifier.weight(.5f),
                    text = value,
                    alignment = TextAlign.End

                )


        }


    }
    


}


data class CategoryStatisticsModel(
    var color: Int = 0,
    var category: String = "",
    var storeAndCategoryModel: StoreAndCategoryModel?= null,
    var totalAmount: Double = 0.0,
    var percent: Double = 0.0,
    var currency: String = "",
    var smsList: List<SmsModel> = emptyList()
)
