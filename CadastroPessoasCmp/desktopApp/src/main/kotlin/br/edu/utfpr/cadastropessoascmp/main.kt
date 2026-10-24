package br.edu.utfpr.cadastropessoascmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import br.edu.utfpr.cadastropessoascmp.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "CadastroPessoasCmp",
        ) {
            App()
        }
    }
}