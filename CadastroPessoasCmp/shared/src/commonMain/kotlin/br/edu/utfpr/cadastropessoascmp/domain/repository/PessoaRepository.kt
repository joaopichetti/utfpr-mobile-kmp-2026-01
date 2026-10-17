package br.edu.utfpr.cadastropessoascmp.domain.repository

import br.edu.utfpr.cadastropessoascmp.domain.model.Pessoa
import kotlinx.coroutines.flow.Flow

interface PessoaRepository {
    fun observar(): Flow<List<Pessoa>>
    suspend fun buscarPorId(id: Int): Pessoa?
    suspend fun salvar(pessoa: Pessoa): Long
    suspend fun atualizar(pessoa: Pessoa)
    suspend fun deletar(pessoa: Pessoa)
}