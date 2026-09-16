package org.prueba

sealed class EstadoPedido {
    object Pendientes : EstadoPedido()
    object EnPreparacion: EstadoPedido()
    object Listo : EstadoPedido()

    data class Error(val mensaje: String) : EstadoPedido()
}