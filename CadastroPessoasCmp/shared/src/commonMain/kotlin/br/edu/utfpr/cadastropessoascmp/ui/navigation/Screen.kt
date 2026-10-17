package br.edu.utfpr.cadastropessoascmp.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object List : Screen

    @Serializable
    data class Form(val pessoaId: Int? = null) : Screen
}