package br.edu.utfpr.cadastropessoascmp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import br.edu.utfpr.cadastropessoascmp.domain.model.Endereco
import br.edu.utfpr.cadastropessoascmp.domain.model.Pessoa
import br.edu.utfpr.cadastropessoascmp.ui.list.PessoasListScreen
import br.edu.utfpr.cadastropessoascmp.ui.list.PessoasListUiState

@Composable
@Preview
fun App() {
    MaterialTheme {
        val samplePessoas = listOf(
            Pessoa(
                id = 1,
                nome = "João Silva",
                cpf = "123.456.789-00",
                telefone = "(41) 99999-1111",
                endereco = Endereco(
                    logradouro = "Rua das Flores",
                    numero = 100,
                    cidade = "Curitiba",
                    bairro = "Centro",
                    cep = "80000-000"
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
                    cidade = "Curitiba",
                    bairro = "Batel",
                    cep = "80240-000"
                )
            )
        )

        PessoasListScreen(
            uiState = PessoasListUiState.Success(pessoas = samplePessoas),
            onRefresh = {},
            onRetry = {},
            onAddClick = {}
        )
    }
}