package org.xtras.mvi.actions

abstract class Action {
    internal var isConsumed = false

    fun consume() {
        isConsumed = true
    }
}
