package br.edu.utfpr.cadastropessoascmp.di

import androidx.room.RoomDatabase
import br.edu.utfpr.cadastropessoascmp.data.local.database.AppDatabase
import br.edu.utfpr.cadastropessoascmp.data.local.database.getDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        getDatabaseBuilder()
    }
}