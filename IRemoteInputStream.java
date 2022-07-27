import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteInputStream extends Remote,AutoCloseable {
    int read(byte[] b, int off, int len) throws RemoteException, IOException;
    int read() throws RemoteException, IOException;
    int read(byte[] b) throws  IOException;
    void close() throws IOException;
    int available() throws IOException;
    byte[] readb(byte[] b) throws IOException;

    byte[] readb(byte[] b, int off, int len) throws  IOException;

}
