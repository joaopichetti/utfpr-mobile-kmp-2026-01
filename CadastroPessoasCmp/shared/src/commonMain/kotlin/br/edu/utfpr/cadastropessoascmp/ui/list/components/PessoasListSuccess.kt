package br.edu.utfpr.cadastropessoascmp.ui.list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.edu.utfpr.cadastropessoascmp.domain.model.Endereco
import br.edu.utfpr.cadastropessoascmp.domain.model.Pessoa

@Composable
fun PessoasListSuccess(
    modifier: Modifier = Modifier,
    pessoas: List<Pessoa>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onPessoaClick: (Pessoa) -> Unit
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize()
    ) {
        if (pessoas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Lista vazia",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Nenhuma pessoa cadastrada.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(pessoas, key = { it.id } ) { pessoa ->
                    ListItem(
                        modifier = Modifier.clickable { onPessoaClick(pessoa) },
                        headlineContent = {
                            Text(
                                text = pessoa.nome,
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        supportingContent = {
                            Text(
                                text = pessoa.endereco.descricao,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PessoasListSuccessEmptyPreview() {
    MaterialTheme {
        PessoasListSuccess(
            pessoas = emptyList(),
            isRefreshing = false,
            onRefresh = {},
            onPessoaClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PessoasListSuccessWithItemsPreview() {
    MaterialTheme {
        PessoasListSuccess(
            pessoas = listOf(
                Pessoa(
                    id = 1,
                    nome = "Ana Souza",
                    cpf = "111.222.333-44",
                    telefone = "(41) 91111-2222",
                    endereco = Endereco(
                        logradouro = "Rua XV de Novembro",
                        numero = 150,
                        cidade = "Curitiba"
                    )
                ),
                Pessoa(
                    id = 2,
                    nome = "Carlos Eduardo",
                    cpf = "555.666.777-88",
                    telefone = "(41) 93333-4444",
                    endereco = Endereco(
                        logradouro = "Av. Brasil",
                        numero = 1020,
                        cidade = "Maringá"
                    )
                )
            ),
            isRefreshing = false,
            onRefresh = {},
            onPessoaClick = {}
        )
    }
}