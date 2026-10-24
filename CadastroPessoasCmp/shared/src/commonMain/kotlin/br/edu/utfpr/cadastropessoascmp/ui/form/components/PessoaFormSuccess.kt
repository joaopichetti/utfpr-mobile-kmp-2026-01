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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.edu.utfpr.cadastropessoascmp.domain.model.Endereco
import br.edu.utfpr.cadastropessoascmp.domain.model.Pessoa
import br.edu.utfpr.cadastropessoascmp.extensions.apenasDigitos
import br.edu.utfpr.cadastropessoascmp.ui.form.PessoaFormErrors
import br.edu.utfpr.cadastropessoascmp.ui.form.visualtransformation.MaskVisualTransformation
import br.edu.utfpr.cadastropessoascmp.ui.form.visualtransformation.TelefoneVisualTransformation

@Composable
fun PessoaFormSuccess(
    modifier: Modifier = Modifier,
    pessoa: Pessoa = Pessoa(),
    enabled: Boolean = true,
    errors: PessoaFormErrors = PessoaFormErrors(),
    onPessoaChange: (Pessoa) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FormSectionTitle(title = "Dados Gerais")

        OutlinedTextField(
            value = pessoa.nome,
            onValueChange = { onPessoaChange(pessoa.copy(nome = it)) },
            label = { Text("Nome") },
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
            ),
            modifier = Modifier.fillMaxWidth(),
            isError = errors.nome != null,
            supportingText = errors.nome?.let { error -> { Text(error) } }
        )

        OutlinedTextField(
            value = pessoa.cpf,
            onValueChange = { novoValor ->
                onPessoaChange(pessoa.copy(cpf = novoValor.apenasDigitos(11)))
            },
            label = { Text("CPF") },
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth(),
            isError = errors.cpf != null,
            supportingText = errors.cpf?.let { error -> { Text(error) } },
            visualTransformation = MaskVisualTransformation.Cpf
        )

        OutlinedTextField(
            value = pessoa.telefone,
            onValueChange = { novoValor ->
                onPessoaChange(pessoa.copy(telefone = novoValor.apenasDigitos(11)))
            },
            label = { Text("Telefone") },
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            ),
            modifier = Modifier.fillMaxWidth(),
            isError = errors.telefone != null,
            supportingText = errors.telefone?.let { error -> { Text(error) } },
            visualTransformation = TelefoneVisualTransformation
        )

        Spacer(Modifier.height(8.dp))

        FormSectionTitle(title = "Endereço")

        OutlinedTextField(
            value = pessoa.endereco.cep,
            onValueChange = { novoValor ->
                onPessoaChange(pessoa.copy(
                    endereco = pessoa.endereco.copy(cep = novoValor.apenasDigitos(8))
                ))
            },
            label = { Text("CEP") },
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth(),
            isError = errors.cep != null,
            supportingText = errors.cep?.let { error -> { Text(error) } },
            visualTransformation = MaskVisualTransformation.Cep
        )

        OutlinedTextField(
            value = pessoa.endereco.logradouro,
            onValueChange = {
                onPessoaChange(pessoa.copy(
                    endereco = pessoa.endereco.copy(logradouro = it)
                ))
            },
            label = { Text("Logradouro") },
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
            ),
            modifier = Modifier.fillMaxWidth(),
            isError = errors.logradouro != null,
            supportingText = errors.logradouro?.let { error -> { Text(error) } }
        )

        OutlinedTextField(
            value = if (pessoa.endereco.numero <= 0) "" else pessoa.endereco.numero.toString(),
            onValueChange = {
                onPessoaChange(pessoa.copy(
                    endereco = pessoa.endereco.copy(
                        numero = it.toIntOrNull() ?: 0
                    )
                ))
            },
            label = { Text("Número") },
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth(),
            isError = errors.numero != null,
            supportingText = errors.numero?.let { error -> { Text(error) } }
        )

        OutlinedTextField(
            value = pessoa.endereco.complemento,
            onValueChange = {
                onPessoaChange(pessoa.copy(
                    endereco = pessoa.endereco.copy(complemento = it)
                ))
            },
            label = { Text("Complemento") },
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = pessoa.endereco.bairro,
            onValueChange = {
                onPessoaChange(pessoa.copy(
                    endereco = pessoa.endereco.copy(bairro = it)
                ))
            },
            label = { Text("Bairro") },
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = pessoa.endereco.cidade,
            onValueChange = {
                onPessoaChange(pessoa.copy(
                    endereco = pessoa.endereco.copy(cidade = it)
                ))
            },
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
            pessoa = Pessoa(),
            onPessoaChange = {}
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
                cpf = "12345678900",
                telefone = "41999991111",
                endereco = Endereco(
                    cep = "80000000",
                    logradouro = "Rua das Flores",
                    numero = 100,
                    complemento = "Apto 302",
                    bairro = "Centro",
                    cidade = "Curitiba"
                )
            ),
            onPessoaChange = {}
        )
    }
}