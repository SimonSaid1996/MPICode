import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

interface APIInterface extends Remote {
    void upload(IRemoteInputStream ris,String name) throws IOException;

    void remove(String name)throws RemoteException;
    String[] listFiles()throws RemoteException;
    void download(String key,IRemoteOutputStream os) throws RemoteException;
    void quit() throws RemoteException;

}