package MotorBuscaminas
import kotlin.random.Random


class Tablero(val filas: Int, val columnas: Int, val minas: Int) {
    val casillas: Array<Array<Casilla>>

    init {
        if (filas <= 0 || columnas <= 0) {
            throw IllegalArgumentException("El tablero debe tener al menos 1 fila y 1 columna.")
        }

        if (minas < 0 || minas >= filas * columnas) {
            throw IllegalArgumentException("Cantidad de minas inválida.")
        }

        casillas = Array(filas) { fila ->
            Array(columnas) { columna ->
                Casilla(fila, columna)
            }
        }

        generarMinas()
    }

    private fun generarMinas() {
        var minasColocadas = 0
        while (minasColocadas < minas) {
            val fila = Random.nextInt(filas)
            val columna = Random.nextInt(columnas)
            val casilla = casillas[fila][columna]
            if (!casilla.esMina) {
                casilla.esMina = true
                actualizarMinasAlrededor(fila, columna)
                minasColocadas++
            }
        }
    }

    private fun actualizarMinasAlrededor(fila: Int, columna: Int) {
        obtenerAdyacentes(fila, columna).forEach {
            it.minasAlrededor++
        }
    }

    private fun obtenerAdyacentes(fila: Int, columna: Int): List<Casilla> {
        val direcciones = listOf(
            -1 to -1, -1 to 0, -1 to 1,
            0 to -1,          0 to 1,
            1 to -1,  1 to 0, 1 to 1
        )

        return direcciones.mapNotNull { (df, dc) ->
            val nf = fila + df
            val nc = columna + dc
            if (nf in 0 until filas && nc in 0 until columnas) casillas[nf][nc] else null
        }
    }

    fun alternarBandera(fila: Int, columna: Int) {
        val c = casillas[fila][columna]
        if (!c.revelada) {
            c.tieneBandera = !c.tieneBandera
        }
    }

    fun revelar(fila: Int, columna: Int) {
        val c = casillas[fila][columna]
        if (c.revelada || c.tieneBandera) return

        c.revelada = true
        if (c.minasAlrededor == 0 && !c.esMina) {
            obtenerAdyacentes(fila, columna).forEach {
                revelar(it.fila, it.columna)
            }
        }
    }

    fun juegoGanado(): Boolean {
        return casillas.flatten().all {
            (it.revelada && !it.esMina) || (!it.revelada && it.esMina)
        }
    }
}