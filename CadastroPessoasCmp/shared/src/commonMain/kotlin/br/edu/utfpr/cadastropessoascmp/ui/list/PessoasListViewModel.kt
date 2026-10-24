package br.edu.utfpr.cadastropessoascmp.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.utfpr.cadastropessoascmp.data.repository.PessoaRepositoryImpl
import br.edu.utfpr.cadastropessoascmp.domain.repository.PessoaRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class PessoasListViewModel(
    private val pessoaRepository: PessoaRepository = PessoaRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow<PessoasListUiState>(PessoasListUiState.Loading)
    val uiState: StateFlow<PessoasListUiState> = _uiState.asStateFlow()

    private var collectJob: Job? = null

    init {
        carregarPessoas()
    }

    fun carregarPessoas() {
        observarPessoas(isRefreshing = false)
    }

    fun atualizar() {
        observarPessoas(isRefreshing = true)
    }

    private fun observarPessoas(isRefreshing: Boolean) {
        collectJob?.cancel()
        collectJob = viewModelScope.launch {
            if (isRefreshing) {
                val currentState = _uiState.value
                if (currentState is PessoasListUiState.Success) {
                    _uiState.value = currentState.copy(isRefreshing = true)
                } else {
                    _uiState.value = PessoasListUiState.Loading
                }
            } else {
                _uiState.value = PessoasListUiState.Loading
            }

            // TODO - Remover após testes
            delay(2.seconds)

            pessoaRepository.observar()
                .catch { error ->
                    _uiState.value = PessoasListUiState.Error(
                        message = error.message ?: "Ocorreu um erro ao carregar as pessoas."
                    )
                }
                .collect { pessoas ->
                    _uiState.value = PessoasListUiState.Success(
                        pessoas = pessoas,
                        isRefreshing = false
                    )
                }
        }
    }
}