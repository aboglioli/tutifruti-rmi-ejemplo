package tutifruti;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Julio
 */
public interface RmiServerIntf extends Remote {
    public String comenzarComunicacion(String quien, String clave) throws RemoteException;
    public String terminarComunicacion(String quien, String clave) throws RemoteException;

    public String getLetra(String quien, String clave) throws RemoteException;
    public String getPalabra(String quien, String clave, String categoria) throws RemoteException;
    public String getCantidadPalabra(String quien, String clave, String categoria) throws RemoteException;
    public String setLugarPalabra(String quien, String clave, String categoria, int donde) throws RemoteException;

    public String getCategorias(String quien, String clave) throws RemoteException;
    public String getCategoria( String quien, String Clave) throws RemoteException;
    
    
    public String setPalabra(String palabra, String quien, String Clave) throws RemoteException;

    public String getMisPuntos(String palabra, String quien) throws RemoteException;

    public String getGanador(String palabra, String quien) throws RemoteException;
    public String getUltimoMensajeError(String quien, String clave) throws RemoteException;
}
