package com.example.habitapp.ui

import org.junit.Assert.assertEquals
import org.junit.Test

class PrincipalViewModelTest {
    @Test
    fun crearHabito_actualizaEstadoConNuevoHabito() {
        val vm = PrincipalViewModel()
        assertEquals(0, vm.state.value.habitos.size)
        vm.agregarHabito("Leer 20 minutos")
        val state = vm.state.value
        assertEquals(1, state.habitos.size)
        assertEquals("Leer 20 minutos", state.habitos[0].Nombre)
        assertEquals("Pendiente", state.habitos[0].Estado)
    }

    @Test
    fun alternarHabito_cambiaEstadoPendienteCompleto() {
        val vm = PrincipalViewModel()
        vm.agregarHabito("Ejercicio")
        val id = vm.state.value.habitos.first().id
        vm.alternarHabito(id)
        assertEquals("Completo", vm.state.value.habitos.first().Estado)
        vm.alternarHabito(id)
        assertEquals("Pendiente", vm.state.value.habitos.first().Estado)
    }
}

