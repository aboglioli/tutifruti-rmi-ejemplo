/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author kiriost
 */
public class Main {

    private static TutifrutiClient client;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        client = new TutifrutiClient("alanb", "estatura10");
        client.getDiccionario();
        jugar();
    }

    public static void jugar() {
        while (true) {
            System.out.println("");
            String categoria = client.getCategoria();
            String letra = client.getLetra();
            
            System.out.println("Categoria: " + categoria + " - Letra: " + letra);
            
            String palabra = client.obtenerPalabra(categoria, letra);
            
            if(palabra == null) {
                System.out.println("No se encontraron palabras");
                continue;
            }
            
            System.out.println("Enviando: " + palabra);
            String respuesta = client.setPalabra(palabra);
            
            System.out.println("Respuesta: " + respuesta);
            System.out.println("Puntos: " + client.getMisPuntos(palabra));
        }

    }

}
