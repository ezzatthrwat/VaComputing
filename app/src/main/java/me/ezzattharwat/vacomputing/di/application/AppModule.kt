package me.ezzattharwat.vacomputing.di.application

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import dagger.Module
import dagger.Provides
import me.ezzattharwat.vacomputing.data.db.EquationDAO
import me.ezzattharwat.vacomputing.data.db.EquationsDatabase
import me.ezzattharwat.vacomputing.di.scope.ApplicationScope
import me.ezzattharwat.vacomputing.operators.CalculatorEngine
import me.ezzattharwat.vacomputing.repository.AppRepository

@Module
class AppModule(val application: Application) {

    @ApplicationScope
    @Provides
    fun providesApplication(): Application {
        return application
    }

    @ApplicationScope
    @Provides
    fun providesApplicationContext(): Context {
        return application
    }

    @ApplicationScope
    @Provides
    fun provideViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory = factory

    @ApplicationScope
    @Provides
    fun providesRoomDatabase(): EquationsDatabase =
        Room.databaseBuilder(application, EquationsDatabase::class.java, "equations_database")
            .fallbackToDestructiveMigration().build()


    @ApplicationScope
    @Provides
    fun providesEquationDAO(equationsDatabase: EquationsDatabase) = equationsDatabase.getEquationDAO()


    @ApplicationScope
    @Provides
    fun providesAppRepository(equationDAO: EquationDAO) = AppRepository(equationDAO)

    @ApplicationScope
    @Provides
    fun provideAppCalcEngine() = CalculatorEngine()


}