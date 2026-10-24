package br.edu.utfpr.cadastropessoascmp.extensions

fun String.apenasDigitos(maxDigitos: Int = length): String =
    filter { it.isDigit() }.take(maxDigitos)