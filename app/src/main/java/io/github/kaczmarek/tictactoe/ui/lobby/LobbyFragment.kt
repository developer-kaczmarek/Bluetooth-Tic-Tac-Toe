package io.github.kaczmarek.tictactoe.ui.lobby

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import io.github.kaczmarek.tictactoe.R
import io.github.kaczmarek.tictactoe.databinding.FragmentLobbyBinding
import io.github.kaczmarek.tictactoe.ui.main.MainActivity
import io.github.kaczmarek.tictactoe.ui.model.PlayerType
import io.github.kaczmarek.tictactoe.utils.BluetoothConnectionStatus
import io.github.kaczmarek.tictactoe.utils.OnNavigateListener
import org.koin.androidx.viewmodel.ext.android.viewModel


class LobbyFragment : Fragment(R.layout.fragment_lobby) {

    private var navigateListener: OnNavigateListener? = null
    private var viewBinding: FragmentLobbyBinding? = null

    private val playerType by lazy {
        arguments?.let { LobbyFragmentArgs.fromBundle(it).playerType }
    }

    private val viewModel by viewModel<LobbyViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigateListener = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateListener = context as MainActivity

        viewBinding = FragmentLobbyBinding.bind(view)

        if (playerType == PlayerType.HOST) {
            viewBinding?.tvLobbyDescription?.setText(R.string.lobby_fragment_host_description)
            viewModel.getClientConnection().observe(viewLifecycleOwner) { clientConnected ->
                if (clientConnected) {
                    goToGameFragment()
                } else {
                    viewBinding?.clLobbyContainer?.let {
                        Snackbar.make(
                            it,
                            R.string.lobby_fragment_error_timeout,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }

            viewModel.startServer()
        } else {
            viewBinding?.tvLobbyDescription?.setText(R.string.lobby_fragment_guest_description)
            viewModel.getConnectionStatus().observe(viewLifecycleOwner) { state ->
                if (state == BluetoothConnectionStatus.CONNECTED) {
                    goToGameFragment()
                } else {
                    viewBinding?.clLobbyContainer?.let { container ->
                        Snackbar.make(
                            container,
                            R.string.lobby_fragment_error_connect_failed,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }

            viewModel.startClient()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
        navigateListener = null
    }

    private fun goToGameFragment() {
        navigateListener?.onNavigate(
            R.id.lobbyFragment,
            LobbyFragmentDirections.actionLobbyFragmentToGameFragment()
        )
    }
}