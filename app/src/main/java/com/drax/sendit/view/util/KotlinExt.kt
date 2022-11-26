package com.drax.sendit.view.util


fun <K> Any?.ifNotNull(vararg also: Any? = emptyArray(), then: (List<Any>) -> K ): K? {
    val all = listOf(*also) + this
    return all.takeIf {
        all.all { it != null }
    }?.map { it!! }?.let {
        then.invoke(it)
    }
}
