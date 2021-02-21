package me.ezzattharwat.vacomputing.di.fragment

import android.content.Context
import android.location.Geocoder
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import me.ezzattharwat.vacomputing.di.scope.FragmentScope
import java.util.*

@Module
class FragmentModule(val fragment: Fragment){

    @FragmentScope
    @Provides
    fun providesFusedLocationProviderClient(context: Context): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @FragmentScope
    @Provides
    fun providesGecoder(context: Context) = Geocoder(context, Locale.getDefault())

}
