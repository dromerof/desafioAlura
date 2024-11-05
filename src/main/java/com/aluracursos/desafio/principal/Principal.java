package com.aluracursos.desafio.principal;

import com.aluracursos.desafio.model.Datos;
import com.aluracursos.desafio.model.DatosLibros;
import com.aluracursos.desafio.service.ConsumoAPI;
import com.aluracursos.desafio.service.ConvierteDatos;

import java.util.Comparator;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private final ConsumoAPI consumoApi = new ConsumoAPI();
    private final ConvierteDatos conversor = new ConvierteDatos();
    private final Scanner teclado = new Scanner(System.in);
    private static final String API_URL = "https://gutendex.com/books/";

    public void muestraMenu() {

        var json = consumoApi.obtenerDatos(API_URL);
        System.out.println(json);
        var datos = conversor.obtenerDatos(json, Datos.class);
        System.out.println(datos);

        //Top 10 de libros más descargados
        System.out.println("\nTop 10 de libros más descargados\n");
        datos.resultados().stream()
                .sorted(Comparator.comparing(DatosLibros::numeroDeDescargas).reversed())
                .limit(10)
                .map(l -> l.titulo().toUpperCase())
                .forEach(System.out::println);

        //Búsqueda de libros por nombre
        System.out.println("\nPor favor escribe el comienzo del titulo del libro que deseas encontrar\n");
        var cominezoTitulo = teclado.nextLine();
        json = consumoApi.obtenerDatos(API_URL+"?search=" + cominezoTitulo.replace(" ", "+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(cominezoTitulo.toUpperCase()))
                .findFirst();
        if (libroBuscado.isPresent()){
            System.out.println("Libro encontrado ");
            System.out.println(libroBuscado.get());
        }else {
            System.out.println("Libro no encontrado");
        }

    }

}
