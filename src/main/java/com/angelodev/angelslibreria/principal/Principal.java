package com.angelodev.angelslibreria.principal;

import ch.qos.logback.core.encoder.JsonEscapeUtil;
import com.angelodev.angelslibreria.modelo.DatosAutores;
import com.angelodev.angelslibreria.modelo.DatosGeneralesLibros;
import com.angelodev.angelslibreria.modelo.DatosLibros;
import com.angelodev.angelslibreria.service.ConsumoAPI;
import com.angelodev.angelslibreria.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private ConsumoAPI consumoApi = new ConsumoAPI();

    private ConvierteDatos conversor = new ConvierteDatos();

    private Scanner teclado = new Scanner(System.in);

    private final String URL_BASE = "https://gutendex.com/books/";

    public void inicializaElPrograma(){
        var json = consumoApi.obtenerDatos(URL_BASE);
        System.out.println(json);
        //Convertimos los datos del json a objetos java y lo almacenamos en Datos
        var datos = conversor.obtenerDatos(json, DatosGeneralesLibros.class);

        //Datos generales de los top 5 libros
        System.out.println("########## Top 5 Libros ##########");
        datos.libros().stream()
                .limit(5)
                .forEach(System.out::println);


        //Se muestra el TOP 10 libros
        System.out.println("\n########## TOP los 10 libros más populares ##########");
        datos.libros().stream()
                .sorted(Comparator.comparing(DatosLibros::conteoDeDescargas).reversed())
                .limit(10)
                .map(l -> l.titulo().toUpperCase())
                .forEach(System.out::println);

        //Buscando un libro por un Titulo introducido por el usuario
        System.out.println("########## Buscando un libro por un Titulo introducido por el Usuario #########");
        var tituloBuscado = teclado.nextLine();
        json = consumoApi.obtenerDatos(URL_BASE + "?search=" + tituloBuscado.replace(" ","+"));
        var datosBusqueda = conversor.obtenerDatos(json, DatosGeneralesLibros.class);
        Optional <DatosLibros> libroBuscado = datosBusqueda.libros().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloBuscado.toUpperCase()))
                .findFirst();
        if (libroBuscado.isPresent()){
            System.out.println("Libro Encontrado !!!\n " + libroBuscado.get());
        }else {
            System.out.println("Búsqueda invalida! : Resultados no Enonctrados");
        }

        //Búscando libro por fechas
        System.out.println("########## Búsqueda por fechas ##########");
        System.out.println("Ingresa el valor el año del cuál se comience a buscar libros");
        var fechaInicio = teclado.nextInt();
        System.out.println("Ingresa el año donde se detendra las búsquea de libros");
        var fechaFin = teclado.nextInt();

        json = consumoApi.obtenerDatos(URL_BASE + "?author_year_start=" + fechaInicio + "&author_year_end=" + fechaFin);
        var datosBusquedaPorFecha = conversor.obtenerDatos(json, DatosGeneralesLibros.class);

        System.out.println("########## Libros encontrado entre las fechas " + fechaInicio + " y la fecha " + fechaFin + " ##########");
        datosBusquedaPorFecha.libros().stream()
                .limit(10)
                .forEach(System.out::println);

        //Generando estadísticas
        System.out.println("\n########## Estadísticas Generales ##########");
        IntSummaryStatistics est = datosBusqueda.libros().stream()
                .filter(d -> d.conteoDeDescargas() > 0 )
                .collect(Collectors.summarizingInt(DatosLibros::conteoDeDescargas));
        System.out.println("Media de las descargas realizadas: " + est.getAverage());
        System.out.println("La mayor cantidad de descargas que se ha dado es: " + est.getMax());
        System.out.println("Cantidad mínima de descargas" + est.getMin());
        System.out.println("Cantidad de registros evaluados para calcular las estadísticas: " + est.getCount());
        System.out.println("#############################################");

    }
}
