package br.edu.utfpr.consultacepkmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.edu.utfpr.consultacepkmp.ui.CepScreen
import br.edu.utfpr.consultacepkmp.ui.theme.ConsultaCepKmpTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ConsultaCepKmpTheme {
                CepScreen()
            }
        }
    }
}