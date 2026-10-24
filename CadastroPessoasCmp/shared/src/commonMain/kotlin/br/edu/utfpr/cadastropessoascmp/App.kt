package br.edu.utfpr.cadastropessoascmp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import br.edu.utfpr.cadastropessoascmp.ui.navigation.AppNavHost
import br.edu.utfpr.cadastropessoascmp.ui.theme.CadastroPessoasTheme

@Composable
@Preview
fun App() {
    CadastroPessoasTheme {
        AppNavHost()
    }
}