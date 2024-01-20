package com.msharialsayari.musrofaty.ui_component

import androidx.annotation.DrawableRes
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.DateUtils.DEFAULT_DATE_PATTERN
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
                        tint =  MusrofatyTheme.colors.iconBackgroundColor,
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
    fun ExpandableCategoryStatisticsRow(
        modifier: Modifier = Modifier,
        model: CategoryStatisticsModel,
        onClick: (String) -> Unit
    ) {
        
        val expanded = remember { mutableStateOf(false) }
        
        Column(modifier = modifier) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.default_margin16))
                    .clickable {
                        expanded.value = !expanded.value
                    }
                ,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(RectangleShape)
                        .background(Color(model.color))
                )

                TextComponent.PlaceholderText(
                    text = model.category
                )

                TextComponent.PlaceholderText(
                    text = StringsUtils.formatNumberWithComma(model.totalAmount.toString())
                )

                TextComponent.PlaceholderText(
                    text = StringsUtils.formatDecimalNumber(model.percent) + " %"
                )

                TextComponent.PlaceholderText(
                    text = model.currency
                )
                if (expanded.value)
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = null,
                    )
                else
                    Icon(
                        modifier = Modifier.mirror(),
                        painter = painterResource(id = R.drawable.ic_collapsed_arrow),
                        contentDescription = null
                    )

            }


            ExpandableContent(visible = expanded.value, initialVisibility = expanded.value, view = { CategoryDetailsStatisticsList(model.smsList, onItemClicked = onClick) })

        }
    }


    @Composable
    fun CategoryStatisticsRow(
        modifier: Modifier = Modifier,
        model: CategoryStatisticsModel)  {

        val openDialog = rememberSaveable{ mutableStateOf(false) }

        if (openDialog.value){
            DialogComponent.MessageDialog(
                message = R.string.no_currency_message,
                onDismiss = {
                    openDialog.value = false
                })
        }


        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.default_margin16)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(RectangleShape)
                    .background(Color(model.color))
            )

            TextComponent.PlaceholderText(
                modifier = Modifier.padding(start = 8.dp).weight(2f),
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
                    modifier = Modifier.weight(1f).clickable {
                        openDialog.value = true
                    },
                    text = stringResource(id = R.string.common_click),
                    alignment = TextAlign.End
                )
            } else {
                TextComponent.PlaceholderText(
                    modifier = Modifier.weight(1f),
                    text = model.currency,
                    alignment = TextAlign.End
                )
            }
        }

    }
    
    @Composable
    fun CategoryDetailsStatisticsList(list:List<SmsModel>, onItemClicked: (String) -> Unit){
        val listState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier.height(80.dp),
            state = listState,
        ) {

            itemsIndexed(list) { index,item ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.default_margin10))
                        .clickable { onItemClicked(item.id) },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    TextComponent.PlaceholderText(
                        modifier = Modifier.weight(1f),
                        alignment = TextAlign.Center,
                        enableEllipsis =true,
                        text = item.storeName
                    )

                    TextComponent.PlaceholderText(
                        modifier = Modifier.weight(1f),
                        alignment = TextAlign.Center,
                        enableEllipsis =true,
                        text = StringsUtils.formatNumberWithComma(item.amount.toString())
                    )

                    TextComponent.PlaceholderText(
                        modifier = Modifier.weight(1f),
                        alignment = TextAlign.Center,
                        enableEllipsis =true,
                        text = DateUtils.getDateByTimestamp(
                            item.timestamp,
                            pattern = DEFAULT_DATE_PATTERN
                        ) ?: ""
                    )

                    TextComponent.PlaceholderText(
                        modifier = Modifier.weight(1f),
                        alignment = TextAlign.Center,
                        enableEllipsis =true,
                        text = item.currency
                    )
                }

                if (index !=list.lastIndex )
                    DividerComponent.HorizontalDividerComponent()
            }


        }
        
    }


    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun ExpandableContent(
        visible: Boolean = true,
        initialVisibility: Boolean = false,
        view: @Composable () -> Unit

    ) {
        val enterTransition = remember {
            expandVertically(
                expandFrom = Alignment.Top,
                animationSpec = tween(EXPANSTION_TRANSITION_DURATION)
            ) + fadeIn(
                initialAlpha = 0.3f,
                animationSpec = tween(EXPANSTION_TRANSITION_DURATION)
            )
        }
        val exitTransition = remember {
            shrinkVertically(
                // Expand from the top.
                shrinkTowards = Alignment.Top,
                animationSpec = tween(EXPANSTION_TRANSITION_DURATION)
            ) + fadeOut(
                // Fade in with the initial alpha of 0.3f.
                animationSpec = tween(EXPANSTION_TRANSITION_DURATION)
            )
        }
        AnimatedVisibility(
            visible = visible,
            initiallyVisible = initialVisibility,
            enter = enterTransition,
            exit = exitTransition
        ) {
            view()
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

//data class CategoryDetailsStatisticsModel(
//    var smsId: String = "",
//    var storeName: String = "",
//    var storeAndCategoryModel: StoreAndCategoryModel?= null,
//    var amount: Double = 0.0,
//    var currency: String = "",
//    var timestamp:Long= 0
//)