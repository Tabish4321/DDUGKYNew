package com.deendayalproject.network

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object AppUpdateNotifier {

    private val _updateRequired =
        MutableStateFlow(false)

    val updateRequired: StateFlow<Boolean>
        get() = _updateRequired

    fun notifyUpdateRequired() {
        _updateRequired.value = true
    }
}