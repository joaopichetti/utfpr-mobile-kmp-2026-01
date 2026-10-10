package br.edu.utfpr.shared.data.remote

import br.edu.utfpr.shared.data.model.EnderecoResponse

interface ViaCepApi {
    suspend fun buscarCep(cep: String): EnderecoResponse
}