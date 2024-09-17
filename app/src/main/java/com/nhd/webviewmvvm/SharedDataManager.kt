package com.nhd.webviewmvvm

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object SharedDataManager {

    private val _musicActionFlow = MutableSharedFlow<String>()
    val musicActionFlow: SharedFlow<String> = _musicActionFlow
    suspend fun updateMusicAction(musicAction: String) {
        _musicActionFlow.emit(musicAction)
    }

}