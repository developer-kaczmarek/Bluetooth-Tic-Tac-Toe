package io.github.kaczmarek.tictactoe.ui.welcome

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import io.github.kaczmarek.tictactoe.R
import io.github.kaczmarek.tictactoe.databinding.FragmentWelcomeBinding
import io.github.kaczmarek.tictactoe.ui.main.MainActivity
import io.github.kaczmarek.tictactoe.utils.OnNavigateListener
import org.koin.androidx.viewmodel.ext.android.viewModel


class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    private var navigateListener: OnNavigateListener? = null
    private var viewBinding: FragmentWelcomeBinding? = null

    private val viewModel by viewModel<WelcomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateListener = context as MainActivity

        viewBinding = FragmentWelcomeBinding.bind(view)
        viewBinding?.btnContinue?.setOnClickListener {
            viewModel.onNextClicked()
            viewModel.getShouldGoNext().observe(viewLifecycleOwner, {
                if (it == true) {
                    navigateListener?.onNavigate(
                        R.id.welcomeFragment,
                        WelcomeFragmentDirections.actionWelcomeFragmentToChooserFragment()
                    )
                } else {
                    viewBinding?.clWelcomeContainer?.let { container ->
                        Snackbar.make(
                            container,
                            R.string.common_error_bluetooth_is_disabled,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
        navigateListener = null
    }
}