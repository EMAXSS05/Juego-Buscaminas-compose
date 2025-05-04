package ui

import MotorBuscaminas.Tablero
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun BuscaminasPantalla(filas: Int, columnas: Int, minas: Int) {
    var tablero by remember { mutableStateOf(Tablero(filas, columnas, minas)) }
    var tiempo by remember { mutableStateOf(0) }
    var juegoPerdido by remember { mutableStateOf(false) }
    var modoBandera by remember { mutableStateOf(false) }

    val juegoGanado = tablero.juegoGanado()
    val juegoTerminado = juegoGanado || juegoPerdido


    LaunchedEffect(juegoTerminado) {
        while (!juegoTerminado) {
            delay(1000)
            tiempo++
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("⏱\uFE0F ${tiempo}s", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(8.dp))


        Button(onClick = { modoBandera = !modoBandera }) {
            Text(if (modoBandera) "Modo: 🚩 Bandera" else "Modo: Revelar Celda")
        }

        Spacer(modifier = Modifier.height(16.dp))

        for (fila in 0 until filas) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                for (col in 0 until columnas) {
                    val casilla = tablero.casillas[fila][col]
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .border(1.dp, Color.Black)
                            .background(
                                when {
                                    casilla.revelada -> Color.LightGray
                                    casilla.tieneBandera -> Color.Yellow
                                    else -> Color.Gray
                                }
                            )
                            .clickable(enabled = !juegoTerminado) {
                                if (modoBandera && !casilla.revelada) {
                                    tablero.alternarBandera(fila, col)
                                } else if (!casilla.tieneBandera && !casilla.revelada) {
                                    tablero.revelar(fila, col)
                                    if (tablero.casillas[fila][col].esMina) {
                                        juegoPerdido = true
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (casilla.revelada) {
                            Text(
                                text = when {
                                    casilla.esMina -> "💣"
                                    casilla.minasAlrededor > 0 -> "${casilla.minasAlrededor}"
                                    else -> ""
                                },
                                style = MaterialTheme.typography.h6
                            )
                        } else if (casilla.tieneBandera) {
                            Text(text = "🚩")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            tablero = Tablero(filas, columnas, minas)
            tiempo = 0
            juegoPerdido = false
            modoBandera = false
        }) {
            Text("Reiniciar")
        }

        if (juegoGanado) {
            Text("¡Ganaste!", color = Color.Green, style = MaterialTheme.typography.h4)
        } else if (juegoPerdido) {
            Text("¡Perdiste! ", color = Color.Red, style = MaterialTheme.typography.h4)
        }
    }
}
