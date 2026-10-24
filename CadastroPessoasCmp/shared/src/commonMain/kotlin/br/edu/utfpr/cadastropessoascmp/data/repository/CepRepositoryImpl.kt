package br.edu.utfpr.cadastropessoascmp.data.repository

import br.edu.utfpr.cadastropessoascmp.data.model.EnderecoResponse
import br.edu.utfpr.cadastropessoascmp.data.remote.ViaCepApi
import br.edu.utfpr.cadastropessoascmp.domain.model.Endereco
import br.edu.utfpr.cadastropessoascmp.domain.repository.CepRepository

class CepRepositoryImpl(
    private val api: ViaCepApi
) : CepRepository {
    override suspend fun buscarCep(cep: String): Result<Endereco> {
        return runCatching {
            val response: EnderecoResponse = api.buscarCep(cep)
            if (response.erro.equals("true", ignoreCase = true)) {
                throw NoSuchElementException("CEP não encontrado")
            }
            response.toDomain()
        }
    }
}