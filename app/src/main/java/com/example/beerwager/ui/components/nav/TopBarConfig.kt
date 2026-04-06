package com.example.beerwager.ui.components.nav


import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class TopBarAction(
    val icon: ImageVector,
    val onClick: () -> Unit,
    val enabled: Boolean = true,
    val visible: Boolean = true
)

@Stable
class TopBarController {
    var title by mutableStateOf("")
    var showBack by mutableStateOf(false)
    var onBack by mutableStateOf<(() -> Unit)?>(null)
    var actions by mutableStateOf<List<TopBarAction>>(emptyList())
}

val LocalScreenTabBar = staticCompositionLocalOf<TopBarController> {
    error("TopBarController not provided")
}

@Composable
fun rememberTopBarController(): TopBarController {
    return remember { TopBarController() }
}

@Composable
fun ConfigureTopBar(
    title: String,
    showBack: Boolean,
    onBack: (() -> Unit)?,
    actions: List<TopBarAction> = emptyList()
) {
    val topBar = LocalScreenTabBar.current
    val latestOnBack by rememberUpdatedState(onBack)
    val latestActions by rememberUpdatedState(actions)

    SideEffect {
        topBar.title = title
        topBar.showBack = showBack
        topBar.onBack = latestOnBack
        topBar.actions = latestActions
    }
}
