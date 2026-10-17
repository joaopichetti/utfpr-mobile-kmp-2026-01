package br.edu.utfpr.cadastropessoascmp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import br.edu.utfpr.cadastropessoascmp.data.local.entity.PessoaEntity
import br.edu.utfpr.cadastropessoascmp.data.local.entity.PessoaWithEndereco
import kotlinx.coroutines.flow.Flow

@Dao
interface PessoaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pessoa: PessoaEntity): Long

    @Update
    suspend fun update(pessoa: PessoaEntity)

    @Delete
    suspend fun delete(pessoa: PessoaEntity)

    @Transaction
    @Query("SELECT * FROM pessoas")
    fun getAllWithEndereco(): Flow<List<PessoaWithEndereco>>

    @Transaction
    @Query("SELECT * FROM pessoas WHERE id = :id")
    suspend fun getWithEnderecoById(id: Int): PessoaWithEndereco?
}