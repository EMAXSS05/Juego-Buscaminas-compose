import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.BuscaminasPantalla

@Composable
@Preview
fun App() {
    MaterialTheme {
        Surface {
            BuscaminasPantalla(filas = 6, columnas = 6, minas = 6)
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Buscaminas") {
        App()
    }
}
