package com.aluracursos.biblioApp.service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}