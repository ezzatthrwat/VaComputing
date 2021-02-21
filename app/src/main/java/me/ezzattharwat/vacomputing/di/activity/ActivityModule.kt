package me.ezzattharwat.vacomputing.di.activity

import android.app.Activity
import dagger.Module
import me.ezzattharwat.vacomputing.di.scope.ActivityScope

/**
 * This class is responsible for providing the requested objects to [ActivityScope] annotated classes
 */
@Module
class ActivityModule(val activity: Activity){

}