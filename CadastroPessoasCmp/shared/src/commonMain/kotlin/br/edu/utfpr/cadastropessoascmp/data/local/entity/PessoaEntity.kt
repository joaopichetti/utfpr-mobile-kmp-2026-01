package br.edu.utfpr.cadastropessoascmp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.edu.utfpr.cadastropessoascmp.domain.model.Endereco
import br.edu.utfpr.cadastropessoascmp.domain.model.Pessoa

@Entity(tableName = "pessoas")
data class PessoaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val cpf: String,
    val telefone: String
) {
    fun toDomain(endereco: Endereco = Endereco()): Pessoa {
        return Pessoa(
            id = id,
            nome = nome,
            cpf = cpf,
            telefone = telefone,
            endereco = endereco
        )
    }
}

fun Pessoa.toEntity(): PessoaEntity {
    return PessoaEntity(
        id = id,
        nome = nome,
        cpf = cpf,
        telefone = telefone
    )
}
