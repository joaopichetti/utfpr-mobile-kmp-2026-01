package br.edu.utfpr.cadastropessoascmp.ui.form

import br.edu.utfpr.cadastropessoascmp.domain.model.Pessoa

data class PessoaFormErrors(
    val nome: String? = null,
    val cpf: String? = null,
    val telefone: String? = null,
    val cep: String? = null,
    val logradouro: String? = null,
    val numero: String? = null
) {
    val hasErrors: Boolean
        get() = listOfNotNull(nome, cpf, telefone, cep, logradouro, numero).isNotEmpty()
}

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
        val pessoa: Pessoa = Pessoa(),
        val errors: PessoaFormErrors = PessoaFormErrors()
    ) : PessoaFormUiState

    data class Saving(
        override val isEditing: Boolean = false,
        val pessoa: Pessoa = Pessoa(),
        val errors: PessoaFormErrors = PessoaFormErrors()
    ) : PessoaFormUiState

    data class SaveError(
        override val isEditing: Boolean = false,
        val pessoa: Pessoa = Pessoa(),
        val message: String = "Ocorreu um erro ao salvar a pessoa",
        val errors: PessoaFormErrors = PessoaFormErrors()
    ) : PessoaFormUiState
}