package org.xtras.mvi.actions

private fun getNonConsumedItems(iterable: Iterable<Action>) = iterable.filter { !it.isConsumed }

fun Iterable<Action>.newInsertingActions(newActions: List<Action>) = Iterable {
    val oldItems = toList()

    iterator {
        yieldAll(getNonConsumedItems(oldItems))
        yieldAll(getNonConsumedItems(newActions))
    }
}

fun Iterable<Action>.newInsertingActions(vararg newActions: Action) = newInsertingActions(newActions.toList())
