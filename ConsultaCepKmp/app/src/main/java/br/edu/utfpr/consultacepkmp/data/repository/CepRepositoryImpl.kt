package br.edu.utfpr.consultacepkmp.data.repository

import br.edu.utfpr.consultacepkmp.data.remote.RetrofitClient
import br.edu.utfpr.consultacepkmp.data.remote.ViaCepApi
import br.edu.utfpr.consultacepkmp.domain.model.Endereco
import br.edu.utfpr.consultacepkmp.domain.repository.CepRepository

class CepRepositoryImpl(
    private val api: ViaCepApi = RetrofitClient.viaCepApi
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