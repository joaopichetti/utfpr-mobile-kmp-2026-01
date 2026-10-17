package br.edu.utfpr.cadastropessoascmp.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import br.edu.utfpr.cadastropessoascmp.domain.model.Endereco
import br.edu.utfpr.cadastropessoascmp.domain.model.Pessoa

data class PessoaWithEndereco(
    @Embedded
    val pessoa: PessoaEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "pessoaId"
    )
    val endereco: EnderecoEntity?
) {
    fun toDomain(): Pessoa {
        return pessoa.toDomain(
            endereco = endereco?.toDomain() ?: Endereco()
        )
    }
}