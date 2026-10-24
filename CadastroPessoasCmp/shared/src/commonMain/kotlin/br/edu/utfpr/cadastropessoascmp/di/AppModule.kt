package br.edu.utfpr.cadastropessoascmp.di

import br.edu.utfpr.cadastropessoascmp.data.local.dao.EnderecoDao
import br.edu.utfpr.cadastropessoascmp.data.local.dao.PessoaDao
import br.edu.utfpr.cadastropessoascmp.data.local.database.AppDatabase
import br.edu.utfpr.cadastropessoascmp.data.local.database.getRoomDatabase
import br.edu.utfpr.cadastropessoascmp.data.remote.ViaCepApi
import br.edu.utfpr.cadastropessoascmp.data.remote.ViaCepApiImpl
import br.edu.utfpr.cadastropessoascmp.data.remote.createHttpClient
import br.edu.utfpr.cadastropessoascmp.data.repository.CepRepositoryImpl
import br.edu.utfpr.cadastropessoascmp.data.repository.PessoaRepositoryImpl
import br.edu.utfpr.cadastropessoascmp.domain.repository.CepRepository
import br.edu.utfpr.cadastropessoascmp.domain.repository.PessoaRepository
import br.edu.utfpr.cadastropessoascmp.ui.form.PessoaFormViewModel
import br.edu.utfpr.cadastropessoascmp.ui.list.PessoasListViewModel
import io.ktor.client.HttpClient
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> { getRoomDatabase(get()) }
    single<PessoaDao> { get<AppDatabase>().pessoaDao() }
    single<EnderecoDao> { get<AppDatabase>().enderecoDao() }
}

val networkModule = module {
    single<HttpClient> { createHttpClient() }
    singleOf(::ViaCepApiImpl) bind ViaCepApi::class
}

val repositoryModule = module {
    singleOf(::PessoaRepositoryImpl) bind PessoaRepository::class
    singleOf(::CepRepositoryImpl) bind CepRepository::class
}

val viewModelModule = module {
    viewModelOf(::PessoasListViewModel)
    viewModel { params ->
        PessoaFormViewModel(
            pessoaId = params.getOrNull(),
            pessoaRepository = get(),
            cepRepository = get()
        )
    }
}

val appModules = listOf(
    platformModule,
    databaseModule,
    networkModule,
    repositoryModule,
    viewModelModule
)

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModules)
    }
}