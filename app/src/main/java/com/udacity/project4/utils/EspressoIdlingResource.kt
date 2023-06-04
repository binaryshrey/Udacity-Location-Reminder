package com.udacity.project4.utils

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {

    private val ESPRESSO_RESOURCE = "GLOBAL"

    @JvmField
    val countingIdlingResource = CountingIdlingResource(ESPRESSO_RESOURCE)

    fun decrementRes() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }

    fun incrementRes() {
        countingIdlingResource.increment()
    }
}

inline fun <T> wrapEspressoIdlingResource(function: () -> T): T {
    // Espresso does not work well with coroutines yet. See https://github.com/Kotlin/kotlinx.coroutines/issues/982
    EspressoIdlingResource.incrementRes() // Set app as busy.
    return try {
        function()
    } finally {
        EspressoIdlingResource.decrementRes() // Set app as idle.
    }
}