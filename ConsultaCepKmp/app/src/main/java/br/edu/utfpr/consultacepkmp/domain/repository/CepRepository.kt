package br.edu.utfpr.consultacepkmp.domain.repository

import br.edu.utfpr.consultacepkmp.domain.model.Endereco

interface CepRepository {
    suspend fun buscarCep(cep: String): Result<Endereco>
}