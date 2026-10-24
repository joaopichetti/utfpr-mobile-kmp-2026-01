package br.edu.utfpr.cadastropessoascmp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import br.edu.utfpr.cadastropessoascmp.ui.form.PessoaFormScreen
import br.edu.utfpr.cadastropessoascmp.ui.list.PessoasListScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: Screen = Screen.List
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<Screen.List> {
            PessoasListScreen(
                onAddClick = {
                    navController.navigate(Screen.Form())
                },
                onPessoaClick = { pessoa ->
                    navController.navigate(Screen.Form(pessoaId = pessoa.id))
                }
            )
        }
        composable<Screen.Form> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.Form>()
            val pessoaId = route.pessoaId

            PessoaFormScreen(
                pessoaId = pessoaId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}