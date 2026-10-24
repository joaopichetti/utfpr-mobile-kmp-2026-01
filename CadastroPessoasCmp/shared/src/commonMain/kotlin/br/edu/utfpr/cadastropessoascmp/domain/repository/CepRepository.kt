package br.edu.utfpr.cadastropessoascmp.domain.repository

import br.edu.utfpr.cadastropessoascmp.domain.model.Endereco

interface CepRepository {
    suspend fun buscarCep(cep: String): Result<Endereco>
}