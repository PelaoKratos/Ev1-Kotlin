package org.prueba

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay


data class ResumenPedido(
    val productos: List<Producto>,
    val tipoCliente: String,
    val subtotal: Int,
    val descuento: Int,
    val promocion: Int,
    val iva: Int,
    val total: Int
)

fun crearCatalogo(): List<Producto> {
    return listOf(
        ProductoPrincipal("Hamburguesa Clasica", 8990, "Comida", 3000, false),
        ProductoPrincipal("Salmon Grillado", 14990, "Comida", 3000, true),
        ProductoBebida("Coca Cola", 1990, "Bebida", 1000, "mediano"),
        ProductoBebida("Jugo Natural", 2990, "Bebida", 1000, "grande")
    )
}

fun mostrarCatalogo(catalogo: List<Producto>) {
    println("\nCatalogo disponible:")
    catalogo.forEachIndexed { indice, producto ->
        println("${indice + 1}. ${producto.mostrarProducto()}")
    }
}

fun seleccionarProductos(entrada: String, catalogo: List<Producto>): List<Producto> {
    require(entrada.isNotBlank()) { "Selecciona al menos un producto." }
    // map transforma cada número escrito en su producto. Repetirlo agrega otra unidad.
    return entrada.split(",").map { texto ->
        val numero = texto.trim().toIntOrNull()
            ?: throw IllegalArgumentException("Escribe numeros separados por coma.")
        catalogo.getOrNull(numero - 1)
            ?: throw IllegalArgumentException("El producto $numero no existe.")
    }
}

fun calcularSubtotal(productos: List<Producto>): Int {
    require(productos.isNotEmpty()) { "El pedido debe tener al menos un producto." }
    val subtotal = productos.sumOf { it.calcularPrecio().toLong() }
    require(subtotal in 1..Int.MAX_VALUE.toLong()) { "El subtotal está fuera del rango permitido." }
    return subtotal.toInt()
}

fun porcentajeCliente(tipoCliente: String): Int {
    return when (tipoCliente.trim().lowercase()) {
        "regular" -> 5
        "vip" -> 10
        "premium" -> 15
        else -> throw IllegalArgumentException("El cliente debe ser regular, vip o premium.")
    }
}

fun calcularPorcentaje(monto: Int, porcentaje: Int): Int {
    require(monto >= 0 && porcentaje in 0..100) { "Monto o porcentaje inválido." }
    return ((monto.toLong() * porcentaje + 50) / 100).toInt()
}

fun calcularDescuento(subtotal: Int, tipoCliente: String): Int {
    return calcularPorcentaje(subtotal, porcentajeCliente(tipoCliente))
}

fun aplicarPromocion(monto: Int, cantidad: Int): Int {
    return if (cantidad >= 3) calcularPorcentaje(monto, 5) else 0
}

fun calcularIva(valor: Int): Int {
    return calcularPorcentaje(valor, 19)
}

fun calcularPedido(productos: List<Producto>, tipoCliente: String): ResumenPedido {
    val subtotal = calcularSubtotal(productos)
    val descuento = calcularDescuento(subtotal, tipoCliente)
    val promocion = aplicarPromocion(subtotal - descuento, productos.size)
    val neto = subtotal - descuento - promocion
    val iva = calcularIva(neto)
    val total = neto.toLong() + iva
    require(total <= Int.MAX_VALUE) { "El total es demasiado grande." }
    return ResumenPedido(productos.toList(), tipoCliente.trim().lowercase(), subtotal,
        descuento, promocion, iva, total.toInt())
}

fun mostrarEstado(estado: EstadoPedido) {
    when (estado) {
        is EstadoPedido.Pendiente -> println("Estado: Pendiente")
        is EstadoPedido.EnPreparacion -> println("Estado: En preparacion")
        is EstadoPedido.Listo -> println("Estado: Listo para entrega")
        is EstadoPedido.Error -> println("Estado: Error - ${estado.mensaje}")
    }
}

suspend fun procesarPedido(productos: List<Producto>): EstadoPedido {
    var estado: EstadoPedido = EstadoPedido.Pendiente
    mostrarEstado(estado)
    try {
        require(productos.isNotEmpty()) { "No se puede preparar un pedido vacío." }
        estado = EstadoPedido.EnPreparacion
        mostrarEstado(estado)
        delay(productos.maxOf { it.tiempoPreparacion })
        estado = EstadoPedido.Listo
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        estado = EstadoPedido.Error(e.message ?: "No se pudo preparar el pedido.")
    }
    return estado
}

fun mostrarResumen(pedido: ResumenPedido) {
    println("\n=== RESUMEN DEL PEDIDO ===")
    pedido.productos.forEach { println("- ${it.mostrarProducto()}") }
    println("Subtotal: $${pedido.subtotal}")
    println("Descuento ${pedido.tipoCliente.uppercase()} (${porcentajeCliente(pedido.tipoCliente)}%): -$${pedido.descuento}")
    println("Promoción 3 o mqs productos (5% adicional): -$${pedido.promocion}")
    println("IVA (19%): $${pedido.iva}")
    println("TOTAL: $${pedido.total}")
}

fun mostrarReporte(ventas: List<ResumenPedido>) {
    println("\n=== REPORTE DE VENTAS ===")
    val total = ventas.sumOf { it.total.toLong() }
    val vip = ventas.filter { it.tipoCliente == "vip" }
    val totales = ventas.map { it.total }
    println("Pedidos entregados: ${ventas.size}")
    println("Ventas a clientes VIP: ${vip.size}")
    println("Totales por pedido: $totales")
    var comidas = 0
    var bebidas = 0
    for (venta in ventas) {
        comidas += venta.productos.filter { it is ProductoPrincipal }.size
        bebidas += venta.productos.filter { it is ProductoBebida }.size
    }
    println("Unidades vendidas: $comidas comidas y $bebidas bebidas")
    println("Total recaudado: $$total")
    val promedio = if (ventas.isEmpty()) 0.0 else total.toDouble() / ventas.size
    println("Promedio por pedido: $" + String.format(java.util.Locale.forLanguageTag("es-CL"), "%.2f", promedio))
}
