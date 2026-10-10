package br.edu.utfpr.consultacepkmp.data.remote

import br.edu.utfpr.consultacepkmp.data.model.EnderecoResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ViaCepApi {
    @GET("{cep}/json/")
    suspend fun buscarCep(@Path("cep") cep: String): EnderecoResponse
}