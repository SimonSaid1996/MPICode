
/*
 *  RemoteInputStream.java
 *
 *  To enable RemoteOuputStream in a remote architecture
 *  See also: IRemoteOuputStream.java
 *
 *  (C) 2022 Ali Jannatpour <ali.jannatpour@concordia.ca>
 *
 *  This code is licensed under GPL.
 *
 */

import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteOuputStream extends UnicastRemoteObject implements IRemoteOutputStream {
    private OutputStream output;

    public RemoteOuputStream(OutputStream output) throws RemoteException {
        super();
        this.output = output;
    }

    @Override
    public void write(byte[] b) throws IOException
    {
        this.output.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        this.output.write(b, off, len);
    }

    @Override
    public void close() throws IOException {
        this.output.close();
    }

    // ... implement other wrapper methods
}
