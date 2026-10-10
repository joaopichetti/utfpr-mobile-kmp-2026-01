package br.edu.utfpr.consultacepkmp.ui

import br.edu.utfpr.shared.domain.model.Endereco

sealed interface CepUiState {
    data object Idle : CepUiState
    data object Loading : CepUiState
    data class Success(val endereco: Endereco) : CepUiState
    data class Error(val mensagem: String) : CepUiState
}