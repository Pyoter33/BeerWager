package com.example.beerwager.ui.components.createeditwager

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import com.example.beerwager.R
import com.example.beerwager.ui.theme.White
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionAlertDialog() {
    val context = LocalContext.current

    val needsPostNotifications = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    val needsScheduleAlarms = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    val notificationPermissionState = rememberNotificationPermissionState(needsPostNotifications)
    var scheduleAlarmPermissionGranted by remember {
        mutableStateOf(isScheduleAlarmGranted(context, needsScheduleAlarms))
    }
    val scheduleAlarmLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            scheduleAlarmPermissionGranted = isScheduleAlarmGranted(context, needsScheduleAlarms)
        }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(
        notificationPermissionState?.status?.isGranted,
        scheduleAlarmPermissionGranted
    ) {
        showDialog = shouldShowDialog(notificationPermissionState, scheduleAlarmPermissionGranted)
    }

    if (showDialog) {
        val notificationGranted = notificationPermissionState?.status?.isGranted ?: true
        val notificationDenied = notificationPermissionState?.status?.shouldShowRationale ?: false
        val scheduleGranted = scheduleAlarmPermissionGranted

        PermissionAlertDialogContent(
            notificationGranted = notificationGranted,
            scheduleGranted = scheduleGranted,
            onConfirm = {
                handleConfirmClick(
                    context = context,
                    notificationDenied = notificationDenied,
                    notificationGranted = notificationGranted,
                    scheduleGranted = scheduleGranted,
                    notificationPermissionState = notificationPermissionState,
                    scheduleAlarmLauncher = scheduleAlarmLauncher
                )
            }
        )
    }
}

@SuppressLint("InlinedApi")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun rememberNotificationPermissionState(
    needsPostNotifications: Boolean
): PermissionState? {
    return if (needsPostNotifications) {
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        null
    }
}

@SuppressLint("NewApi")
private fun isScheduleAlarmGranted(
    context: Context,
    needsScheduleAlarms: Boolean
): Boolean {
    return if (!needsScheduleAlarms) {
        true
    } else {
        val alarmManager = context.getSystemService(AlarmManager::class.java)
        alarmManager?.canScheduleExactAlarms() == true
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun shouldShowDialog(
    notificationPermissionState: PermissionState?,
    scheduleAlarmPermissionGranted: Boolean
): Boolean {
    val notificationGranted = notificationPermissionState?.status?.isGranted ?: true
    val scheduleGranted = scheduleAlarmPermissionGranted
    return !(notificationGranted && scheduleGranted)
}

@Composable
private fun PermissionAlertDialogContent(
    notificationGranted: Boolean,
    scheduleGranted: Boolean,
    onConfirm: () -> Unit
) {
    AlertDialog(
        containerColor = White,
        onDismissRequest = {
            // no-op
        },
        title = {
            Text(
                text = stringResource(R.string.notification_permissions_required)
            )
        },
        text = {
            Text(
                text = getDialogText(notificationGranted, scheduleGranted),
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = getConfirmButtonText(notificationGranted, scheduleGranted).uppercase()
                )
            }
        }
    )
}

@Composable
private fun getDialogText(
    notificationGranted: Boolean,
    scheduleGranted: Boolean
): String {
    return when {
        !notificationGranted && !scheduleGranted ->
            stringResource(R.string.notification_permission_notifications_alarms)

        !notificationGranted ->
            stringResource(R.string.notification_permission_notifications)

        !scheduleGranted ->
            stringResource(R.string.notification_permission_alarms)

        else ->
            stringResource(R.string.notification_permission_granted)
    }
}

@Composable
private fun getConfirmButtonText(
    notificationGranted: Boolean,
    scheduleGranted: Boolean
): String {
    return when {
        !notificationGranted && !scheduleGranted ->
            stringResource(R.string.notification_permission_allow_notifications)

        !notificationGranted ->
            stringResource(R.string.notification_permission_allow_notifications)

        !scheduleGranted ->
            stringResource(R.string.notification_permission_allow_exact_alarms)

        else ->
            stringResource(android.R.string.ok)
    }
}

@SuppressLint("InlinedApi")
@OptIn(ExperimentalPermissionsApi::class)
private fun handleConfirmClick(
    context: Context,
    notificationDenied: Boolean,
    notificationGranted: Boolean,
    scheduleGranted: Boolean,
    notificationPermissionState: PermissionState?,
    scheduleAlarmLauncher: ActivityResultLauncher<Intent>
) {
    when {
        notificationDenied -> {
            val intent = Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = "package:${context.packageName}".toUri()
            }
            context.startActivity(intent)
        }

        !notificationGranted -> notificationPermissionState?.launchPermissionRequest()

        !scheduleGranted -> {
            val intent = Intent().apply {
                action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
            }
            scheduleAlarmLauncher.launch(intent)
        }
    }
}
