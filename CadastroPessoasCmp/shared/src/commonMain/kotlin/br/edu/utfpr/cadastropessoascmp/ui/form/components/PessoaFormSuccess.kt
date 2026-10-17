package br.edu.utfpr.cadastropessoascmp.ui.form.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.edu.utfpr.cadastropessoascmp.domain.model.Endereco
import br.edu.utfpr.cadastropessoascmp.domain.model.Pessoa

@Composable
fun PessoaFormSuccess(
    modifier: Modifier = Modifier,
    pessoa: Pessoa = Pessoa(),
    enabled: Boolean = true
) {
    var nome by remember(pessoa.nome) { mutableStateOf(pessoa.nome) }
    var cpf by remember(pessoa.cpf) { mutableStateOf(pessoa.cpf) }
    var telefone by remember(pessoa.telefone) { mutableStateOf(pessoa.telefone) }

    var cep by remember(pessoa.endereco.cep) { mutableStateOf(pessoa.endereco.cep) }
    var logradouro by remember(pessoa.endereco.logradouro) { mutableStateOf(pessoa.endereco.logradouro) }
    var numero by remember(pessoa.endereco.numero) {
        mutableStateOf(if (pessoa.endereco.numero == 0) "" else pessoa.endereco.numero.toString())
    }
    var complemento by remember(pessoa.endereco.complemento) { mutableStateOf(pessoa.endereco.complemento) }
    var bairro by remember(pessoa.endereco.bairro) { mutableStateOf(pessoa.endereco.bairro) }
    var cidade by remember(pessoa.endereco.cidade) { mutableStateOf(pessoa.endereco.cidade) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FormSectionTitle(title = "Dados Gerais")

        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome") },
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = cpf,
            onValueChange = { cpf = it },
            label = { Text("CPF") },
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = telefone,
            onValueChange = { telefone = it },
            label = { Text("Telefone") },
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        FormSectionTitle(title = "Endereço")

        OutlinedTextField(
            value = cep,
            onValueChange = { cep = it },
            label = { Text("CEP") },
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = logradouro,
            onValueChange = { logradouro = it },
            label = { Text("Logradouro") },
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = numero,
            onValueChange = { numero = it },
            label = { Text("Número") },
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = complemento,
            onValueChange = { complemento = it },
            label = { Text("Complemento") },
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = bairro,
            onValueChange = { bairro = it },
            label = { Text("Bairro") },
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = cidade,
            onValueChange = { cidade = it },
            label = { Text("Cidade") },
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun FormSectionTitle(
    modifier: Modifier = Modifier,
    title: String
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PessoaFormSuccessEmptyPreview() {
    MaterialTheme {
        PessoaFormSuccess(
            pessoa = Pessoa()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PessoaFormSuccessFilledPreview() {
    MaterialTheme {
        PessoaFormSuccess(
            pessoa = Pessoa(
                id = 1,
                nome = "João da Silva",
                cpf = "123.456.789-00",
                telefone = "(41) 99999-1111",
                endereco = Endereco(
                    cep = "80000-000",
                    logradouro = "Rua das Flores",
                    numero = 100,
                    complemento = "Apto 302",
                    bairro = "Centro",
                    cidade = "Curitiba"
                )
            )
        )
    }
}