import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.Naming;
class RMIClient {

    public static void upload(){
        try {
            System.out.println("upload");
            String sourceFilePath = "Clientnote.txt";
            InputStream fin1 = new FileInputStream(new File(sourceFilePath));
            RemoteInputStream fin =  new RemoteInputStream(fin1);
            byte[] buffer = new byte[4096];   //might be changed later, not even sure if i should send byte or just char
            int bufSize = 0;
            try{
//                bufSize = fin.read(buffer,0,buffer.length);
//                for(int j =0;j<buffer.length;j++){
//                    System.out.print((char)buffer[j]);
//                }
            }catch (Exception e){
                System.out.println("read exception "+e);
                System.out.println("happened in line 102");
            }
            System.out.println("OK looking up");
            APIInterface remoteapi = (APIInterface) Naming.lookup(RMIServer.getURI(6231, "SampleAPI"));
//            int res = remoteapi.upload(buffer);
            remoteapi.upload(fin);


        }
        catch(Exception e){
            System.out.println("ERR " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String[] list(){
        String[] errorSoon = {"Hello", "World"};
        try{
            APIInterface remoteapi = (APIInterface) Naming.lookup(RMIServer.getURI(6231, "SampleAPI"));
            return remoteapi.listFiles();
        }
        catch(Exception e){
            System.out.println("ERR " + e.getMessage());
            e.printStackTrace();
        }
        return errorSoon;
    }
    public static void download(OutputStream OS){

    }


    public static void main(String[] args) {
        try {
            upload();
//            for (String s : list()) {
//                System.out.println(s);
//            }
        }
        catch(Exception e){
            System.out.println("ERR " + e.getMessage());
            e.printStackTrace();
        }
    }
}