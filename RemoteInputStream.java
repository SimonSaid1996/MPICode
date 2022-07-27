import java.io.IOException;
import java.io.InputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteInputStream extends UnicastRemoteObject  implements IRemoteInputStream, Remote { //remote means it is not serializable
    private final transient InputStream input;  //
    //       private final InputStream input;

    public RemoteInputStream( InputStream input)throws RemoteException {  //final
        super();
        this.input = input;
    }

    public int read() throws IOException {
        return input.read();
    }


    public int available() throws IOException {
        return this.input.available();
    }

    @Override
    public void close() throws IOException {
        this.close();
    }

    @Override
    public int read(byte[] b, int off, int len) throws  IOException {

        return input.read(b, off, len);
    }

    @Override
    public byte[] readb(byte[] b, int off, int len) throws  IOException {
        if(input.read(b, off, len)==-1){
            return null;
        }
        return b;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return input.read(b);
    }
    @Override
    public byte[] readb(byte[] b) throws IOException {
        input.read(b);
        return b;
    }
}