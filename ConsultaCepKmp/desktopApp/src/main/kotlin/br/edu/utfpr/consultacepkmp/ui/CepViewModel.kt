package br.edu.utfpr.consultacepkmp.ui

import br.edu.utfpr.shared.data.repository.CepRepositoryImpl
import br.edu.utfpr.shared.domain.repository.CepRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CepViewModel(
    private val repository: CepRepository = CepRepositoryImpl(),
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
) {
    private val _uiState = MutableStateFlow<CepUiState>(CepUiState.Idle)
    val uiState: StateFlow<CepUiState> = _uiState.asStateFlow()

    private val _cepInput = MutableStateFlow("")
    val cepInput: StateFlow<String> = _cepInput.asStateFlow()

    fun onCepChange(newCep: String) {
        val sanitized = newCep.filter { it.isDigit() }.take(8)
        _cepInput.value = sanitized
    }

    fun buscarCep() {
        val cep = _cepInput.value.trim()
        if (cep.length != 8) {
            _uiState.value = CepUiState.Error("Digite um CEP válido com 8 dígitos.")
            return
        }

        scope.launch {
            _uiState.value = CepUiState.Loading
            repository.buscarCep(cep)
                .onSuccess { endereco ->
                    _uiState.value = CepUiState.Success(endereco)
                }
                .onFailure { exception ->
                    _uiState.value = CepUiState.Error(
                        exception.message ?: "Ocorreu um erro ao consultar o CEP."
                    )
                }
        }
    }
}