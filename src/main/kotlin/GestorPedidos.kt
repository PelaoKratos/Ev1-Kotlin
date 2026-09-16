package org.prueba

import kotlinx.coroutines.*

fun crearCatalogo(): List<Producto>{
    return listOf(
    ProductoPrincipal("Hamburguesa Clasica",8990, "Comida",3000, false),
    ProductoPrincipal("Salmon Grillado",14990,"Comida", 3000,true),
    ProductoBebida("Cocacola",1990,"Bebida", 1000,"grande"),
    ProductoBebida("Jugo Natural", 2990,"Bebida", 1000, "grande")
    )
}

fun mostrarCatalogo(catalogo: List<Producto>){
    println("Catalogo disponible:")
    catalogo.forEachIndexed { indice, producto ->
        println("${indice + 1}. ${producto.mostrarProducto()} ")
    }
}

fun calcularSubtotal(productos: List<Producto>): Int{
    return productos.sumOf {
        it.calcularPrecio()
    }
}

fun calcularDescuento(subtotal: Int, tipoCliente: String): Int{
    val porcentaje = when(tipoCliente.lowercase()){
        "regular" -> 5
        "vip" -> 10
        "premium" -> 15
        else -> 0
    }
    return subtotal * porcentaje/100
}

fun calcularIva(valor: Int): Int{
    return valor * 19 / 100
}

suspend fun preocesarPedido(){
    try {
        println()
        println("Procesando pedido...")
        println("Estado: En Preparacion")
        //delay(3000)
        return EstadoPedido.Listo
    } catch(e:Exception){
        return EstadoPedido.Error("Error en procesar el pedido")

    }
}

fun mostrarEstado(estado:EstadoPedido)={

}















