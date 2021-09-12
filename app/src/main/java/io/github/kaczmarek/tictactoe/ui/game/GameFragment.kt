package io.github.kaczmarek.tictactoe.ui.game

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import io.github.kaczmarek.tictactoe.R
import io.github.kaczmarek.tictactoe.databinding.FragmentGameBinding
import io.github.kaczmarek.tictactoe.utils.BluetoothService
import org.koin.androidx.viewmodel.ext.android.viewModel


class GameFragment : Fragment(R.layout.fragment_game) {

    private var viewBinding: FragmentGameBinding? = null

    private val viewModel by viewModel<GameViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentGameBinding.bind(view)
        viewBinding?.gameViewModel = viewModel

        BluetoothService.getMessageObservable().observe(viewLifecycleOwner, {
            val list = it.split(",")
            val row = list[0].toInt()
            val cell = list[1].toInt()
            viewModel.getMark(row, cell)
        })
        viewModel.getWinner().observe(viewLifecycleOwner, {
            Log.i("MY TAG", "winner = ${it?.value}")
            val message = "Победа ${it?.value?.mark}"
            AlertDialog.Builder(requireContext())
                .setTitle(message)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_ok) { dialogInterface, _ ->
                    dialogInterface.dismiss()

                }
                .setNegativeButton(R.string.dialog_close) { dialogInterface, _ ->
                    dialogInterface.dismiss()

                }
                .show()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}