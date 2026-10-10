package br.edu.utfpr.shared

actual fun platform(): String = "${System.getProperty("os.name")} ${System.getProperty("os.version")}"