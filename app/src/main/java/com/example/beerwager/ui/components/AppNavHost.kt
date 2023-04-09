package com.example.beerwager.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.beerwager.ui.view_model.WagerSearchViewModel
import com.example.beerwager.ui.view_model.WagersViewModel
import com.example.beerwager.utils.Dimen
import com.example.beerwager.utils.NavigationArgs

private const val ROUTE_WAGERS_LIST = "wagersList"
private const val ROUTE_WAGERS_SEARCH = "wagersSearch?{searchQuery}"
private fun getSearchQueryRouteWithArgument(searchQuery: String) = "wagersSearch?$searchQuery"

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUTE_WAGERS_LIST,
    wagersViewModel: WagersViewModel = hiltViewModel(),
    searchViewModel: WagerSearchViewModel = hiltViewModel()
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(ROUTE_WAGERS_LIST) {
            WagersView(
                modifier = Modifier.padding(top = Dimen.MARGIN_MEDIUM),
                wagersState = wagersViewModel.wagersState,
                searchQuery = it.savedStateHandle[NavigationArgs.ARG_SEARCH_QUERY]
                    ?: NavigationArgs.ARG_EMPTY_STRING,
                onSearchClick = { searchQuery ->
                    navController.navigate(getSearchQueryRouteWithArgument(searchQuery))
                },
                setSearchQuery = wagersViewModel::setSearchQuery,
                onEvent = wagersViewModel::onEvent
                )
        }
        composable(
            route = ROUTE_WAGERS_SEARCH,
            arguments = listOf(
                navArgument(NavigationArgs.ARG_SEARCH_QUERY) {
                    type = NavType.StringType
                    defaultValue = NavigationArgs.ARG_EMPTY_STRING
                }
            )) {
            SearchView(
                modifier = Modifier.padding(top = Dimen.MARGIN_MEDIUM),
                wagerSearchState = searchViewModel.wagerSearchState,
                onBackClick = {
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        NavigationArgs.ARG_SEARCH_QUERY,
                        it
                    )
                    navController.popBackStack()
                },
                onEvent = searchViewModel::onEvent
            )
        }
    }
}
