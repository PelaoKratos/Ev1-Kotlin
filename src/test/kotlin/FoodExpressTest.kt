package org.prueba

import kotlinx.coroutines.runBlocking
import kotlin.test.*

class FoodExpressTest {
    @Test
    fun `ejemplo del PDF`() {
        val pedido = calcularPedido(seleccionarProductos("1,3", crearCatalogo()), "vip")
        assertEquals(11279, pedido.subtotal)
        assertEquals(1128, pedido.descuento)
        assertEquals(0, pedido.promocion)
        assertEquals(1929, pedido.iva)
        assertEquals(12080, pedido.total)
    }

    @Test
    fun `catalogo y precios por tipo`() {
        assertEquals(listOf(8990, 15990, 2289, 3887), crearCatalogo().map { it.calcularPrecio() })
        assertEquals(1000, ProductoBebida("Agua", 1000, "Bebida", 0, "pequeño").calcularPrecio())
        assertEquals(1150, ProductoBebida("Agua", 1000, "Bebida", 0, "mediano").calcularPrecio())
        assertEquals(1300, ProductoBebida("Agua", 1000, "Bebida", 0, "grande").calcularPrecio())
    }

    @Test
    fun `descuentos y promocion`() {
        assertEquals(500, calcularDescuento(10000, "regular"))
        assertEquals(1000, calcularDescuento(10000, "VIP"))
        assertEquals(1500, calcularDescuento(10000, "premium"))
        assertEquals(0, aplicarPromocion(10000, 2))
        assertEquals(500, aplicarPromocion(10000, 3))
        val producto = Producto("Pan", 1000, "Comida", 0)
        val pedido = calcularPedido(listOf(producto, producto, producto), "vip")
        assertEquals(300, pedido.descuento)
        assertEquals(135, pedido.promocion)
        assertEquals(487, pedido.iva)
        assertEquals(3052, pedido.total)
    }

    @Test
    fun `datos incorrectos y cantidades repetidas`() {
        val catalogo = crearCatalogo()
        for (entrada in listOf("", "hola", "0", "9", "1,", "-1", "1,999999999999")) {
            assertFailsWith<IllegalArgumentException> { seleccionarProductos(entrada, catalogo) }
        }
        assertEquals(3, seleccionarProductos("1,1,3", catalogo).size)
        assertFailsWith<IllegalArgumentException> { calcularPedido(emptyList(), "vip") }
        assertFailsWith<IllegalArgumentException> { calcularDescuento(1000, "otro") }
        assertFailsWith<IllegalArgumentException> { Producto("Pan", -1, "Comida", 0) }
        assertFailsWith<IllegalArgumentException> { Producto("Pan", 1000, "Comida", -1) }
        assertFailsWith<IllegalArgumentException> { ProductoBebida("Agua", 1000, "Bebida", 0, "otro") }
        assertFailsWith<IllegalArgumentException> { calcularSubtotal(List(3000) { Producto("Pan", 1000000, "Comida", 0) }) }
    }

    @Test
    fun `procesamiento y error`() = runBlocking {
        assertTrue(procesarPedido(listOf(Producto("Pan", 1000, "Comida", 0))) is EstadoPedido.Listo)
        assertTrue(procesarPedido(emptyList()) is EstadoPedido.Error)
    }

    @Test
    fun `reporte vacio y reporte con ventas`() {
        mostrarReporte(emptyList())
        mostrarReporte(listOf(calcularPedido(seleccionarProductos("1,3", crearCatalogo()), "vip")))
    }
}
