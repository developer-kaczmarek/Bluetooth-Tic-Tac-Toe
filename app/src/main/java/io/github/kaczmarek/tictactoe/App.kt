package io.github.kaczmarek.tictactoe

import android.app.Application
import io.github.kaczmarek.tictactoe.di.repositoriesModule
import io.github.kaczmarek.tictactoe.di.useCasesModule
import io.github.kaczmarek.tictactoe.di.viewModelsModule
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    repositoriesModule,
                    viewModelsModule,
                    useCasesModule
                )
            )
        }
    }
}