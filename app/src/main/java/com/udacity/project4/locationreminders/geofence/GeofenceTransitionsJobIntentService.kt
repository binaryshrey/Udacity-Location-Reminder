package com.udacity.project4.locationreminders.geofence

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.utils.sendNotification
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

class GeofenceTransitionsJobIntentService : JobIntentService(), CoroutineScope {

    private var coroutineJob: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + coroutineJob

    companion object {
        private const val JOB_ID = 573

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(
                context,
                GeofenceTransitionsJobIntentService::class.java, JOB_ID,
                intent
            )
        }
    }

    override fun onHandleWork(intent: Intent) {
        //handle the geofencing transition events and send a notification
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError() == true) {
            return
        }
        if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            sendNotification(geofencingEvent.triggeringGeofences)
        }
    }

    private fun sendNotification(triggeringGeofence: List<Geofence>) {

        triggeringGeofence.forEach {
            val requestId = it.requestId

            // local repository instance
            val remindersLocalRepository: ReminderDataSource by inject()
            // setup coroutine scope
            CoroutineScope(coroutineContext).launch(SupervisorJob()) {
                //reminder request id
                val result = remindersLocalRepository.getReminder(requestId)
                if (result is Result.Success<ReminderDTO>) {
                    val reminderDTO = result.data
                    //send a notification to the user with the reminder details
                    sendNotification(this@GeofenceTransitionsJobIntentService, ReminderDataItem(
                            reminderDTO.title,
                            reminderDTO.description,
                            reminderDTO.location,
                            reminderDTO.latitude,
                            reminderDTO.longitude,
                            reminderDTO.id
                        )
                    )

                    val remindersRepository: ReminderDataSource by inject()
                    // setup coroutine scope
                    CoroutineScope(coroutineContext).launch(SupervisorJob()) {
                        //reminder request id
                        val result = remindersRepository.getReminder(requestId)
                        if (result is Result.Success<ReminderDTO>) {
                            val reminderDTO = result.data
                            //send a notification to the user with the reminder details
                            sendNotification(this@GeofenceTransitionsJobIntentService, ReminderDataItem(
                                    reminderDTO.title,
                                    reminderDTO.description,
                                    reminderDTO.location,
                                    reminderDTO.latitude,
                                    reminderDTO.longitude,
                                    reminderDTO.id
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}