package org.prueba

open class Producto(
     val nombre: String,
     val precio: Int,
     val categoria: String,
     val tiempoPreparacion: Long
) {
    open fun calcularPrecio(): Int{
        return precio
    }

    open fun mostrarProducto(): String{
        return "$nombre - $precio"
    }
}
//Producto tipo comida
class ProductoPrincipal(
    nombre:String,
    precio: Int,
    categoria: String,
    tiempoPreparacion: Long,
    val premium: Boolean
): Producto(nombre, precio, categoria, tiempoPreparacion) {
    override fun calcularPrecio(): Int {
        return if (premium) {
            precio + 1000
        } else {
            precio
        }
    }

    override fun mostrarProducto(): String {
        return if (premium) {
            "$nombre - $${calcularPrecio()} (Premium)"
        } else {
            "$nombre - $${calcularPrecio()}"
        }
    }
}
//Producto tipo Bebida
class ProductoBebida(
    nombre: String,
    precio: Int,
    categoria: String,
    tiempoPreparacion: Long,
    val tamano : String
)  : Producto(nombre, precio, categoria, tiempoPreparacion){
    override fun calcularPrecio(): Int{
        return when(tamano.lowercase()){
            "pequeno" -> precio
            "mediano" -> precio + 300
            "grande" -> precio + 500
            else -> precio
        }
    }

    override fun mostrarProducto(): String{
        return "$nombre (${tamano.replaceFirstChar { it.uppercase() }}) $${calcularPrecio()}"
    }
}






