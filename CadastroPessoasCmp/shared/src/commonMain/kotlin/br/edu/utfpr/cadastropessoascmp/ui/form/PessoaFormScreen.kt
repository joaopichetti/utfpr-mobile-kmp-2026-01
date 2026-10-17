package br.edu.utfpr.cadastropessoascmp.ui.form

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.edu.utfpr.cadastropessoascmp.domain.model.Endereco
import br.edu.utfpr.cadastropessoascmp.domain.model.Pessoa
import br.edu.utfpr.cadastropessoascmp.ui.components.ErrorComponent
import br.edu.utfpr.cadastropessoascmp.ui.components.LoadingComponent
import br.edu.utfpr.cadastropessoascmp.ui.form.components.PessoaFormSuccess

@Composable
fun PessoaFormScreen(
    modifier: Modifier = Modifier,
    uiState: PessoaFormUiState,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onRetry: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    LaunchedEffect(uiState) {
        if (uiState is PessoaFormUiState.SaveError) {
            snackbarHostState.showSnackbar(uiState.message)
        }
    }

    val titleText = if (uiState.isEditing) "Editar Pessoa" else "Cadastrar Pessoa"

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            PessoaFormTopBar(
                title = titleText,
                uiState = uiState,
                onBackClick = onBackClick,
                onSaveClick = onSaveClick
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
                        enabled = true
                    )
                }
                is PessoaFormUiState.Saving -> {
                    PessoaFormSuccess(
                        pessoa = uiState.pessoa,
                        enabled = false
                    )
                }
                is PessoaFormUiState.SaveError -> {
                    PessoaFormSuccess(
                        pessoa = uiState.pessoa,
                        enabled = true
                    )
                }
            }
        }
    }
}

@Composable
private fun PessoaFormTopBar(
    modifier: Modifier = Modifier,
    title: String,
    uiState: PessoaFormUiState,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = { Text(title) },
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
                is PessoaFormUiState.Success, is PessoaFormUiState.SaveError -> {
                    IconButton(onClick = onSaveClick) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Salvar"
                        )
                    }
                }
                is PessoaFormUiState.Saving -> {
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
            onSaveClick = {}
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
            onSaveClick = {}
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
            onSaveClick = {}
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
            onSaveClick = {}
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
            onSaveClick = {}
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
            onSaveClick = {}
        )
    }
}