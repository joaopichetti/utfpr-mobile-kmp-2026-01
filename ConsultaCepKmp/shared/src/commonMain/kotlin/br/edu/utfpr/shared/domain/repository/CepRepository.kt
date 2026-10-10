package br.edu.utfpr.shared.domain.repository

import br.edu.utfpr.shared.domain.model.Endereco

interface CepRepository {
    suspend fun buscarCep(cep: String): Result<Endereco>
}