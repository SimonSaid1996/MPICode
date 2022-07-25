
/*
 *  RemoteInputStream.java
 *
 *  To enable IInputStream in a remote architecture
 *  See also: IRemoteInputStream.java
 *
 *  (C) 2022 Ali Jannatpour <ali.jannatpour@concordia.ca>
 *
 *  This code is licensed under GPL.
 *
 */

import java.io.IOException;
import java.rmi.Remote;

public interface IRemoteOutputStream extends Remote, AutoCloseable {
    void write(byte[] b) throws IOException;
    void write(byte[] b, int off, int len) throws IOException;
    void close() throws IOException;
    // TODO: other function, if necessary
}
