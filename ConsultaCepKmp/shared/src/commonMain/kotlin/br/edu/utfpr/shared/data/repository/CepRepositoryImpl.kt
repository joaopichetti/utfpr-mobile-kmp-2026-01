package br.edu.utfpr.shared.data.repository

import br.edu.utfpr.shared.data.remote.ViaCepApi
import br.edu.utfpr.shared.data.remote.ViaCepApiImpl
import br.edu.utfpr.shared.domain.model.Endereco
import br.edu.utfpr.shared.domain.repository.CepRepository

class CepRepositoryImpl(
    private val api: ViaCepApi = ViaCepApiImpl()
) : CepRepository {

    override suspend fun buscarCep(cep: String): Result<Endereco> {
        return runCatching {
            val response = api.buscarCep(cep)
            if (response.erro.equals("true", ignoreCase = true)) {
                throw NoSuchElementException("CEP não encontrado.")
            }
            response.toDomain()
        }
    }
}