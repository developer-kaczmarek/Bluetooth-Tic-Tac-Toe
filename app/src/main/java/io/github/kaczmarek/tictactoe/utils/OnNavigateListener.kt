package io.github.kaczmarek.tictactoe.utils

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections

interface OnNavigateListener {
    fun onNavigate(fragmentInstance: Fragment, tag: String?, isAddToBackStack: Boolean = false)

    fun onNavigate(currentDestinationId: Int, directions: NavDirections)

    fun onNavigateExclusive(fragmentInstance: Fragment, tag: String?)
}