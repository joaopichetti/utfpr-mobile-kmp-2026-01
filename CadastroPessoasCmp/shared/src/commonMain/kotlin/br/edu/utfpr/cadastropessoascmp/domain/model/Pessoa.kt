package br.edu.utfpr.cadastropessoascmp.domain.model

data class Pessoa(
    val id: Int = 0,
    val nome: String = "",
    val cpf: String = "",
    val telefone: String = "",
    val endereco: Endereco = Endereco()
)
