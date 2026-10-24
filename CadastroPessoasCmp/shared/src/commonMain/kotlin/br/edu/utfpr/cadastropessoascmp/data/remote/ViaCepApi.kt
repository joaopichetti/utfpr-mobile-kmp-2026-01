package br.edu.utfpr.cadastropessoascmp.data.remote

import br.edu.utfpr.cadastropessoascmp.data.model.EnderecoResponse

interface ViaCepApi {
    suspend fun buscarCep(cep: String): EnderecoResponse
}