package com.example.beerwager.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.beerwager.ui.work.ResetAlarmsWorker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver @Inject constructor() : BroadcastReceiver() {

    @Inject lateinit var workManager: WorkManager

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != Intent.ACTION_BOOT_COMPLETED) return
        val work = OneTimeWorkRequestBuilder<ResetAlarmsWorker>().build()
        workManager.enqueueUniqueWork(ResetAlarmsWorker.WORKER_NAME, ExistingWorkPolicy.KEEP, work)
    }
}