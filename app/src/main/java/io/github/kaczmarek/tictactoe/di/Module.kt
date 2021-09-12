package io.github.kaczmarek.tictactoe.di

import io.github.kaczmarek.tictactoe.data.paireddevice.port.PairedDeviceRepositoryImpl
import io.github.kaczmarek.tictactoe.domain.paireddevice.port.PairedDeviceRepository
import io.github.kaczmarek.tictactoe.domain.paireddevice.usecase.GetPairedDevicesUseCase
import io.github.kaczmarek.tictactoe.ui.chooser.ChooserViewModel
import io.github.kaczmarek.tictactoe.ui.lobby.LobbyViewModel
import io.github.kaczmarek.tictactoe.ui.game.GameViewModel
import io.github.kaczmarek.tictactoe.ui.paireddevices.PairedDevicesViewModel
import io.github.kaczmarek.tictactoe.ui.welcome.WelcomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoriesModule = module {

    single<PairedDeviceRepository> {
        PairedDeviceRepositoryImpl()
    }
}

val viewModelsModule = module {

    viewModel {
        PairedDevicesViewModel(
            getPairedDevicesUseCase = get()
        )
    }
    viewModel {
        ChooserViewModel()
    }

    viewModel {
        LobbyViewModel()
    }

    viewModel {
        GameViewModel()
    }

    viewModel {
        WelcomeViewModel()
    }
}

val useCasesModule = module {

    single { GetPairedDevicesUseCase(get()) }
}