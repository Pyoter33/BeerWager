package com.example.beerwager.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.beerwager.ui.components.createeditwager.CreateEditWagerView
import com.example.beerwager.ui.components.wagerlist.SearchView
import com.example.beerwager.ui.components.wagerlist.WagersView
import com.example.beerwager.ui.state.SaveWagerEvent
import com.example.beerwager.ui.view_model.CreateEditWagerViewModel
import com.example.beerwager.ui.view_model.WagerSearchViewModel
import com.example.beerwager.ui.view_model.WagersViewModel
import com.example.beerwager.utils.Dimen
import com.example.beerwager.utils.NavigationArgs
import kotlinx.coroutines.flow.collectLatest

private const val ROUTE_WAGERS_LIST = "wagersList"
private const val ROUTE_WAGERS_SEARCH = "wagersSearch?searchQuery={searchQuery}"
private const val ROUTE_WAGERS_CREATE = "wagersCreate?wagerId={wagerId}?category={category}"
private fun getSearchQueryRouteWithArgument(searchQuery: String) = "wagersSearch?searchQuery=$searchQuery"
private fun getDetailsRoute(id: Long, category: String) = "wagersCreate?wagerId=$id?category=$category"

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUTE_WAGERS_LIST,
) {
    val uri = "https://beerwager.com"
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        // main wagers screen
        composable(ROUTE_WAGERS_LIST) {
            val viewModel = hiltViewModel<WagersViewModel>()
            val state by viewModel.wagersState.collectAsState()
            WagersView(
                modifier = Modifier.padding(top = Dimen.MARGIN_MEDIUM),
                state = state,
                searchQuery = it.savedStateHandle[NavigationArgs.ARG_SEARCH_QUERY]
                    ?: NavigationArgs.ARG_EMPTY_STRING,
                onSearchClick = { searchQuery ->
                    navController.navigate(getSearchQueryRouteWithArgument(searchQuery))
                },
                setSearchQuery = viewModel::setSearchQuery,
                onCreateClick = {
                    navController.navigate(ROUTE_WAGERS_CREATE)
                },
                onWagerClick = { id, category ->
                    navController.navigate(getDetailsRoute(id, category))
                },
                onEvent = viewModel::onEvent
                )
        }
        // search for wagers screen
        composable(
            route = ROUTE_WAGERS_SEARCH,
            arguments = listOf(
                navArgument(NavigationArgs.ARG_SEARCH_QUERY) {
                    type = NavType.StringType
                    defaultValue = NavigationArgs.ARG_EMPTY_STRING
                }
            )) {
            val viewModel = hiltViewModel<WagerSearchViewModel>()
            val state by viewModel.wagerSearchState.collectAsState()
            SearchView(
                modifier = Modifier.padding(top = Dimen.MARGIN_MEDIUM),
                state = state,
                onBackClick = {
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        NavigationArgs.ARG_SEARCH_QUERY,
                        it
                    )
                    navController.popBackStack()
                },
                onWagerClick = { id, category ->
                    navController.navigate(getDetailsRoute(id, category))
                },
                onEvent = viewModel::onEvent
            )
        }
        // wager details + create + edit  screen
        composable(
            route = ROUTE_WAGERS_CREATE,
            arguments = listOf(
                navArgument(NavigationArgs.ARG_WAGER_ID) {
                    type = NavType.LongType
                    defaultValue = -1L
                },
                navArgument(NavigationArgs.ARG_CATEGORY) {
                    type = NavType.StringType
                    nullable = true
                }
            ),
            deepLinks = listOf(navDeepLink { uriPattern = "$uri/wagersCreate/wagerId={wagerId}/category={category}" })
        ) {
            val viewModel = hiltViewModel<CreateEditWagerViewModel>()
            val state by viewModel.createWagerState.collectAsState()
            LaunchedEffect(key1 = true) {
                viewModel.uiEvent.collectLatest {
                    if (it is SaveWagerEvent) navController.popBackStack()

                }
            }
            CreateEditWagerView(
                state = state,
                onEvent = viewModel::onEvent,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
