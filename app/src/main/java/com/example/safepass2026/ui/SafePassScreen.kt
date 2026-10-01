package com.example.safepass2026.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.safepass2026.data.Asistente
import com.example.safepass2026.logic.esMayorDeEdad
import com.example.safepass2026.logic.validarPrioridad

@Composable
fun SafePassScreen(
    modifier: Modifier = Modifier,
    onRegistrar: (String, String, String) -> RegistroState = { nombre, edadTexto, tipoEntrada ->
        val nombreLimpio = nombre.trim()
        if (nombreLimpio.isEmpty()) {
            RegistroState.Error("El nombre del asistente no puede estar vacío.")
        } else if (nombreLimpio.length < 3) {
            RegistroState.Error("El nombre debe contener al menos 3 caracteres.")
        } else {
            // Conversión segura con toIntOrNull() para blindar contra entradas no numéricas
            val edadNumerica = edadTexto.toIntOrNull()

            // Uso de Scope Function 'let' con operador Elvis '?:' para procesar únicamente si la edad no es nula
            edadNumerica?.let { edadValida ->
                if (!edadValida.esMayorDeEdad()) {
                    // Uso de la Extension Function de mayoría de edad
                    RegistroState.Error("Acceso denegado: El asistente debe ser mayor de 18 años.")
                } else {
                    // Uso de Scope Function 'apply' para configurar el objeto Asistente
                    val asistente = Asistente(
                        nombre = nombreLimpio,
                        edad = edadValida,
                        tipoEntrada = tipoEntrada
                    ).apply {
                        // Confirmación idiomática de construcción del objeto inmutable
                    }

                    // Uso de Higher-Order Function recibiendo una lambda para evaluar prioridad
                    val tienePrioridad = validarPrioridad(asistente) {
                        it.tipoEntrada.equals("VIP", ignoreCase = true)
                    }

                    RegistroState.Success(
                        asistente = asistente,
                        pasePrioritario = tienePrioridad,
                        mensaje = "Asistente ${asistente.nombre} (${asistente.edad} años) validado para el evento."
                    )
                }
            } ?: RegistroState.Error("La edad debe ser un número entero válido.")
        }
    }
) {
    var nombreInput by remember { mutableStateOf("") }
    var edadInput by remember { mutableStateOf("") }
    var tipoEntradaSeleccionada by remember { mutableStateOf("General") }
    var estadoActual by remember { mutableStateOf<RegistroState>(RegistroState.Idle) }

    val categorias = listOf("General", "VIP", "Estudiante")

    // Degradado suave en el fondo para lograr una estética limpia y moderna
    val fondoGradiente = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE0F2FE),
            Color(0xFFF0F9FF),
            Color(0xFFF8FAFC)
        )
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(fondoGradiente)
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Encabezado principal
                HeaderSeccion()

                Spacer(modifier = Modifier.height(20.dp))

                // Tarjeta contenedora del formulario
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, Color(0xFFF1F5F9))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Registro de Asistente",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "Ingresa la información para verificar requisitos de acceso",
                            fontSize = 13.sp,
                            color = Color(0xFF64748B)
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Campo de nombre
                        Text(
                            text = "Nombre completo",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF334155)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = nombreInput,
                            onValueChange = { nombreInput = it },
                            placeholder = { Text("Ej. Alex García", color = Color(0xFF94A3B8)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF2563EB),
                                unfocusedBorderColor = Color(0xFFE2E8F0),
                                focusedContainerColor = Color(0xFFF8FAFC),
                                unfocusedContainerColor = Color(0xFFF8FAFC)
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Campo de edad
                        Text(
                            text = "Edad",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF334155)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = edadInput,
                            onValueChange = { edadInput = it },
                            placeholder = { Text("Ej. 24", color = Color(0xFF94A3B8)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF2563EB),
                                unfocusedBorderColor = Color(0xFFE2E8F0),
                                focusedContainerColor = Color(0xFFF8FAFC),
                                unfocusedContainerColor = Color(0xFFF8FAFC)
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Selector de tipo de entrada con diseño de pastillas
                        Text(
                            text = "Tipo de entrada",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF334155)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            categorias.forEach { categoria ->
                                val seleccionada = tipoEntradaSeleccionada == categoria
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (seleccionada) Color(0xFF2563EB) else Color(0xFFF1F5F9)
                                        )
                                        .clickable { tipoEntradaSeleccionada = categoria }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = categoria,
                                        fontSize = 13.sp,
                                        fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Medium,
                                        color = if (seleccionada) Color.White else Color(0xFF475569)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(22.dp))

                        // Botón de validación
                        Button(
                            onClick = {
                                estadoActual = onRegistrar(nombreInput, edadInput, tipoEntradaSeleccionada)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                        ) {
                            Text(
                                text = "Validar y Registrar",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        // Botón para reiniciar formulario si no está en Idle
                        if (estadoActual !is RegistroState.Idle) {
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedButton(
                                onClick = {
                                    nombreInput = ""
                                    edadInput = ""
                                    tipoEntradaSeleccionada = "General"
                                    estadoActual = RegistroState.Idle
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                            ) {
                                Text(
                                    text = "Nuevo Registro",
                                    fontSize = 14.sp,
                                    color = Color(0xFF475569)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Evaluación exhaustiva de los estados de la UI
                when (val estado = estadoActual) {
                    is RegistroState.Idle -> EstadoIdleCard()
                    is RegistroState.Success -> EstadoSuccessCard(estado = estado)
                    is RegistroState.Error -> EstadoErrorCard(estado = estado)
                }
            }
        }
    }
}

@Composable
private fun HeaderSeccion() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "SafePass 2026",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "TechEvent • Validación de Asistentes",
                fontSize = 13.sp,
                color = Color(0xFF64748B)
            )
        }

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = Color(0xFF2563EB),
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun EstadoIdleCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFEFF6FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Color(0xFF2563EB),
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = "Esperando datos",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Completa los campos para procesar la entrada al evento.",
                    fontSize = 13.sp,
                    color = Color(0xFF64748B)
                )
            }
        }
    }
}

@Composable
private fun EstadoSuccessCard(estado: RegistroState.Success) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(1.dp, Color(0xFFBBF7D0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFDCFCE7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF16A34A),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Acceso Autorizado",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF14532D)
                    )
                    Text(
                        text = "Registro verificado satisfactoriamente",
                        fontSize = 12.sp,
                        color = Color(0xFF16A34A)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
            Spacer(modifier = Modifier.height(14.dp))

            // Resumen formateado con plantillas de texto
            val asistente = estado.asistente
            FilaDetalle(etiqueta = "Asistente", valor = asistente.nombre)
            FilaDetalle(etiqueta = "Edad", valor = "${asistente.edad ?: "No especificada"} años")
            FilaDetalle(etiqueta = "Categoría", valor = asistente.tipoEntrada)

            Spacer(modifier = Modifier.height(14.dp))

            // Distintivo de prioridad sin emojis
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (estado.pasePrioritario) Color(0xFFFEF3C7) else Color(0xFFF1F5F9))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = if (estado.pasePrioritario) "Pase Prioritario Concedido" else "Acceso Estándar",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (estado.pasePrioritario) Color(0xFF92400E) else Color(0xFF475569)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = estado.mensaje,
                fontSize = 12.sp,
                color = Color(0xFF15803D)
            )
        }
    }
}

@Composable
private fun EstadoErrorCard(estado: RegistroState.Error) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(1.dp, Color(0xFFFECDD3))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFE4E6)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFE11D48),
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = "Acceso Denegado",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF881337)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = estado.causa,
                    fontSize = 13.sp,
                    color = Color(0xFFE11D48)
                )
            }
        }
    }
}

@Composable
private fun FilaDetalle(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = etiqueta, fontSize = 13.sp, color = Color(0xFF64748B))
        Text(text = valor, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
    }
}

@Preview(name = "Pantalla Principal", showBackground = true)
@Composable
fun SafePassScreenPreview() {
    SafePassScreen()
}

@Preview(name = "Estado Exitoso", showBackground = true)
@Composable
fun SuccessPreview() {
    Surface(modifier = Modifier.padding(16.dp)) {
        EstadoSuccessCard(
            estado = RegistroState.Success(
                asistente = Asistente(nombre = "Alex García", edad = 24, tipoEntrada = "VIP"),
                pasePrioritario = true,
                mensaje = "Asistente Alex García (24 años) validado para el evento."
            )
        )
    }
}

@Preview(name = "Estado Error", showBackground = true)
@Composable
fun ErrorPreview() {
    Surface(modifier = Modifier.padding(16.dp)) {
        EstadoErrorCard(
            estado = RegistroState.Error(
                causa = "Acceso denegado: El asistente debe ser mayor de 18 años."
            )
        )
    }
}
