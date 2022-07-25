import mpi.MPI;

import java.io.*;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

class SampleAPI  implements APIInterface {
    private static final int DEST = 1;
    private static Integer callno = 0;

    static HashMap<String, ArrayList<Integer>> filemap;
    static HashMap<Integer, Integer> cToChild;
    static HashMap<String, String> nameTohash;



    public SampleAPI() throws RemoteException {
        filemap=new HashMap<String, ArrayList<Integer>>();
        cToChild=new HashMap<Integer, Integer>();
        nameTohash=new HashMap<String,String>();
    }

    public int foo(String a) throws RemoteException, FileNotFoundException {  //there is a simiar pic on the phone similating the behavior of the master node
        int cn;
        synchronized (callno) {
            cn = ++callno;
        }
        System.out.println(String.format("** %d: calling foo", cn));
        System.out.println("Equation received from client "+ cn+ " is : "+a);

        char[] sbuf = new char[a.length()];
        for (int i = 0; i < a.length(); i++) {
            sbuf[i] = a.charAt(i);
        }
        int[] rbuf = new int[1];
        //so the data cluster side is always 4096 bytes? sure it is enough? when the actual length exceed?


        RMIServer.MPI_PROXY.Sendrecv(sbuf,0,sbuf.length, MPI.CHAR,DEST,cn,rbuf,0,rbuf.length,MPI.INT,DEST,cn);
        System.out.println(String.format("** %d: foo called", cn));
        return rbuf[0]; // receiving a message from DEST
    }

    @Override
    public void upload(IRemoteInputStream ris) throws RemoteException,IOException{
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd-hh:mm:ss");
        String strDate = dateFormat.format(date);
        String sourceFilePath = "notes.txt";
        String hash=sourceFilePath+"*"+strDate;
        System.out.println(hash);
        nameTohash.put(sourceFilePath,hash);
        try{
                byte[] buffer = new byte[40];
                int i;
//            ris.read(buffer);
//            System.out.println(new String(buffer));
            System.out.println(new String(ris.readb(buffer)));
//                while ((i= ris.read(buffer))!=-1){
//                    System.out.print((char) i);
//                    for(int j =0;j<buffer.length;j++){
////                    System.out.println("here are :");
//                        System.out.print((char)buffer[j]);
//                    }
////                    System.out.println(new String(buffer));
//                }
//                ris.read(buffer);
//            System.out.println(ris.read());
//                for(int j =0;j<buffer.length;j++){
////                    System.out.println("here are :");
//                    System.out.print((char)buffer[j]);
//                }
        }catch (Exception e){
            System.out.println("read exception "+e.getMessage());
            System.out.println("happened in line 102");
        }
    }
    @Override
    public int upload(byte[] buf) throws RemoteException, FileNotFoundException {
        int cn;
        synchronized (callno) {
            cn = ++callno;
        }
        System.out.println(String.format("** %d: calling foo", cn));
        System.out.println("doing a upload");


        byte info[] = new byte[30];
        byte result[] = new byte[info.length + buf.length];
        System.arraycopy(info, 0, result, 0, info.length);
        System.arraycopy(buf, 0, result, info.length, buf.length);

        /////////sending the first message
//        String a = "upload";
//        char[] sbuf = new char[a.length()];
//        for (int i = 0; i < a.length(); i++) {
//            sbuf[i] = a.charAt(i);
//        }
        int[] rbuf = new int[1];
//        RMIServer.MPI_PROXY.Send(sbuf,0, sbuf.length,MPI.CHAR,DEST,cn);

        //Send(final Object buf, final int offset, final int len, final Datatype datatype, final int dest, final int tag)
        //////////

        //so the data cluster side is always 4096 bytes? sure it is enough? when the actual length exceed?
        RMIServer.MPI_PROXY.Sendrecv(result,0,result.length,MPI.BYTE,DEST,cn,rbuf,0,rbuf.length,MPI.INT,DEST,cn);
        RMIServer.MPI_PROXY.Sendrecv(result,0,result.length,MPI.BYTE,2,cn,rbuf,0,rbuf.length,MPI.INT,2,cn);
//          System.out.println(String.format("** %d: foo called", cn));
        return rbuf[0]; // receiving a message from DEST
    }
    @Override
    public void upload() throws IOException{
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd-hh:mm:ss");
        String strDate = dateFormat.format(date);
        String sourceFilePath = "notes.txt";
        String hash=sourceFilePath+"*"+strDate;
        System.out.println(hash);
        nameTohash.put(sourceFilePath,hash);
        InputStream fin1 = new FileInputStream(new File(sourceFilePath));
        RemoteInputStream fin =  new RemoteInputStream(fin1);
        byte[] buffer = new byte[40];
        int[] rbuf = new int[1];
        int bufSize = 0;
        try{
            int filecounter=0;
            int child=0;
            ArrayList<Integer>map=new ArrayList<Integer>();
            byte info[]=new byte[100] ;

            while((fin.read(buffer,0,buffer.length))!=-1){

//                fin.read(buffer,0,buffer.length);

                child=((++filecounter)%2);
                map.add(child+1);
//                System.out.println("send :"+new String(buffer));
//                System.out.println("to: "+child);
                info=(hash+"*"+String.valueOf(filecounter)).getBytes();
                byte result[] = new byte[info.length + buffer.length];
                System.arraycopy(info, 0, result, 0, info.length);
                System.arraycopy(buffer, 0, result, info.length, buffer.length);
                System.out.println(result.length);
                System.out.println("sending buffer: "+new String(result));

                RMIServer.MPI_PROXY.Sendrecv(result,0,result.length,MPI.BYTE,child+1,1,rbuf,0,1,MPI.INT,child+1,1);
//                RMIServer.MPI_PROXY.Sendrecv(result,0,result.length,MPI.BYTE,2,1,rbuf,0,rbuf.length,MPI.INT,2,1);
//                System.out.print(new String(buffer));
            }
            filemap.put(hash+child,map);


        }catch (Exception e){
            System.out.println("read exception "+e);
            System.out.println("happened in line 102");
        }

    }
    public int fake(byte[] buf) throws RemoteException {
        int cn;
        synchronized (callno) {
            cn = ++callno;
        }
        System.out.println(String.format("** %d: calling foo", cn));
        System.out.println("doing a upload");


        int[] rbuf = new int[1];

        for(int i=0; i<buf.length; i+=4096){
            byte data[] = new byte[4096];
            System.arraycopy(buf, i, data, 0, data.length);
//            byte info[] = new byte[30];
//            byte result[] = new byte[info.length + data.length];
//            System.arraycopy(info, 0, result, 0, info.length);
//            System.arraycopy(data, 0, result, info.length, data.length);
            RMIServer.MPI_PROXY.Sendrecv(buf,0,buf.length,MPI.BYTE,DEST,cn,rbuf,0,rbuf.length,MPI.INT,DEST,cn);

        }
        /////////sending the first message
//        String a = "upload";
//        char[] sbuf = new char[a.length()];
//        for (int i = 0; i < a.length(); i++) {
//            sbuf[i] = a.charAt(i);
//        }

//        RMIServer.MPI_PROXY.Send(sbuf,0, sbuf.length,MPI.CHAR,DEST,cn);

        //Send(final Object buf, final int offset, final int len, final Datatype datatype, final int dest, final int tag)
        //////////

        //so the data cluster side is always 4096 bytes? sure it is enough? when the actual length exceed?
//        RMIServer.MPI_PROXY.Sendrecv(result,0,result.length,MPI.BYTE,DEST,cn,rbuf,0,rbuf.length,MPI.INT,DEST,cn);
//        RMIServer.MPI_PROXY.Sendrecv(result,0,result.length,MPI.BYTE,2,cn,rbuf,0,rbuf.length,MPI.INT,2,cn);
//          System.out.println(String.format("** %d: foo called", cn));
        return rbuf[0]; // receiving a message from DEST
    }
    @Override
    public String[] listFiles()throws RemoteException{
        return filemap.keySet().toArray(new String[0]);
    }

    @Override
//    public void download(String key,IRemoteOutputStream cos) throws RemoteException {
    public void download(String key) throws RemoteException {
        try{
            RemoteOuputStream os=new RemoteOuputStream(new FileOutputStream("test.txt"));
            int filecounter=0;
            for (Iterator<Integer> it = filemap.get(key).iterator(); it.hasNext(); ) {
                int i = it.next();
                byte result[] = new byte[40];
                byte sbuf[] = new byte[140];
                sbuf=(nameTohash.get(key)+"*"+(++filecounter)).getBytes();
                RMIServer.MPI_PROXY.Sendrecv(sbuf,0,sbuf.length,MPI.BYTE,i,1,result,0,40,MPI.BYTE,i,2);
                os.write(result,0,result.length);
                System.out.println(new String(result));
            }
        }catch (Exception e){
            System.out.println("read exception "+e);
            System.out.println("happened in outputstream");
        }
    }

    public void quit() throws RemoteException {
        RMIServer.stop();
    }
}