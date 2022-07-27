import mpi.MPI;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

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
    }

    public static void removeFile(String fileName){
        System.out.println("removing file");
    }

    static void main(String[] args) {
        fileNames = new String[1000];   //not sure about the size yet, just first try
        fileCount = 0;   //initialization
        System.out.println("Master");
        System.out.println("TRY start listening");
        try {
        SampleAPI api = new SampleAPI();
//            Random r = new Random();   //need random later as the key, store it somewhere
//            long curTime=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();   //later combine the curtime with the rand to form the key
            RMIServer.start(PORT);
            RMIServer.register(api);
//            System.out.println(String.format("TRY simulating RMI for %d 'processes'", NCLIENTS_TEST));
//            for(int i = 0; i < NCLIENTS_TEST; i++) {
//
//                new Thread(() -> { RMIClient.upload(); }).start();
////                            new Thread(() -> { RMIClient.main(sourceFilePath); }).start();
//            }
            int[] bufRec = new int[BUF_LENGTH_TEST];
            MPI.COMM_WORLD.Recv(bufRec, 0, bufRec.length, MPI.INT, DEST, 0);  //expecting a message from tag 0

            api.quit();
        }
        catch(Exception e){
            System.out.println("ERR " + e.getMessage());
            e.printStackTrace();
        }
    }
}