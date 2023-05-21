package com.example.beerwager.ui.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.beerwager.domain.use_case.GetWagersWithNotificationUseCase
import com.example.beerwager.utils.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collectLatest

@HiltWorker
class ResetAlarmsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val getWagersWithNotificationUseCase: GetWagersWithNotificationUseCase,
    private val notificationHelper: NotificationHelper
): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        try {
            Log.i("tak", "startWork")
            getWagersWithNotificationUseCase().collectLatest { list ->
                list.forEach {
                    val wagerId = it.id ?: throw IllegalArgumentException()
                    notificationHelper.scheduleNotification(it, wagerId)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure()
        }
        return Result.success()
    }

    companion object {
        const val WORKER_NAME = "ResetAlarmsWorker"
    }
}