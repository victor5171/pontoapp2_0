package org.xtras.mvi

public interface Loggable {
    public val attributes: Map<String, String>
        get() = emptyMap()
}
