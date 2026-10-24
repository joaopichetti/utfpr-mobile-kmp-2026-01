package br.edu.utfpr.cadastropessoascmp.ui.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.utfpr.cadastropessoascmp.domain.model.Pessoa
import br.edu.utfpr.cadastropessoascmp.domain.repository.CepRepository
import br.edu.utfpr.cadastropessoascmp.domain.repository.PessoaRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class PessoaFormViewModel(
    private val pessoaId: Int? = null,
    private val pessoaRepository: PessoaRepository,
    private val cepRepository: CepRepository
) : ViewModel() {

    private val isEditing: Boolean = pessoaId != null && pessoaId > 0

    private val _uiState = MutableStateFlow(
        if (isEditing) {
            PessoaFormUiState.Loading(isEditing = true)
        } else {
            PessoaFormUiState.Success(isEditing = false, pessoa = Pessoa())
        }
    )
    val uiState: StateFlow<PessoaFormUiState> = _uiState.asStateFlow()

    private val _pessoaSalva = MutableStateFlow(false)
    val pessoaSalva: StateFlow<Boolean> = _pessoaSalva.asStateFlow()

    private val _pessoaExcluida = MutableStateFlow(false)
    val pessoaExcluida: StateFlow<Boolean> = _pessoaExcluida.asStateFlow()

    init {
        if (isEditing) {
            carregarPessoa()
        }
    }

    fun carregarPessoa() {
        val id = pessoaId
        if (id == null || id < 0) {
            _uiState.value = PessoaFormUiState.Success(
                isEditing = false,
                pessoa = Pessoa()
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = PessoaFormUiState.Loading(isEditing = isEditing)
            runCatching {
                pessoaRepository.buscarPorId(id)
            }.onSuccess { pessoa ->
                if (pessoa != null) {
                    _uiState.value = PessoaFormUiState.Success(
                        isEditing = isEditing,
                        pessoa = pessoa
                    )
                } else {
                    _uiState.value = PessoaFormUiState.Error(
                        isEditing = isEditing,
                        message = "Pessoa não encontrada."
                    )
                }
            }.onFailure { error ->
                _uiState.value = PessoaFormUiState.Error(
                    isEditing = isEditing,
                    message = error.message ?: "Ocorreu um erro ao carregar os dados da pessoa."
                )
            }
        }
    }

    fun onPessoaChange(pessoa: Pessoa) {
        val currentState = _uiState.value
        val (previousPessoa, currentErrors, isSearchingCep) = when (currentState) {
            is PessoaFormUiState.Success -> Triple(
                currentState.pessoa, currentState.errors, currentState.isSearchingCep
            )
            is PessoaFormUiState.SaveError -> Triple(
                currentState.pessoa, currentState.errors, currentState.isSearchingCep
            )
            is PessoaFormUiState.DeleteError -> Triple(
                currentState.pessoa, currentState.errors, currentState.isSearchingCep
            )
            else -> return
        }

        val cepAlterado = pessoa.endereco.cep != previousPessoa.endereco.cep
        val pessoaAtualizada = if (cepAlterado) {
            pessoa.copy(
                endereco = pessoa.endereco.copy(
                    logradouro = "",
                    bairro = "",
                    cidade = ""
                )
            )
        } else {
            pessoa
        }

        val updatedErrors = currentErrors.copy(
            nome = if (pessoaAtualizada.nome != previousPessoa.nome) {
                validarNome(pessoaAtualizada.nome)
            } else {
                currentErrors.nome
            },
            cpf = if (pessoaAtualizada.cpf != previousPessoa.cpf) {
                validarCpf(pessoaAtualizada.cpf)
            } else {
                currentErrors.cpf
            },
            telefone = if (pessoaAtualizada.telefone != previousPessoa.telefone) {
                validarTelefone(pessoaAtualizada.telefone)
            } else {
                currentErrors.telefone
            },
            cep = if (cepAlterado) {
                validarCep(pessoaAtualizada.endereco.cep)
            } else {
                currentErrors.cep
            },
            logradouro = if (pessoaAtualizada.endereco.logradouro != previousPessoa.endereco.logradouro) {
                validarLogradouro(pessoaAtualizada.endereco.logradouro)
            } else {
                currentErrors.logradouro
            },
            numero = if (pessoaAtualizada.endereco.numero != previousPessoa.endereco.numero) {
                validarNumero(pessoaAtualizada.endereco.numero)
            } else {
                currentErrors.numero
            }
        )

        _uiState.value = PessoaFormUiState.Success(
            isEditing = isEditing,
            pessoa = pessoaAtualizada,
            errors = updatedErrors,
            isSearchingCep = isSearchingCep
        )
    }

    fun salvarPessoa() {
        val (currentPessoa, currentErrors, isSearchingCep) = when (val state = _uiState.value) {
            is PessoaFormUiState.Success -> Triple(state.pessoa, state.errors, state.isSearchingCep)
            is PessoaFormUiState.SaveError -> Triple(state.pessoa, state.errors, state.isSearchingCep)
            is PessoaFormUiState.DeleteError -> Triple(state.pessoa, state.errors, state.isSearchingCep)
            else -> return
        }

        if (isSearchingCep) return

        val validationErrors = validarFormulario(currentPessoa).let { formErrors ->
            if (formErrors.cep == null && currentErrors.cep != null) {
                formErrors.copy(cep = currentErrors.cep)
            } else {
                formErrors
            }
        }
        if (validationErrors.hasErrors) {
            _uiState.value = PessoaFormUiState.Success(
                isEditing = isEditing,
                pessoa = currentPessoa,
                errors = validationErrors
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = PessoaFormUiState.Saving(
                isEditing = isEditing,
                pessoa = currentPessoa
            )
            runCatching {
                if (isEditing) {
                    pessoaRepository.atualizar(currentPessoa)
                } else {
                    pessoaRepository.salvar(currentPessoa)
                }
            }.onSuccess {
                _pessoaSalva.value = true
            }.onFailure { error ->
                _uiState.value = PessoaFormUiState.SaveError(
                    isEditing = isEditing,
                    pessoa = currentPessoa,
                    message = error.message ?: "Ocorreu um erro ao salvar a pessoa."
                )
            }
        }
    }

    fun excluirPessoa() {
        if (!isEditing) return

        val (currentPessoa, currentErrors) = when (val state = _uiState.value) {
            is PessoaFormUiState.Success -> state.pessoa to state.errors
            is PessoaFormUiState.SaveError -> state.pessoa to state.errors
            is PessoaFormUiState.DeleteError -> state.pessoa to state.errors
            else -> return
        }

        viewModelScope.launch {
            _uiState.value = PessoaFormUiState.Deleting(
                isEditing = isEditing,
                pessoa = currentPessoa,
                errors = currentErrors
            )

            delay(2.seconds)

            runCatching {
                pessoaRepository.deletar(currentPessoa)
            }.onSuccess {
                _pessoaExcluida.value = true
            }.onFailure { error ->
                _uiState.value = PessoaFormUiState.DeleteError(
                    isEditing = isEditing,
                    pessoa = currentPessoa,
                    errors = currentErrors,
                    message = error.message ?: "Ocorreu um erro ao excluir a pessoa."
                )
            }
        }
    }


    fun buscarCep() {
        val currentState = _uiState.value
        val (currentPessoa, currentErrors) = when (currentState) {
            is PessoaFormUiState.Success -> {
                if (currentState.isSearchingCep) return
                currentState.pessoa to currentState.errors
            }
            is PessoaFormUiState.SaveError -> {
                if (currentState.isSearchingCep) return
                currentState.pessoa to currentState.errors
            }
            is PessoaFormUiState.DeleteError -> {
                if (currentState.isSearchingCep) return
                currentState.pessoa to currentState.errors
            }
            else -> return
        }

        val cep = currentPessoa.endereco.cep
        if (validarCep(cep) != null) {
            return
        }

        viewModelScope.launch {
            _uiState.value = PessoaFormUiState.Success(
                isEditing = isEditing,
                pessoa = currentPessoa,
                errors = currentErrors.copy(cep = null),
                isSearchingCep = true
            )

            delay(2.seconds)

            cepRepository.buscarCep(cep)
                .onSuccess { enderecoRetornado ->
                    val stateAfterSearch = _uiState.value
                    val pessoaBase = (stateAfterSearch as? PessoaFormUiState.Success)?.pessoa ?: currentPessoa
                    val errorsBase = (stateAfterSearch as? PessoaFormUiState.Success)?.errors ?: currentErrors
                    val novoLogradouro = enderecoRetornado.logradouro

                    val pessoaComEndereco = pessoaBase.copy(
                        endereco = pessoaBase.endereco.copy(
                            logradouro = novoLogradouro,
                            bairro = enderecoRetornado.bairro,
                            cidade = enderecoRetornado.cidade
                        )
                    )

                    _uiState.value = PessoaFormUiState.Success(
                        isEditing = isEditing,
                        pessoa = pessoaComEndereco,
                        errors = errorsBase.copy(
                            cep = null,
                            logradouro = validarLogradouro(novoLogradouro)
                        ),
                        isSearchingCep = false
                    )
                }
                .onFailure { error ->
                    val stateAfterSearch = _uiState.value
                    val pessoaBase = (stateAfterSearch as? PessoaFormUiState.Success)?.pessoa ?: currentPessoa
                    val errorsBase = (stateAfterSearch as? PessoaFormUiState.Success)?.errors ?: currentErrors

                    _uiState.value = PessoaFormUiState.Success(
                        isEditing = isEditing,
                        pessoa = pessoaBase,
                        errors = errorsBase.copy(
                            cep = error.message?.takeIf { it.isNotBlank() } ?: "Erro ao buscar o CEP."
                        ),
                        isSearchingCep = false
                    )
                }
        }
    }

    private fun validarFormulario(pessoa: Pessoa): PessoaFormErrors {
        return PessoaFormErrors(
            nome = validarNome(pessoa.nome),
            cpf = validarCpf(pessoa.cpf),
            telefone = validarTelefone(pessoa.telefone),
            cep = validarCep(pessoa.endereco.cep),
            logradouro = validarLogradouro(pessoa.endereco.logradouro),
            numero = validarNumero(pessoa.endereco.numero)
        )
    }

    private fun validarNome(nome: String): String? {
        return if (nome.isBlank()) {
            "Nome é obrigatório."
        } else {
            null
        }
    }

    private fun validarCpf(cpf: String): String? {
        return when {
            cpf.isBlank() -> null
            cpf.length != 11 || !cpf.all { it.isDigit() } -> "CPF deve ser válido."
            else -> null
        }
    }

    private fun validarTelefone(telefone: String): String? {
        return when {
            telefone.isBlank() -> null
            telefone.length !in 10..11 || !telefone.all { it.isDigit() } -> "Telefone deve ser válido."
            else -> null
        }
    }

    private fun validarCep(cep: String): String? {
        return when {
            cep.isBlank() -> "CEP é obrigatório."
            cep.length != 8 || !cep.all { it.isDigit() } -> "CEP deve ser válido."
            else -> null
        }
    }

    private fun validarLogradouro(logradouro: String): String? {
        return if (logradouro.isBlank()) {
            "Logradouro é obrigatório."
        } else {
            null
        }
    }

    private fun validarNumero(numero: Int): String? {
        return when {
            numero == 0 -> "Número é obrigatório."
            numero < 0 -> "Número deve ser válido."
            else -> null
        }
    }
}