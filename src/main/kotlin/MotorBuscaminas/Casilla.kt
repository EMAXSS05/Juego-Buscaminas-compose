package MotorBuscaminas

data class Casilla(
    val fila: Int,
    val columna: Int,
    var esMina: Boolean = false,
    var minasAlrededor: Int = 0,
    var revelada: Boolean = false,
    var tieneBandera: Boolean = false
)
