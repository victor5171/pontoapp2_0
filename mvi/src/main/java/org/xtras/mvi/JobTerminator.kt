package org.xtras.mvi

import kotlin.reflect.KClass

public interface JobTerminator<TIntent : Intent> {
    public fun kill(intent: TIntent)
    public fun killAllFromType(intentClass: KClass<out TIntent>)
}
