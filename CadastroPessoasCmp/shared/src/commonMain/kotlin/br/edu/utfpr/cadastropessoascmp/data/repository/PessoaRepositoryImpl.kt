package br.edu.utfpr.cadastropessoascmp.data.repository

import br.edu.utfpr.cadastropessoascmp.data.local.dao.EnderecoDao
import br.edu.utfpr.cadastropessoascmp.data.local.dao.PessoaDao
import br.edu.utfpr.cadastropessoascmp.data.local.entity.toEntity
import br.edu.utfpr.cadastropessoascmp.domain.model.Pessoa
import br.edu.utfpr.cadastropessoascmp.domain.repository.PessoaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PessoaRepositoryImpl(
    private val pessoaDao: PessoaDao,
    private val enderecoDao: EnderecoDao
) : PessoaRepository {

    override fun observar(): Flow<List<Pessoa>> {
        return pessoaDao.getAllWithEndereco().map { lista ->
            lista.map { it.toDomain() }
        }
    }

    override suspend fun buscarPorId(id: Int): Pessoa? {
        return pessoaDao.getWithEnderecoById(id)?.toDomain()
    }

    override suspend fun salvar(pessoa: Pessoa): Long {
        if (pessoa.id > 0) {
            atualizar(pessoa)
            return pessoa.id.toLong()
        }
        val pessoaId = pessoaDao.insert(pessoa.toEntity())
        val id = pessoaId.toInt()
        val enderecoEntity = pessoa.endereco.toEntity(
            pessoaId = id
        )
        enderecoDao.insert(enderecoEntity)
        return pessoaId
    }

    override suspend fun atualizar(pessoa: Pessoa) {
        pessoaDao.update(pessoa.toEntity())
        val enderecoExistente = enderecoDao.getByPessoaId(pessoa.id)
        val enderecoEntity = pessoa.endereco.toEntity(
            pessoaId = pessoa.id,
            id = enderecoExistente?.id ?: 0
        )
        if (enderecoExistente != null) {
            enderecoDao.update(enderecoEntity)
        } else {
            enderecoDao.insert(enderecoEntity)
        }
    }

    override suspend fun deletar(pessoa: Pessoa) {
        pessoaDao.delete(pessoa.toEntity())
    }
}