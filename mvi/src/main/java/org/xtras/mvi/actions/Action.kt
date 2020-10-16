package org.xtras.mvi.actions

public abstract class Action {
    internal var isConsumed = false

    public fun consume() {
        isConsumed = true
    }

    public fun executeAndConsume(action: () -> Unit) {
        if (isConsumed) {
            return
        }

        action()

        consume()
    }
}
