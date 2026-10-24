package br.edu.utfpr.cadastropessoascmp

import android.app.Application
import br.edu.utfpr.cadastropessoascmp.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class CadastroPessoasApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@CadastroPessoasApplication)
        }
    }
}