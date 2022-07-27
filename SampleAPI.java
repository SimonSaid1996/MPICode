import mpi.MPI;

import java.io.*;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

class SampleAPI  implements APIInterface {
    private static final int childNum = MPI.COMM_WORLD.Size()-1;
    private static Integer callno = 0;

    static HashMap<String, ArrayList<Integer>> filemap;
    static HashMap<Integer, Integer> cToChild;
    static HashMap<String, String> nameTohash;



    public SampleAPI() throws RemoteException {
        filemap=new HashMap<String, ArrayList<Integer>>();
        cToChild=new HashMap<Integer, Integer>();
        nameTohash=new HashMap<String,String>();
    }

    @Override
    public void upload(IRemoteInputStream ris,String name) throws RemoteException,IOException{
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd-hh:mm:ss");
        String strDate = dateFormat.format(date);
        String hash="";
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update((name+"*"+strDate).getBytes("utf8"));
            hash= String.format("%040x", new BigInteger(1, digest.digest()));
        }catch (Exception e){
            System.out.println("read exception "+e.getMessage());
            System.out.println("happened in line 102");
        }

        System.out.println(hash);
        nameTohash.put(name,hash);
        try{

            int filecounter=0;
            int child=0;
            ArrayList<Integer>map=new ArrayList<Integer>();

            byte[] buffer = new byte[40];
            int[] rbuf = new int[1];
            int i;
            while ((buffer= ris.readb(buffer,0,buffer.length))!=null){

                byte[] info=new byte[100] ;
                child=((++filecounter)%childNum);
                map.add(child+1);
//                System.out.println("send :"+buffer.length);
//                System.out.println("to: "+child);
                info=(hash+"*"+String.valueOf(filecounter)).getBytes();
                byte result[] = new byte[info.length + buffer.length];
                System.arraycopy(buffer, 0, result, 0, buffer.length);
                System.arraycopy(info, 0, result, buffer.length, info.length);
//                System.out.println(result.length);
//                System.out.println("sending buffer: "+new String(result));

                RMIServer.MPI_PROXY.Sendrecv(result,0,result.length,MPI.BYTE,child+1,1,rbuf,0,1,MPI.INT,child+1,1);

//                System.out.println("2,"+new String(buffer)+"--");
                buffer=new byte[40];
            }
            filemap.put(hash,map);
        }catch (Exception e){
            System.out.println("read exception "+e.getMessage());
            System.out.println("happened in line 102");
        }
    }

    @Override
    public String[] listFiles()throws RemoteException{
        return nameTohash.keySet().toArray(new String[0]);
    }
    @Override
    public void remove(String name)throws RemoteException{
        int filecounter=0;
        String hash=nameTohash.get(name);
//            System.out.println(filemap.get(key));

        for (Iterator<Integer> it = filemap.get(hash).iterator(); it.hasNext(); ) {
            int i = it.next();
            byte result[] = new byte[40];
            byte sbuf[] = new byte[140];
            byte info[]=new byte[100];
//            System.out.println("name="+name);
            info=(nameTohash.get(name)+"*"+(++filecounter)).getBytes();
            System.arraycopy(info, 0, sbuf, result.length, info.length);
            RMIServer.MPI_PROXY.Send(sbuf,0,sbuf.length,MPI.BYTE,i,3);

        }

        nameTohash.remove(name);
        filemap.remove(hash);

    }
    @Override
    public void download(String name,IRemoteOutputStream os) throws RemoteException {
        try{

            int filecounter=0;
            String hash=nameTohash.get(name);
//            System.out.println(filemap.get(key));

            for (Iterator<Integer> it = filemap.get(hash).iterator(); it.hasNext(); ) {
                int i = it.next();
                byte result[] = new byte[40];
                byte sbuf[] = new byte[140];
                byte info[]=new byte[100];
//                System.out.println("name="+name);
                info=(nameTohash.get(name)+"*"+(++filecounter)).getBytes();
                System.arraycopy(info, 0, sbuf, result.length, info.length);
//                System.out.println("sbuf: "+new String(sbuf));
//                System.out.println("info: "+new String(info));
                RMIServer.MPI_PROXY.Sendrecv(sbuf,0,sbuf.length,MPI.BYTE,i,2,result,0,40,MPI.BYTE,i,2);
                if (result[39]==0){
                    byte out[]=new String(result).trim().getBytes();
                    os.write(out,0,out.length);
                }else{
                    os.write(result,0,result.length);
                }
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