package io.github.kaczmarek.tictactoe.utils

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


/**
 * Метод-расширение для открытия переданного фрагмента на экране активности
 *
 * Данный метод получает по переданному тэгу [tag] фрагмент из [androidx.fragment.app.FragmentManager]
 * Если найденный фрагмент != null, то на него делается [FragmentTransaction.attach],
 * который заново присоединяет его к контейнеру в активности.
 * В противном случае инстанс фрагмента, переданный в [fragmentInstance] добавляется в [androidx.fragment.app.FragmentManager]
 *
 * Так как параметр [tag] nullable, если в него не передавать значение, то каждый раз будет создаваться
 * новый инстанс фрагмента [fragmentInstance] и добавляться в [androidx.fragment.app.FragmentManager],
 * что не очень хорошо с точки зрения пользовательского опыта и оптимизации работы приложения
 *
 * Если ранее был установлен [androidx.fragment.app.FragmentManager.getPrimaryNavigationFragment],
 * то он находится и на него делается [FragmentTransaction.detach].
 * С помощью этого механизма можно всегда получить верхний активный фрагмент с помощью
 * вызова [androidx.fragment.app.FragmentManager.getPrimaryNavigationFragment]
 *
 * Если задан флаг [isAddToBackStack], то применяется метод [FragmentTransaction.addToBackStack],
 * добавляющий [FragmentTransaction] в стак возврата.
 * По-умолчанию данный флаг стоит в позиции [java.lang.Boolean.FALSE]
 *
 * [FragmentTransaction] задается флаг оптимизации [FragmentTransaction.setReorderingAllowed] и транзакция добавляется
 * в очередь выполнения [androidx.fragment.app.FragmentManager]
 *
 * @param containerId Идентификатор контейнера, в который будет помещён фрагмент
 * @param fragmentInstance Инстанс фрагмента для создания его, если ранее создан не был
 * @param tag Метка фрагмента, по которой можно будет найти фрагмент среди уже созданных
 * @param isAddToBackStack Флаг, говорящий о том, добавлять в backStack [fragmentInstance] или нет
 */
fun FragmentManager.attachFragment(
    @IdRes containerId: Int,
    fragmentInstance: Fragment,
    tag: String?,
    isAddToBackStack: Boolean = false
) {
    val fragmentTransaction = beginTransaction()

    var fragment = findFragmentByTag(tag)
    if (fragment == null) {
        fragment = fragmentInstance
        fragmentTransaction.add(containerId, fragment, tag)
    } else {
        fragmentTransaction.attach(fragment)
    }

    val curFrag = primaryNavigationFragment
    if (fragment != curFrag) {
        if (curFrag != null) {
            fragmentTransaction.detach(curFrag)
        }

        if (isAddToBackStack) {
            fragmentTransaction.addToBackStack(tag)
        }

        fragmentTransaction
            .setReorderingAllowed(true)
            .setPrimaryNavigationFragment(fragment)
            .commit()
    }
}

/**
 * Метод-расширение для открытия переданного фрагмента на экране активности
 *
 * Работает по аналогии с вышеуказанным методом.
 * Лишь одна особенность - при открытии фрагмента будут стерты
 * все прошлые использованные фрагменты
 *
 * @param containerId Идентификатор контейнера, в который будет помещён фрагмент
 * @param fragmentInstance Инстанс фрагмента для создания его, если ранее создан не был
 * @param tag Метка фрагмента, по которой можно будет найти фрагмент среди уже созданных
 */
fun FragmentManager.replaceFragment(
    @IdRes containerId: Int,
    fragmentInstance: Fragment,
    tag: String?
) {
    val fragmentTransaction = beginTransaction()

    if (backStackEntryCount > 0) {
        popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fragmentTransaction
        .replace(containerId, fragmentInstance, tag)
        .setReorderingAllowed(true)
        .setPrimaryNavigationFragment(fragmentInstance)
        .commit()
}