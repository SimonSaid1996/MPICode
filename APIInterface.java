import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

interface APIInterface extends Remote {
    int foo(String a) throws RemoteException, FileNotFoundException;
    int upload(byte[] buf) throws RemoteException, FileNotFoundException;
    void upload(IRemoteInputStream ris) throws IOException;
    public void upload() throws IOException;
    String[] listFiles()throws RemoteException;

    void download(String key)throws RemoteException;
    void quit() throws RemoteException;
}