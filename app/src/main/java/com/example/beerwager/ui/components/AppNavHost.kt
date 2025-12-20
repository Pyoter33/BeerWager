package com.example.beerwager.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.beerwager.ui.components.createeditwager.CreateEditWagerView
import com.example.beerwager.ui.components.createeditwager.PermissionAlertDialog
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
private const val ROUTE_WAGERS_CREATE = "wagersCreate?wagerId={wagerId}"
private fun getSearchQueryRouteWithArgument(searchQuery: String) = "wagersSearch?searchQuery=$searchQuery"
private fun getDetailsRoute(id: Long) = "wagersCreate?wagerId=$id"

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUTE_WAGERS_LIST,
) {
    val uri = "https://beerwager.com"

    PermissionAlertDialog()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        // main wagers screen
        composable(ROUTE_WAGERS_LIST) {
            val viewModel = hiltViewModel<WagersViewModel>()
            val state by viewModel.wagersState.collectAsStateWithLifecycle()
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
                onWagerClick = { id ->
                    navController.navigate(getDetailsRoute(id))
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
            val state by viewModel.wagerSearchState.collectAsStateWithLifecycle()
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
                onWagerClick = { id ->
                    navController.navigate(getDetailsRoute(id))
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
                }
            ),
            deepLinks = listOf(navDeepLink { uriPattern = "$uri/wagersCreate/wagerId={wagerId}" })
        ) {
            val lifecycleOwner = LocalLifecycleOwner.current
            val viewModel = hiltViewModel<CreateEditWagerViewModel>()
            val state by viewModel.createWagerState.collectAsStateWithLifecycle()
            LaunchedEffect(lifecycleOwner) {
                lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.uiEvent.collectLatest {
                        if (it is SaveWagerEvent) navController.popBackStack()
                    }
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
