package org.prueba


open class Producto(
    val nombre: String,
    val precio: Int,
    val categoria: String,
    val tiempoPreparacion: Long
) {
    init {
        require(nombre.isNotBlank()) { "El nombre no puede estar vacio." }
        require(categoria.isNotBlank()) { "La categoría no puede estar vacia." }
        require(precio > 0 && precio <= 1000000) { "El precio debe estar entre 1 y 1000000 pesos." }
        require(tiempoPreparacion >= 0) { "El tiempo no puede ser negativo." }
    }

    open fun calcularPrecio(): Int {
        return precio
    }

    open fun mostrarProducto(): String {
        return "$nombre - $${calcularPrecio()}"
    }
}

class ProductoPrincipal(
    nombre: String, precio: Int, categoria: String, tiempoPreparacion: Long,
    val premium: Boolean
) : Producto(nombre, precio, categoria, tiempoPreparacion) {
    override fun calcularPrecio(): Int {
        return if (premium) precio + 1000 else precio
    }

    override fun mostrarProducto(): String {
        return super.mostrarProducto() + if (premium) " (Premium)" else ""
    }
}

class ProductoBebida(
    nombre: String, precio: Int, categoria: String, tiempoPreparacion: Long,
    val tamano: String
) : Producto(nombre, precio, categoria, tiempoPreparacion) {
    init {
        require(tamano.lowercase() in listOf("pequeno", "pequeño", "mediano", "grande")) {
            "Tamaño invalido: usa pequeño, mediano o grande."
        }
    }

    override fun calcularPrecio(): Int {
        val recargo = when (tamano.lowercase()) {
            "mediano" -> 15
            "grande" -> 30
            else -> 0
        }
        return precio + calcularPorcentaje(precio, recargo)
    }

    override fun mostrarProducto(): String {
        return "$nombre ($tamano) - $${calcularPrecio()}"
    }
}
