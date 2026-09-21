package org.prueba

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val catalogo = crearCatalogo()
    val ventas = mutableListOf<ResumenPedido>()
    println("=== SISTEMA FOODEXPRESS ===")
    println("Promocion: lleva 3 o más productos y recibe 5% adicional de descuento.")

    pedidos@ while (true) {
        mostrarCatalogo(catalogo)
        println("Selecciona productos separados por coma (ejemplo: 1,3). Escribe 0 para salir:")
        val entrada = readlnOrNull() ?: break
        if (entrada.trim() == "0") break

        try {
            val productos = seleccionarProductos(entrada, catalogo)
            var tipoCliente: String
            while (true) {
                println("Cliente tipo (regular/vip/premium):")
                tipoCliente = readlnOrNull()?.trim()?.lowercase() ?: break@pedidos
                try {
                    porcentajeCliente(tipoCliente)
                    break
                } catch (e: IllegalArgumentException) {
                    println("Error: ${e.message}")
                }
            }
            val pedido = calcularPedido(productos, tipoCliente)
            var estado: EstadoPedido = EstadoPedido.Pendiente
            val tarea = launch {
                estado = procesarPedido(productos)
            }
            println("Procesando pedido...")
            tarea.join()
            if (estado is EstadoPedido.Listo) {
                ventas.add(pedido)
                mostrarResumen(pedido)
            }
            mostrarEstado(estado)
        } catch (e: IllegalArgumentException) {
            println("Error: ${e.message}")
        }
    }
    mostrarReporte(ventas)
    println("Gracias por usar FoodExpress.")
}
