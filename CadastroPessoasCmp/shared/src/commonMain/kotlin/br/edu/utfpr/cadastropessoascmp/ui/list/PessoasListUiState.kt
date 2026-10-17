package br.edu.utfpr.cadastropessoascmp.ui.list

import br.edu.utfpr.cadastropessoascmp.domain.model.Pessoa

sealed interface PessoasListUiState {
    data object Loading : PessoasListUiState

    data class Success(
        val pessoas: List<Pessoa> = emptyList(),
        val isRefreshing: Boolean = false
    ) : PessoasListUiState

    data class Error(
        val message: String = "Ocorreu um erro ao carregar as pessoas."
    ) : PessoasListUiState
}