package studio.forface.shuttle.startup

import studio.forface.shuttle.ShuttleApplicationContext

/**
 * Initialize a list of [Startup]s
 */
context(ShuttleApplicationContext)
fun init(first: Startup, vararg others: Startup) {
    buildList {
        add(first)
        addAll(others)
    }.forEach { it.init() }
}

interface Startup {

    context(ShuttleApplicationContext)
    fun init()
}
