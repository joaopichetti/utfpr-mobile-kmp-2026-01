package br.edu.utfpr.cadastropessoascmp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.edu.utfpr.cadastropessoascmp.data.local.entity.EnderecoEntity

@Dao
interface EnderecoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(endereco: EnderecoEntity): Long

    @Update
    suspend fun update(endereco: EnderecoEntity)

    @Query("SELECT * FROM enderecos WHERE pessoaId = :pessoaId LIMIT 1")
    suspend fun getByPessoaId(pessoaId: Int): EnderecoEntity?
}