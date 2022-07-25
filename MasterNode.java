import mpi.MPI;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.rmi.Naming;

class MasterNode {
    private static final int DEST = 1;   //try to think about storing the list of child node here in the master node
    private static final int BUF_LENGTH_TEST = 100;
    private static final int NCLIENTS_TEST = 1;
    private static final int PORT = 6231;
    private static String[] fileNames ;   //initialize later
    private static int fileCount;


    public static byte[] retrieveData(String Url){
        byte[] testing= new byte[10];
        return testing;
    }

    public static void upload(int fileSize, byte[] data){

        System.out.println("uploading files");
    }

    public static void listFile(){
        System.out.println("listing the names of the files");
        for(int i =0;i<fileNames.length;i++){
            System.out.println(fileNames[i]);
        }
    }

    public static void removeFile(String fileName){
        System.out.println("removing file");
        for(int i =0;i<fileNames.length;i++){
            if(fileNames[i].equals(fileName) ){
                if(i!=fileNames.length-1){
                    fileNames[i] = fileNames[i+1];
                }
                else{
                    fileNames[i] = null;
                }
            }
        }
    }

    static void main(String[] args) {
        fileNames = new String[1000];   //not sure about the size yet, just first try
        fileCount = 0;   //initialization
        System.out.println("Master");
        System.out.println("TRY start listening");
        try {
            System.out.println("OK looking up");
            //original code
            APIInterface remoteapi = (APIInterface)Naming.lookup(RMIServer.getURI(MPJAndRMIDemo.PORT, "SampleAPI"));
    //        RmiioService2 service = (RmiioService2) Naming.lookup(RMIServer.getURI(MPJAndRMIDemo.PORT, "RmiioServiceImpl2"));

            String function = "Upload";
            String[] sourceFilePath = {"notes.txt","100"};  //reading files from the master
            switch (function){   //.toUpperCase()
                case "List":
                    System.out.println("listing");
                    break;
                case "Upload":
                    System.out.println("upload");
                    //old code
//                  upload( sourceFilePath,  remoteapi);
                    upload( sourceFilePath, remoteapi);

                    break;
                case "downLoad":
                    String fileName= "Notes.txt";
                    retrieveData(fileName);
                    System.out.println("downLoad");
                    break;
                case "remove":
                    System.out.println("remove");
                    break;
                default:
                    System.out.println("nothing happened");
                    break;
            }
//                System.out.println(String.format("OK called foo "+eq+ " through RMI, result: %d", res));
        }
        catch(Exception e){
            System.out.println("ERR " + e.getMessage());
            e.printStackTrace();
        }
    }
        /*try {


        SampleAPI api = new SampleAPI();
//            Random r = new Random();   //need random later as the key, store it somewhere
//            long curTime=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();   //later combine the curtime with the rand to form the key
            RMIServer.start(PORT);
            RMIServer.register(api);
            int[] bufRec = new int[BUF_LENGTH_TEST];
            MPI.COMM_WORLD.Recv(bufRec, 0, bufRec.length, MPI.INT, DEST, 0);  //expecting a message from tag 0

            api.quit();
        }
        catch(Exception e){
            System.out.println("ERR " + e.getMessage());
            e.printStackTrace();
        }*/


    }
}