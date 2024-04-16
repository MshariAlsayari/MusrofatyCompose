package com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen.CategorySmsListViewModel
import com.msharialsayari.musrofaty.ui_component.TextComponent

@Composable
fun CategorySmsListTabs(
    modifier: Modifier = Modifier,
    viewModel: CategorySmsListViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val tabIndex = uiState.selectedTabIndex
    Column(modifier) {
        ScrollableTabRow(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.background,
            selectedTabIndex = tabIndex,
            edgePadding = 0.dp,
            indicator = {
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(it[tabIndex]),
                    color = MaterialTheme.colors.secondary,
                    height = TabRowDefaults.IndicatorHeight
                )
            }
        ) {
            CategorySmsListScreenTabs.entries.forEachIndexed { index, tab ->
                Tab(
                    modifier = Modifier.background(MaterialTheme.colors.background),
                    selected = tabIndex == index,
                    onClick = { viewModel.onTabSelected(index) },
                    text = {
                        TextComponent.ClickableText(
                            text = stringResource(id = tab.title),
                            color = if (tabIndex == index) MaterialTheme.colors.secondary else colorResource(
                                id = R.color.light_gray
                            )
                        )
                    })
            }
        }
        when (CategorySmsListScreenTabs.getTabByIndex(tabIndex)) {
            CategorySmsListScreenTabs.ALL -> AllSmsTab(viewModel)
            CategorySmsListScreenTabs.FAVORITE -> FavoriteSmsTab(viewModel)
            CategorySmsListScreenTabs.DELETED -> SoftDeletedTab(viewModel)
            CategorySmsListScreenTabs.FINANCIAL -> FinancialStatisticsTab(viewModel)
        }
    }


}