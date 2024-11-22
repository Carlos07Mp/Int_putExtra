package com.calculator.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VariableTest {

    @Test
    void testVariableConstructor() {
        // Crear una instancia de la clase Variable con el constructor
        Variable variable = new Variable("x", 5);

        // Verificar que el nombre y el valor sean correctos
        assertEquals("x", variable.getName(), "El nombre de la variable debe ser 'x'.");
        assertEquals(5, variable.getValue(), "El valor de la variable debe ser 5.");
    }

    @Test
    void testSettersAndGetters() {
        // Crear una instancia de la clase Variable sin parámetros
        Variable variable = new Variable();

        // Establecer el valor de la variable utilizando los setters
        variable.setName("y");
        variable.setValue(10);

        // Verificar que los getters devuelvan los valores correctos
        assertEquals("y", variable.getName(), "El nombre de la variable debe ser 'y'.");
        assertEquals(10, variable.getValue(), "El valor de la variable debe ser 10.");
    }

    @Test
    void testSetName() {
        Variable variable = new Variable("x", 5);

        // Establecer un nuevo nombre
        variable.setName("z");

        // Verificar que el nombre se haya actualizado correctamente
        assertEquals("z", variable.getName(), "El nombre de la variable debe ser 'z'.");
    }

    @Test
    void testSetValue() {
        Variable variable = new Variable("x", 5);

        // Establecer un nuevo valor
        variable.setValue(20);

        // Verificar que el valor se haya actualizado correctamente
        assertEquals(20, variable.getValue(), "El valor de la variable debe ser 20.");
    }
}
