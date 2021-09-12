package io.github.kaczmarek.tictactoe.ui.paireddevices

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import io.github.kaczmarek.tictactoe.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import io.github.kaczmarek.tictactoe.databinding.FragmentPairedDevicesBinding
import io.github.kaczmarek.tictactoe.ui.base.BaseListAdapter
import io.github.kaczmarek.tictactoe.ui.base.BaseRVItem
import io.github.kaczmarek.tictactoe.ui.main.MainActivity
import io.github.kaczmarek.tictactoe.ui.model.PlayerType
import io.github.kaczmarek.tictactoe.ui.paireddevices.adapter.ItemPairedDevice
import io.github.kaczmarek.tictactoe.utils.OnNavigateListener


class PairedDevicesFragment : Fragment(R.layout.fragment_paired_devices),
    BaseListAdapter.OnClickListener {

    private var viewBinding: FragmentPairedDevicesBinding? = null
    private var rvAdapter: PairedDevicesRVAdapter? = null
    private var navigateListener: OnNavigateListener? = null

    private val viewModel by viewModel<PairedDevicesViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigateListener = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentPairedDevicesBinding.bind(view)

        rvAdapter = PairedDevicesRVAdapter().apply {
            onClickListener = this@PairedDevicesFragment
        }

        viewBinding?.rvPairedDevices?.adapter = rvAdapter

        initObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rvAdapter?.onClickListener = null
        rvAdapter = null
        viewBinding = null
        navigateListener = null
    }

    override fun onClick(item: BaseRVItem) {
        when (item) {
            is ItemPairedDevice -> {
                viewModel.onDeviceItemClicked(item)
                viewModel.getShouldGoToLobby().observe(viewLifecycleOwner, { shouldGoToLobby ->
                    if (shouldGoToLobby) {
                        navigateListener?.onNavigate(
                            R.id.pairedDevicesFragment,
                            PairedDevicesFragmentDirections.actionPairedDevicesFragmentToLobbyFragment(
                                PlayerType.GUEST
                            )
                        )
                    }
                })
            }
        }
    }

    private fun initObserver() {
        viewModel.getPairedDevices().observe(viewLifecycleOwner, {
            rvAdapter?.update(it)
        })
        viewModel.getErrorMessage().observe(viewLifecycleOwner, { errorMessage ->
            viewBinding?.clPairedDevicesContainer?.let {
                val message = errorMessage ?: getString(R.string.common_empty_placeholder)
                Snackbar.make(it, getString(R.string.common_error, message), Snackbar.LENGTH_LONG)
                    .show()
            }
        })
    }
}