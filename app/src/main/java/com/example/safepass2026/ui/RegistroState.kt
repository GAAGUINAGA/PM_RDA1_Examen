package com.example.safepass2026.ui

import com.example.safepass2026.data.Asistente

sealed class RegistroState{
    data object Idle:RegistroState()

    data class Success(
        val asistente: Asistente,
        val pasePrioritario: Boolean,
        val mensaje: String
    ) : RegistroState()

    data class Error(val causa: String) : RegistroState()
}