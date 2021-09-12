package io.github.kaczmarek.tictactoe.ui.chooser

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import io.github.kaczmarek.tictactoe.R
import io.github.kaczmarek.tictactoe.databinding.FragmentChooserBinding
import io.github.kaczmarek.tictactoe.ui.main.MainActivity
import io.github.kaczmarek.tictactoe.ui.model.PlayerType
import io.github.kaczmarek.tictactoe.utils.BluetoothConnectionStatus
import io.github.kaczmarek.tictactoe.utils.OnNavigateListener
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChooserFragment : Fragment(R.layout.fragment_chooser) {

    private var navigateListener: OnNavigateListener? = null
    private var viewBinding: FragmentChooserBinding? = null

    private val viewModel by viewModel<ChooserViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateListener = context as MainActivity

        viewBinding = FragmentChooserBinding.bind(view)

        viewBinding?.btnChooserHost?.setOnClickListener {
            viewModel.initServer()

            viewModel.getBluetoothConnectionStatus().observe(viewLifecycleOwner, { status ->
                if (status == BluetoothConnectionStatus.CONNECTED) {
                    navigateListener?.onNavigate(
                        R.id.chooserFragment,
                        ChooserFragmentDirections.actionChooserFragmentToLobbyFragment(PlayerType.HOST)
                    )
                } else {
                    viewBinding?.clChooserContainer?.let { container ->
                        Snackbar.make(
                            container,
                            R.string.chooser_fragment_error_connect_failed,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            })
        }

        viewBinding?.btnChooserGuest?.setOnClickListener {
            navigateListener?.onNavigate(
                R.id.chooserFragment,
                ChooserFragmentDirections.actionChooserFragmentToPairedDevicesFragment()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
        navigateListener = null
    }
}