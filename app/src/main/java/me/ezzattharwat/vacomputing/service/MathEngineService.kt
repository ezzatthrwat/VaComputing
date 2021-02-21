package me.ezzattharwat.vacomputing.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleService
import me.ezzattharwat.vacomputing.alarmbroadcast.AlarmBroadcastReceiver
import me.ezzattharwat.vacomputing.other.Constant.ACTION_START_SERVICE
import me.ezzattharwat.vacomputing.other.Constant.EQUATION_ID
import me.ezzattharwat.vacomputing.other.Constant.MATH_EQUATION_SCHEDULED_TIME
import timber.log.Timber
import java.util.*

class MathEngineService : LifecycleService() {


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (it.action) {
                ACTION_START_SERVICE -> {
                    if (it.hasExtra(MATH_EQUATION_SCHEDULED_TIME)&&
                        it.hasExtra(EQUATION_ID)){
                        createScheduledAlarm(it)
                    }
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }


    private fun createScheduledAlarm(it: Intent) {

        val equationID = it.getLongExtra(EQUATION_ID, 0L)

        val time = it.getIntExtra(MATH_EQUATION_SCHEDULED_TIME, 0)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmBroadcastReceiver::class.java)
            intent.putExtra(EQUATION_ID, equationID)

        val alarmPendingIntent = PendingIntent.getBroadcast(this, equationID.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, time)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmPendingIntent)
    }

}


