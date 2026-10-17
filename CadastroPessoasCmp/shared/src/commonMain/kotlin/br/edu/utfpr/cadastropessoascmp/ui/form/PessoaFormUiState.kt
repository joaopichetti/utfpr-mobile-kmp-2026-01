package br.edu.utfpr.cadastropessoascmp.ui.form

import br.edu.utfpr.cadastropessoascmp.domain.model.Pessoa

sealed interface PessoaFormUiState {
    val isEditing: Boolean

    data class Loading(
        override val isEditing: Boolean = true,
        val message: String = "Carregando dados da pessoa..."
    ) : PessoaFormUiState

    data class Error(
        override val isEditing: Boolean = true,
        val message: String = "Ocorreu um erro ao carregar os dados da pessoa."
    ) : PessoaFormUiState

    data class Success(
        override val isEditing: Boolean = false,
        val pessoa: Pessoa = Pessoa()
    ) : PessoaFormUiState

    data class Saving(
        override val isEditing: Boolean = false,
        val pessoa: Pessoa = Pessoa(),
    ) : PessoaFormUiState

    data class SaveError(
        override val isEditing: Boolean = false,
        val pessoa: Pessoa = Pessoa(),
        val message: String = "Ocorreu um erro ao salvar a pessoa"
    ) : PessoaFormUiState
}