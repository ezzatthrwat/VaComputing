package me.ezzattharwat.vacomputing.alarmbroadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.ezzattharwat.vacomputing.VaComputingApp
import me.ezzattharwat.vacomputing.other.Constant.EQUATION_ID
import me.ezzattharwat.vacomputing.repository.AppRepository
import timber.log.Timber
import javax.inject.Inject

class AlarmBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var appRepository: AppRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        VaComputingApp.getComponent(context!!).inject(this)

        intent?.let {
            if (it.hasExtra(EQUATION_ID)) {

                val equationID =  it.getLongExtra(EQUATION_ID, 0L)

                CoroutineScope(Dispatchers.IO).launch {
                    appRepository.updateEquation(equationID)
                }
            }
        }
    }
}