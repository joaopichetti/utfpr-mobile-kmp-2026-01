package br.edu.utfpr.cadastropessoascmp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import br.edu.utfpr.cadastropessoascmp.domain.model.Endereco

@Entity(
    tableName = "enderecos",
    foreignKeys = [
        ForeignKey(
            entity = PessoaEntity::class,
            parentColumns = ["id"],
            childColumns = ["pessoaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["pessoaId"], unique = true)
    ]
)
data class EnderecoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pessoaId: Int = 0,
    val cep: String = "",
    val logradouro: String = "",
    val numero: Int = 0,
    val complemento: String = "",
    val bairro: String = "",
    val cidade: String = ""
) {
    fun toDomain(): Endereco {
        return Endereco(
            cep = cep,
            logradouro = logradouro,
            numero = numero,
            complemento = complemento,
            bairro = bairro,
            cidade = cidade
        )
    }
}

fun Endereco.toEntity(pessoaId: Int, id: Int = 0): EnderecoEntity {
    return EnderecoEntity(
        id = id,
        pessoaId = pessoaId,
        cep = cep,
        logradouro = logradouro,
        numero = numero,
        complemento = complemento,
        bairro = bairro,
        cidade = cidade
    )
}
