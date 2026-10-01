package com.example.safepass2026.logic
import com.example.safepass2026.data.Asistente

// Extension Function - Valida regla Int.esMayorDeEdad
fun Int.esMayorDeEdad(): Boolean = this >= 18

// Higher Order Funtion - Validar la prioridad
fun validarPrioridad(asistente: Asistente, criterio: (Asistente) -> Boolean): Boolean {
    return criterio(asistente)
}


