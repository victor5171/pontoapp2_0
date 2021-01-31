package org.xtras.mvi.transform

public fun <TOwner> not(function: (TOwner) -> Boolean): (TOwner) -> Boolean {
    return {
        !function(it)
    }
}

public fun <TOwner, TValue> equals(retriever: (TOwner) -> TValue, valueToCompare: TValue): (TOwner) -> Boolean {
    return {
        retriever(it) == valueToCompare
    }
}

public fun <TOwner, TValue> notEquals(retriever: (TOwner) -> TValue, valueToCompare: TValue): (TOwner) -> Boolean {
    return not(equals(retriever, valueToCompare))
}
