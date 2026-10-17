package br.edu.utfpr.cadastropessoascmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform