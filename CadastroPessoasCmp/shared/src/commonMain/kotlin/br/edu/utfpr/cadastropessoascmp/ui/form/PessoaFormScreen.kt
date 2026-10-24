package br.edu.utfpr.cadastropessoascmp.ui.form

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.utfpr.cadastropessoascmp.domain.model.Endereco
import br.edu.utfpr.cadastropessoascmp.domain.model.Pessoa
import br.edu.utfpr.cadastropessoascmp.ui.components.ErrorComponent
import br.edu.utfpr.cadastropessoascmp.ui.components.LoadingComponent
import br.edu.utfpr.cadastropessoascmp.ui.form.components.PessoaFormSuccess

@Composable
fun PessoaFormScreen(
    modifier: Modifier = Modifier,
    pessoaId: Int? = null,
    viewModel: PessoaFormViewModel = viewModel(key = pessoaId?.toString() ?: "new") {
        PessoaFormViewModel(pessoaId = pessoaId)
    },
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit = onBackClick,
    onDeleteSuccess: () -> Unit = onBackClick
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pessoaSalva by viewModel.pessoaSalva.collectAsStateWithLifecycle()
    val pessoaExcluida by viewModel.pessoaExcluida.collectAsStateWithLifecycle()

    LaunchedEffect(pessoaSalva) {
        if (pessoaSalva) {
            onSaveSuccess()
        }
    }

    LaunchedEffect(pessoaExcluida) {
        if (pessoaExcluida) {
            onDeleteSuccess()
        }
    }

    PessoaFormScreen(
        modifier = modifier,
        uiState = uiState,
        onBackClick = onBackClick,
        onSaveClick = viewModel::salvarPessoa,
        onRetry = viewModel::carregarPessoa,
        onPessoaChange = viewModel::onPessoaChange,
        onDeleteClick = viewModel::excluirPessoa
    )
}

@Composable
fun PessoaFormScreen(
    modifier: Modifier = Modifier,
    uiState: PessoaFormUiState,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onRetry: () -> Unit,
    onPessoaChange: (Pessoa) -> Unit,
    onDeleteClick: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        when (uiState) {
            is PessoaFormUiState.SaveError -> {
                snackbarHostState.showSnackbar(uiState.message)
            }
            is PessoaFormUiState.DeleteError -> {
                snackbarHostState.showSnackbar(uiState.message)
            }
            else -> {}
        }
    }

    if (showDeleteDialog) {
        ConfirmDeleteDialog(
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                showDeleteDialog = false
                onDeleteClick()
            }
        )
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            PessoaFormTopBar(
                uiState = uiState,
                onBackClick = onBackClick,
                onSaveClick = onSaveClick,
                onDeleteClick = { showDeleteDialog = true }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                is PessoaFormUiState.Loading -> {
                    LoadingComponent(
                        message = uiState.message
                    )
                }
                is PessoaFormUiState.Error -> {
                    ErrorComponent(
                        message = uiState.message,
                        onRetry = onRetry
                    )
                }
                is PessoaFormUiState.Success -> {
                    PessoaFormSuccess(
                        pessoa = uiState.pessoa,
                        enabled = true,
                        errors = uiState.errors,
                        onPessoaChange = onPessoaChange
                    )
                }
                is PessoaFormUiState.Saving -> {
                    PessoaFormSuccess(
                        pessoa = uiState.pessoa,
                        enabled = false,
                        errors = uiState.errors,
                        onPessoaChange = onPessoaChange
                    )
                }
                is PessoaFormUiState.SaveError -> {
                    PessoaFormSuccess(
                        pessoa = uiState.pessoa,
                        enabled = true,
                        errors = uiState.errors,
                        onPessoaChange = onPessoaChange
                    )
                }
                is PessoaFormUiState.DeleteError -> {
                    PessoaFormSuccess(
                        pessoa = uiState.pessoa,
                        errors = uiState.errors,
                        enabled = true,
                        onPessoaChange = onPessoaChange
                    )
                }
                is PessoaFormUiState.Deleting -> {
                    PessoaFormSuccess(
                        pessoa = uiState.pessoa,
                        errors = uiState.errors,
                        enabled = false,
                        onPessoaChange = onPessoaChange
                    )
                }
            }
        }
    }
}

@Composable
fun ConfirmDeleteDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            Text("Confirmar exclusão")
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = "Excluir",
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun PessoaFormTopBar(
    modifier: Modifier = Modifier,
    uiState: PessoaFormUiState,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val titleText = if (uiState.isEditing) "Editar Pessoa" else "Cadastrar Pessoa"

    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = { Text(titleText) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar"
                )
            }
        },
        actions = {
            when (uiState) {
                is PessoaFormUiState.Success,
                is PessoaFormUiState.SaveError,
                is PessoaFormUiState.DeleteError -> {
                    if (uiState.isEditing) {
                        IconButton(onClick = onDeleteClick) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Excluir"
                            )
                        }
                    }
                    IconButton(onClick = onSaveClick) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Salvar"
                        )
                    }
                }
                is PessoaFormUiState.Saving,
                is PessoaFormUiState.Deleting -> {
                    Box(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.5.dp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                is PessoaFormUiState.Loading, is PessoaFormUiState.Error -> {}
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun PessoaFormScreenLoadingPreview() {
    MaterialTheme {
        PessoaFormScreen(
            uiState = PessoaFormUiState.Loading(
                isEditing = true,
                message = "Carregando dados da pessoa..."
            ),
            onBackClick = {},
            onRetry = {},
            onSaveClick = {},
            onPessoaChange = {},
            onDeleteClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PessoaFormScreenErrorPreview() {
    MaterialTheme {
        PessoaFormScreen(
            uiState = PessoaFormUiState.Error(
                isEditing = true,
                message = "Não foi possível carregar os dados para edição."
            ),
            onBackClick = {},
            onRetry = {},
            onSaveClick = {},
            onPessoaChange = {},
            onDeleteClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PessoaFormScreenSuccessInsertPreview() {
    MaterialTheme {
        PessoaFormScreen(
            uiState = PessoaFormUiState.Success(
                isEditing = false,
                pessoa = Pessoa()
            ),
            onBackClick = {},
            onRetry = {},
            onSaveClick = {},
            onPessoaChange = {},
            onDeleteClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PessoaFormScreenSuccessEditPreview() {
    MaterialTheme {
        PessoaFormScreen(
            uiState = PessoaFormUiState.Success(
                isEditing = true,
                pessoa = Pessoa(
                    id = 1,
                    nome = "Mariana Alcantara",
                    cpf = "123.456.789-00",
                    telefone = "(41) 99887-6655",
                    endereco = Endereco(
                        cep = "80020-310",
                        logradouro = "Rua Marechal Deodoro",
                        numero = 500,
                        complemento = "Conjunto 401",
                        bairro = "Centro",
                        cidade = "Curitiba"
                    )
                )
            ),
            onBackClick = {},
            onRetry = {},
            onSaveClick = {},
            onPessoaChange = {},
            onDeleteClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PessoaFormScreenSavingPreview() {
    MaterialTheme {
        PessoaFormScreen(
            uiState = PessoaFormUiState.Saving(
                isEditing = false,
                pessoa = Pessoa(
                    nome = "Mariana Alcantara",
                    cpf = "123.456.789-00",
                    telefone = "(41) 99887-6655",
                    endereco = Endereco(
                        cep = "80020-310",
                        logradouro = "Rua Marechal Deodoro",
                        numero = 500,
                        complemento = "Conjunto 401",
                        bairro = "Centro",
                        cidade = "Curitiba"
                    )
                )
            ),
            onBackClick = {},
            onRetry = {},
            onSaveClick = {},
            onPessoaChange = {},
            onDeleteClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PessoaFormScreenSaveErrorPreview() {
    MaterialTheme {
        PessoaFormScreen(
            uiState = PessoaFormUiState.SaveError(
                isEditing = false,
                pessoa = Pessoa(
                    nome = "Mariana Alcantara",
                    cpf = "123.456.789-00",
                    telefone = "(41) 99887-6655",
                    endereco = Endereco(
                        cep = "80020-310",
                        logradouro = "Rua Marechal Deodoro",
                        numero = 500,
                        complemento = "Conjunto 401",
                        bairro = "Centro",
                        cidade = "Curitiba"
                    )
                ),
                message = "Erro ao salvar: verifique os dados informados e tente novamente."
            ),
            onBackClick = {},
            onRetry = {},
            onSaveClick = {},
            onPessoaChange = {},
            onDeleteClick = {}
        )
    }
}