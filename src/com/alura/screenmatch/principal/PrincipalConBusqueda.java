package com.alura.screenmatch.principal;

import com.alura.screenmatch.excepcion.ErrorEnConversionDeDuracionException;
import com.alura.screenmatch.modelos.Titulo;
import com.alura.screenmatch.modelos.TituloOmdb;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class PrincipalConBusqueda {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner lectura = new Scanner(System.in);
        System.out.println("Escriba el nombre de una pelicula: ");
        var busqueda = lectura.nextLine();
        String direccion = "http://www.omdbapi.com/?t="+busqueda.replace(" ", "+")+"&apikey=96ab3630";
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(direccion))
                    .build();
            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            String json = response.body();
            System.out.println(json);

            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();



            TituloOmdb miTitulo0mdb = gson.fromJson(json, TituloOmdb.class);
            System.out.println(miTitulo0mdb);

            Titulo miTitulo = new Titulo(miTitulo0mdb);
            System.out.println(miTitulo);
            FileWriter escritura = new FileWriter("peliculas.txt");
            escritura.write(miTitulo.toString());
            escritura.close();
        }catch (NumberFormatException e){
            System.out.println("Ocurrio un error: ");
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Ocurrio un error en la URI, verifique la direccion");

        } catch (ErrorEnConversionDeDuracionException e){
            System.out.println(e.getMensaje());

        }catch (Exception e) {
            System.out.println("Ocurrio un error inesperado.");
        } finally {
            System.out.println("Finalizo la ejecucion del programa!");
        }

    }
}