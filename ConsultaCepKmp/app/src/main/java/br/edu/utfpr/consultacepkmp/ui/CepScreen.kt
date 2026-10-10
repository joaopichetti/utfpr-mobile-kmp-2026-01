package br.edu.utfpr.consultacepkmp.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.utfpr.consultacepkmp.domain.model.Endereco
import br.edu.utfpr.consultacepkmp.ui.theme.ConsultaCepKmpTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CepScreen(
    modifier: Modifier = Modifier,
    viewModel: CepViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val cepInput by viewModel.cepInput.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Consulta de CEP") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Seção de busca (Topo)
            OutlinedTextField(
                value = cepInput,
                onValueChange = { viewModel.onCepChange(it) },
                label = { Text("CEP") },
                placeholder = { Text("Ex: 01001000") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        viewModel.buscarCep()
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    viewModel.buscarCep()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is CepUiState.Loading
            ) {
                Text(text = "Consultar CEP")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Seção de conteúdo reativo com animação suave
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.TopCenter
            ) {
                AnimatedContent(
                    targetState = uiState,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "CepUiStateAnimation"
                ) { state ->
                    when (state) {
                        is CepUiState.Idle -> IdleStateContent()
                        is CepUiState.Loading -> LoadingStateContent()
                        is CepUiState.Error -> ErrorStateContent(mensagem = state.mensagem)
                        is CepUiState.Success -> SuccessStateContent(endereco = state.endereco)
                    }
                }
            }
        }
    }
}

@Composable
private fun IdleStateContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Digite um CEP de 8 dígitos para consultar",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun LoadingStateContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorStateContent(mensagem: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Erro",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.padding(start = 12.dp))
            Column {
                Text(
                    text = "Atenção",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = mensagem,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun SuccessStateContent(endereco: Endereco) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.padding(start = 8.dp))
                Text(
                    text = "Endereço Encontrado",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            EnderecoItem(label = "Logradouro", valor = endereco.logradouro.ifBlank { "Não informado" })
            EnderecoItem(label = "Bairro", valor = endereco.bairro.ifBlank { "Não informado" })
            EnderecoItem(label = "Cidade/UF", valor = "${endereco.cidade} - ${endereco.uf}".trim('-', ' '))
            EnderecoItem(label = "CEP", valor = formatarCep(endereco.cep))
        }
    }
}

@Composable
private fun EnderecoItem(label: String, valor: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun formatarCep(cep: String): String {
    val digitsOnly = cep.filter { it.isDigit() }
    return if (digitsOnly.length == 8) {
        "${digitsOnly.substring(0, 5)}-${digitsOnly.substring(5)}"
    } else {
        cep
    }
}

@Preview(showBackground = true)
@Composable
fun SuccessPreview() {
    ConsultaCepKmpTheme {
        SuccessStateContent(
            endereco = Endereco(
                cep = "01001-000",
                logradouro = "Praça da Sé",
                bairro = "Sé",
                cidade = "São Paulo",
                uf = "SP"
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorPreview() {
    ConsultaCepKmpTheme {
        ErrorStateContent(mensagem = "CEP não encontrado.")
    }
}