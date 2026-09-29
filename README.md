# SafePass 2026

## Sistema de Gestión de Check-in y Validación de Asistentes

Este proyecto es una aplicación móvil desarrollada para el evento tecnológico "TechEvent 2026", diseñada para facilitar el trabajo del personal de registro.

### Arquitectura Principal
- **Data Model**: `Asistente` (Clase de datos inmutable).
- **UI State**: `RegistroState` (Sealed class para manejo exhaustivo de estados).
- **Lógica de Validación**: Implementada mediante Extension Functions y Scope Functions.
- **Interfaz de Usuario**: Desarrollada en Jetpack Compose siguiendo el estándar Edge-to-Edge.

### Requisitos Técnicos
- Kotlin
- Android 16 (API 36)
- Java 21
