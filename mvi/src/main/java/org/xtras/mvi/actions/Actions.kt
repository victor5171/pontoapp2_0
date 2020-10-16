package org.xtras.mvi.actions

private fun <T : Action> getNonConsumedItems(iterable: Iterable<T>): Iterable<T> = iterable.filter { !it.isConsumed }

public fun <T : Action> Iterable<T>.newInsertingActions(newActions: List<T>): Iterable<T> = Iterable {
    val oldItems = toList()

    iterator {
        yieldAll(getNonConsumedItems(oldItems))
        yieldAll(getNonConsumedItems(newActions))
    }
}

public fun <T : Action> Iterable<T>.newInsertingActions(vararg newActions: T): Iterable<T> = newInsertingActions(newActions.toList())
