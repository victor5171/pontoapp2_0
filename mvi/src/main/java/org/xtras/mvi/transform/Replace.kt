package org.xtras.mvi.transform

public fun <T> List<T>.replaceIf(evaluation: (T) -> Boolean, producer: (T) -> T): List<T> {
    return map {
        if (evaluation(it)) {
            return@map producer(it)
        }

        it
    }
}

public inline fun <TOld, reified TNew: TOld> List<TOld>.replaceIfIs(producer: (TNew) -> TOld): List<TOld> {
    return map {
        if (it is TNew) {
            return@map producer(it)
        }

        it
    }
}
