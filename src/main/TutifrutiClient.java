/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import tutifruti.RmiServerIntf;

/**
 *
 * @author kiriost
 */
public class TutifrutiClient {

    private String host = "//jmonetti.ddns.net";
    private String serverName = "RmiServer";
    private String port = "1099";

    private String user = "alanb";
    private String password = "estatura10";

    private RmiServerIntf obj = null;

    private HashMap<String, ArrayList<String>> diccionario;

    TutifrutiClient(String user, String password) {
        this.user = user;
        this.password = password;

        this.diccionario = new HashMap<String, ArrayList<String>>();

        this.conectar(this.host + ":" + this.port + "/" + this.serverName);
    }

    private void conectar(String host) {
        while (obj == null) {
            System.out.print("Conectando como " + this.user + "/" + this.password + " ");
            try {
                if (System.getSecurityManager() == null) {
                    System.setProperty("java.security.policy", "java.policy");
                    System.setSecurityManager(new RMISecurityManager());
                }

                obj = (RmiServerIntf) Naming.lookup(host);

                System.out.print("(" + obj.comenzarComunicacion(this.user, this.password) + "): ");
                System.out.println("CONECTADO");
            } catch (Exception e) {
                String error = e.getMessage();
                e.printStackTrace();
            }
        }
    }

    public void getDiccionario() {
        try {
            HashMap<String, ArrayList<String>> diccionario = this.cargarDiccionario();

            System.out.println("Obteniendo diccionario: ");

            if (diccionario == null) {
                // Carga desde el servidor
                String categoriasStr = obj.getCategorias(user, password);
                String[] categorias = categoriasStr.split("-");

                for (String categoria : categorias) {
                    try {
                        if (categoria.equals("TOTAL")) {
                            continue;
                        }
                        System.out.print(categoria);

                        int cantPalabras = Integer.valueOf(obj.getCantidadPalabra(user, password, categoria));
                        obj.setLugarPalabra(user, password, categoria, 0);

                        System.out.print(" (" + String.valueOf(cantPalabras));

                        ArrayList<String> palabras;
                        if (!this.diccionario.containsKey(categoria)) {
                            palabras = new ArrayList<String>();
                            this.diccionario.put(categoria, palabras);
                        } else {
                            palabras = this.diccionario.get(categoria);
                        }

                        for (int i = 0; i < cantPalabras && i < 3000; i++) {
                            String palabra = obj.getPalabra(user, password, categoria);
                            palabras.add(palabra);
                        }

                        System.out.println("/" + palabras.size() + ")");
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }

                }

                this.guardarDiccionario(this.diccionario);

                System.out.println("Diccionario guardado localmente");
            } else {
                // Carga desde archivo local
                this.diccionario = diccionario;
                System.out.print("diccionario.data (archivo local)");
            }

            System.out.println("");
        } catch (Exception e) {
            String error = e.getMessage();
            e.printStackTrace();
        }
    }

    public String getCategoria() {
        String categoria = "";
        try {
            categoria = obj.getCategoria(user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoria;
    }

    public String getLetra() {
        String letra = "";
        try {
            letra = obj.getLetra(user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return letra;
    }

    public String obtenerPalabra(String categoria, String letra) {
        ArrayList<String> palabrasEnCategoria = this.diccionario.get(categoria);
        ArrayList<String> palabrasPosibles = new ArrayList<String>();

        for (String palabraEnCategoria : palabrasEnCategoria) {
            if (palabraEnCategoria.startsWith(letra)) {
                palabrasPosibles.add(palabraEnCategoria);
            }
        }

        System.out.println(String.valueOf(palabrasEnCategoria.size())
                + " palabras en categoría / "
                + String.valueOf(palabrasPosibles.size())
                + " palabras posibles");

        if (palabrasPosibles.size() == 0) {
            return null;
        }

        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(palabrasPosibles.size());
        String palabra = palabrasPosibles.get(index);

        System.out.println("Palabra seleccionada: " + palabra);

        return palabra;
    }

    public String setPalabra(String palabra) {
        String respuesta = "";
        try {
            respuesta = obj.setPalabra(palabra, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respuesta;
    }

    public String getMisPuntos(String palabra) {
        String puntos = "";
        try {
            puntos = obj.getMisPuntos(palabra, user);
        } catch (Exception e) {
            String error = e.getMessage();
            e.printStackTrace();
        }
        return puntos;
    }

    public String getGanador(String palabra) {
        String ganador = "";
        try {
            ganador = obj.getGanador(palabra, user);
        } catch (Exception e) {
            String error = e.getMessage();
            e.printStackTrace();
        }
        return ganador;
    }

    private void guardarDiccionario(HashMap<String, ArrayList<String>> diccionario) {
        try {
            File fileOne = new File("diccionario.data");
            FileOutputStream fos = new FileOutputStream(fileOne);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(diccionario);
            oos.flush();
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, ArrayList<String>> cargarDiccionario() {
        HashMap<String, ArrayList<String>> diccionario = new HashMap<String, ArrayList<String>>();

        try {
            File toRead = new File("diccionario.data");

            if (!toRead.exists()) {
                return null;
            }

            FileInputStream fis = new FileInputStream(toRead);
            ObjectInputStream ois = new ObjectInputStream(fis);

            diccionario = (HashMap<String, ArrayList<String>>) ois.readObject();

            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return diccionario;
    }
}
