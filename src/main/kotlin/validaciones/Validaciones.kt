package org.prueba.validaciones

import java.io.EOFException

fun leerTexto(): String{
    val texto: String? = readlnOrNull()

    if (texto == null){
        throw  EOFException("Se termio la entrada de consola.")
    }
    return texto
}