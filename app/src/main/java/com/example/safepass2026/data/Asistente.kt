package com.example.safepass2026.data
/**
 * Representa a un asistente registrado para el TechEvent 2026.
 *
 * @property nombre Nombre completo del asistente. No debe ser vacío.
 * @property edad Edad del asistente. Es anulable (Int?) para gestionar
 *                la ausencia de datos o entradas inválidas sin provocar caídas.
 * @property tipoEntrada Categoría de acceso (ej. "General", "VIP", "Estudiante").
 */
data class Asistente(
    val nombre: String,
    val edad: Int?,
    val tipoEntrada: String
)