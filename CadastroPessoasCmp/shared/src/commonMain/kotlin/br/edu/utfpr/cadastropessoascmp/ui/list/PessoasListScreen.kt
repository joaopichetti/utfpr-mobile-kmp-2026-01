package br.edu.utfpr.cadastropessoascmp.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.edu.utfpr.cadastropessoascmp.domain.model.Endereco
import br.edu.utfpr.cadastropessoascmp.domain.model.Pessoa
import br.edu.utfpr.cadastropessoascmp.ui.components.ErrorComponent
import br.edu.utfpr.cadastropessoascmp.ui.components.LoadingComponent
import br.edu.utfpr.cadastropessoascmp.ui.list.components.PessoasListSuccess

@Composable
fun PessoasListScreen(
    modifier: Modifier = Modifier,
    uiState: PessoasListUiState,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    onAddClick: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Cadastro de Pessoas"
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            if (uiState is PessoasListUiState.Success) {
                FloatingActionButton(onClick = onAddClick) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Adicionar Pessoa"
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                is PessoasListUiState.Loading -> {
                    LoadingComponent(
                        message = "Carregando pessoas..."
                    )
                }
                is PessoasListUiState.Error -> {
                    ErrorComponent(
                        message = uiState.message,
                        onRetry = onRetry
                    )
                }
                is PessoasListUiState.Success -> {
                    PessoasListSuccess(
                        pessoas = uiState.pessoas,
                        isRefreshing = uiState.isRefreshing,
                        onRefresh = onRefresh
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PessoasListScreenLoadingPreview() {
    MaterialTheme {
        PessoasListScreen(
            uiState = PessoasListUiState.Loading,
            onRefresh = {},
            onRetry = {},
            onAddClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PessoasListScreenErrorPreview() {
    MaterialTheme {
        PessoasListScreen(
            uiState = PessoasListUiState.Error("Falha de conexão com o servidor."),
            onRefresh = {},
            onRetry = {},
            onAddClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PessoasListScreenEmptyPreview() {
    MaterialTheme {
        PessoasListScreen(
            uiState = PessoasListUiState.Success(pessoas = emptyList()),
            onRefresh = {},
            onRetry = {},
            onAddClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PessoasListScreenSuccessPreview() {
    MaterialTheme {
        PessoasListScreen(
            uiState = PessoasListUiState.Success(
                pessoas = listOf(
                    Pessoa(
                        id = 1,
                        nome = "João da Silva",
                        cpf = "123.456.789-00",
                        telefone = "(41) 99999-1111",
                        endereco = Endereco(
                            logradouro = "Rua das Flores",
                            numero = 100,
                            cidade = "Curitiba"
                        )
                    ),
                    Pessoa(
                        id = 2,
                        nome = "Maria Oliveira",
                        cpf = "987.654.321-99",
                        telefone = "(41) 98888-2222",
                        endereco = Endereco(
                            logradouro = "Av. Sete de Setembro",
                            numero = 2500,
                            cidade = "Curitiba"
                        )
                    )
                )
            ),
            onRefresh = {},
            onRetry = {},
            onAddClick = {}
        )
    }
}