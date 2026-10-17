package br.edu.utfpr.cadastropessoascmp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import br.edu.utfpr.cadastropessoascmp.domain.model.Pessoa
import br.edu.utfpr.cadastropessoascmp.ui.form.PessoaFormScreen
import br.edu.utfpr.cadastropessoascmp.ui.form.PessoaFormUiState
import br.edu.utfpr.cadastropessoascmp.ui.list.PessoasListScreen
import br.edu.utfpr.cadastropessoascmp.ui.list.PessoasListUiState

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: Screen = Screen.List,
    pessoas: List<Pessoa>
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<Screen.List> {
            PessoasListScreen(
                uiState = PessoasListUiState.Success(pessoas = pessoas),
                onAddClick = {
                    navController.navigate(Screen.Form())
                },
                onPessoaClick = { pessoa ->
                    navController.navigate(Screen.Form(pessoaId = pessoa.id))
                },
                onRetry = {},
                onRefresh = {}
            )
        }
        composable<Screen.Form> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.Form>()
            val pessoaId = route.pessoaId
            val isEditing = pessoaId != null

            val existingPessoa = if (isEditing) {
                pessoas.find { it.id == pessoaId } ?: Pessoa(id = pessoaId)
            } else {
                Pessoa()
            }

            PessoaFormScreen(
                uiState = PessoaFormUiState.Success(
                    isEditing = isEditing,
                    pessoa = existingPessoa
                ),
                onBackClick = {
                    navController.popBackStack()
                },
                onRetry = {},
                onSaveClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}