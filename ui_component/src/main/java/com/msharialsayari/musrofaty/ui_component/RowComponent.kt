package com.msharialsayari.musrofaty.ui_component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

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
        iconId: Int,
        header: String = "",
        body: String = "",
        onClick: () -> Unit = {}
    ) {

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
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = ""
                )


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
}