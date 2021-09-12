package io.github.kaczmarek.tictactoe.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import io.github.kaczmarek.tictactoe.R
import io.github.kaczmarek.tictactoe.databinding.ActivityMainBinding
import io.github.kaczmarek.tictactoe.utils.BluetoothService
import io.github.kaczmarek.tictactoe.utils.OnNavigateListener
import io.github.kaczmarek.tictactoe.utils.attachFragment
import io.github.kaczmarek.tictactoe.utils.replaceFragment

class MainActivity : AppCompatActivity(), OnNavigateListener {

    private lateinit var binding: ActivityMainBinding

    private val navHostController by lazy {
        Navigation.findNavController(this, R.id.navHostFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        BluetoothService.clear()
    }

    override fun onNavigate(fragmentInstance: Fragment, tag: String?, isAddToBackStack: Boolean) {
        supportFragmentManager.attachFragment(
            R.id.flMainContainer,
            fragmentInstance,
            tag,
            isAddToBackStack
        )
    }

    override fun onNavigate(currentDestinationId: Int, directions: NavDirections) {
        if (navHostController.currentDestination?.id == currentDestinationId) {
            navHostController.navigate(directions)
        }
    }

    override fun onNavigateExclusive(fragmentInstance: Fragment, tag: String?) {
        supportFragmentManager.replaceFragment(R.id.flMainContainer, fragmentInstance, tag)
    }
}