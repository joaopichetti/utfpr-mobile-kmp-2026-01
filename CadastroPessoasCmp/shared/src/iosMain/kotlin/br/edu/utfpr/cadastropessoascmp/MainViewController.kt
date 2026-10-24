package br.edu.utfpr.cadastropessoascmp

import androidx.compose.ui.window.ComposeUIViewController
import br.edu.utfpr.cadastropessoascmp.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}