package br.edu.utfpr.cadastropessoascmp.data.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import br.edu.utfpr.cadastropessoascmp.data.local.dao.EnderecoDao
import br.edu.utfpr.cadastropessoascmp.data.local.dao.PessoaDao
import br.edu.utfpr.cadastropessoascmp.data.local.entity.EnderecoEntity
import br.edu.utfpr.cadastropessoascmp.data.local.entity.PessoaEntity

@Database(
    entities = [
        PessoaEntity::class,
        EnderecoEntity::class
    ],
    version = 1
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pessoaDao(): PessoaDao
    abstract fun enderecoDao(): EnderecoDao
}

// O compilador KSP do Room gera as implementações `actual` nas plataformas de destino.
@Suppress("KotlinNoActualForExpect", "NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}