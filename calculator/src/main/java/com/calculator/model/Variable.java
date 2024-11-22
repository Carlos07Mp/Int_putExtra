package com.calculator.model;

public class Variable {
    private String name;  // Nombre de la variable (identificador)
    private Integer value; // Valor de la variable (tipo entero)

    // Constructor vacío
    public Variable() {}

    // Constructor con parámetros
    public Variable(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    // Getter para el nombre de la variable
    public String getName() {
        return name;
    }

    // Setter para el nombre de la variable
    public void setName(String name) {
        this.name = name;
    }

    // Getter para el valor de la variable
    public Integer getValue() {
        return value;
    }

    // Setter para el valor de la variable
    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
