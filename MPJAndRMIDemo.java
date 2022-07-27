/*
 *  MPJAndRMIDemo.java
 *
 *  This example demonstrates using RMI with MPJ
 *  Rank 0 hosts the RMI object, by which any MPI messaging call
 *  is performed via RMIServer.MPI_PROXY, instead of MPI.COMM_WORLD.
 *  For simplicity, the master node performs a local RMI call via
 *  RMIClient.
 *
 *  see: MasterNode, SampleAPI, RMIClient
 *
 *  (C) 2022 Ali Jannatpour <ali.jannatpour@concordia.ca>
 *
 *  This code is licensed under GPL.
 *
 *  How to run:
 *      startup class: 	runtime.starter.MPJRun
 *      args: 		-jar $MPJ_HOME$\lib\starter.jar com.comp6231.MPJAndRMIDemo -np 3 -dev multicore
 *      env-var: 		MPJ_HOME=...
 *      add-to-class-path:	mpj-lib...
 */

import mpi.Datatype;
import mpi.MPI;

import java.io.*;
import java.rmi.*;
import java.util.Random;
import mpi.Status;

public class MPJAndRMIDemo {
    static final int PORT = 6231;
    static final int MASTER = 0;
    static final int DEST = 1;
    static final int NCLIENTS_TEST = 1;    //NCLIENTS_TEST = 5;
    static final int BUF_LENGTH_TEST = 100;
    static int fileSize;
    public static void main(String[] args) {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        fileSize = 0;  //will be changed under master
        if(size < 2) {
            System.out.println("MPI size too low");
            return;
        }
        System.out.println("Hello world from <"+me+"> of <"+size+">");
        if(me == MASTER) {
            MasterNode.main(args);
         }
        if(me >0) {
            ChildNode.main(args);
        }
        MPI.Finalize();
    }








//    // API Remote Object
//
//    interface APIInterface extends Remote {
//        int foo(String a) throws RemoteException, FileNotFoundException;
//        int upload(byte[] buf) throws RemoteException, FileNotFoundException;
//        void quit() throws RemoteException;
//    }
//
//    // API Implementation
//
//    static class SampleAPI implements APIInterface {
//        private static Integer callno = 0;
//        public int foo(String a) throws RemoteException, FileNotFoundException {  //there is a simiar pic on the phone similating the behavior of the master node
//            int cn;
//            synchronized (callno) {
//                cn = ++callno;
//            }
//            System.out.println(String.format("** %d: calling foo", cn));
//            System.out.println("Equation received from client "+ cn+ " is : "+a);
//
//            char[] sbuf = new char[a.length()];
//            for (int i = 0; i < a.length(); i++) {
//                sbuf[i] = a.charAt(i);
//            }
//            int[] rbuf = new int[1];
//            //so the data cluster side is always 4096 bytes? sure it is enough? when the actual length exceed?
//
//
//            RMIServer.MPI_PROXY.Sendrecv(sbuf,0,sbuf.length,MPI.CHAR,DEST,cn,rbuf,0,rbuf.length,MPI.INT,DEST,cn);
//            System.out.println(String.format("** %d: foo called", cn));
//            return rbuf[0]; // receiving a message from DEST
//        }
//
//        @Override
//        public int upload(byte[] buf) throws RemoteException, FileNotFoundException {
//            int cn;
//            synchronized (callno) {
//                cn = ++callno;
//            }
//            System.out.println(String.format("** %d: calling foo", cn));
//            System.out.println("doing a upload");
//
//            /////////sending the first message
//            String a = "upload";
//            char[] sbuf = new char[a.length()];
//            for (int i = 0; i < a.length(); i++) {
//                sbuf[i] = a.charAt(i);
//            }
//            int[] rbuf = new int[1];
//            RMIServer.MPI_PROXY.Send(sbuf,0, sbuf.length,MPI.CHAR,DEST,cn);
//
//            //Send(final Object buf, final int offset, final int len, final Datatype datatype, final int dest, final int tag)
//            //////////
//
//            //so the data cluster side is always 4096 bytes? sure it is enough? when the actual length exceed?
//            RMIServer.MPI_PROXY.Sendrecv(buf,0,buf.length,MPI.BYTE,DEST,cn,rbuf,0,rbuf.length,MPI.INT,DEST,cn);
////          System.out.println(String.format("** %d: foo called", cn));
//
//            RMIServer.MPI_PROXY.Sendrecv(sbuf,0, sbuf.length,MPI.CHAR,DEST,cn,rbuf,0,rbuf.length,MPI.INT,DEST,cn);
//            return rbuf[0]; // receiving a message from DEST
//        }
//
//        public void quit() throws RemoteException {
//            RMIServer.stop();
//        }
//    }
//
//    static class MasterNode {   //try to think about storing the list of child node here in the master node
//        private static String[] fileNames ;   //initialize later
//        private static int fileCount;
//
//
//        public static byte[] retrieveData(String Url){
//            byte[] testing= new byte[10];
//            return testing;
//        }
//
//        public static void upload(int fileSize, byte[] data){
//
//            System.out.println("uploading files");
//        }
//
//        public static void listFile(){
//            System.out.println("listing the names of the files");
//        }
//
//        public static void removeFile(String fileName){
//            System.out.println("removing file");
//        }
//
//        private static void main(String[] args) {
//            fileNames = new String[10];   //not sure about the size yet, just first try
//            fileCount = 0;   //initialization
//            System.out.println("Master");
//            System.out.println("TRY start listening");
//            SampleAPI api = new SampleAPI();
////            Random r = new Random();   //need random later as the key, store it somewhere
////            long curTime=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();   //later combine the curtime with the rand to form the key
//
//            try {
//                //check the args here, and do the switch case part as well
//                String function = "Upload"; //  args[3];    //get the argument
//                //String fileN = args[4];       //think about how to open files from the name
//                switch (function){   //.toUpperCase()
//                    case "List":
//                        System.out.println("listing");
//
//                        break;
//                    case "Upload":
//                        System.out.println("upload");
//                        //////////  code to do upload, might need to change to user input file name here
//                        String[] sourceFilePath = {"Clientnote.txt","100"};   //assuming 100 as the filesize
//                        fileNames[fileCount] = sourceFilePath[0];
//                        fileCount = fileCount+1;
//                        fileSize = 100;   //assuming defining the fileSize here
//                        RMIServer.start(PORT);
//                        RMIServer.register(api);
//                        System.out.println(String.format("TRY simulating RMI for %d 'processes'", NCLIENTS_TEST));
//                        for(int i = 0; i < NCLIENTS_TEST; i++) {
//                            //try to send stream here
//                            InputStream fin = new FileInputStream(new File(sourceFilePath[0]));
//                            byte[] buffer = new byte[4096];   //might be changed later, not even sure if i should send byte or just char
//                            int bufSize = 0;
//                            try{
//                                bufSize = fin.read(buffer);
//                            }catch (Exception e){
//                                System.out.println("read exception "+e);
//                                System.out.println("happened in line 102");
//                            }
//                            new Thread(() -> { RMIClient.upload(buffer); }).start();
////                            new Thread(() -> { RMIClient.main(sourceFilePath); }).start();
//                        }
//
//                        int[] bufRec = new int[BUF_LENGTH_TEST];
//
//                        MPI.COMM_WORLD.Recv(bufRec, 0, bufRec.length, MPI.INT, DEST, 0);  //expecting a message from tag 0
//                        //////////////
//                        break;
//                    case "downLoad":
//                        System.out.println("downLoad");
//                        break;
//                    case "remove":
//                        System.out.println("remove");
//                        break;
//                    default:
//                        System.out.println("nothing happened");
//                        break;
//                }
//
//                api.quit();
//            }
//            catch(Exception e){
//                System.out.println("ERR " + e.getMessage());
//                e.printStackTrace();
//            }
//        }
//    }
//
//    static class ChildNode {
//        private static char[]buf;
//        private static int[]foo;
//        private static byte[]chunk = new byte[4096];   //as suggested size
//
//        public static byte[] retrieveData(String Url){
//            byte[] testing= new byte[10];   //assuming the size for now
//            return testing;
//        }
//
//        public static void upload(int fileSize, byte[] data){
//            System.out.println("uploading files");
//        }
//
//        public static void listFile(){
//            System.out.println("listing the names of the files");
//        }
//
//        public static void removeFile(String fileName){
//            System.out.println("removing file");
//        }
//
//        public static void main(String[] args) {
//                    System.out.println("child");
//
//                    byte[] objByte = new byte[4096];
//                    Object testInput = new Object();
//                    foo = new int[1];
//                    char[] buf = new char[10];
//                    for(int i = 0; i < NCLIENTS_TEST; i++) {
//
//                        //later, this part needs to be changed into reading the catagory first, then deciding the functions to use
//                           //***************
//                        //
//                        MPI.COMM_WORLD.Recv(buf, 0, buf.length, MPI.CHAR, MASTER, MPI.ANY_TAG);
//                        System.out.println("received message is "+String.valueOf(buf));
//
//                        //then do the upload function
//
//
//                        Status s = MPI.COMM_WORLD.Recv(objByte, 0, 4096, MPI.BYTE, MASTER, MPI.ANY_TAG);
//                        int tag = s.tag;
//                        //check the testInput is the same as the original inputStream
//                        /*FileInputStream testInput1 = (FileInputStream) testInput;
//                        try{
//                            testInput1.read(objByte);
//                        }catch (Exception E){
//                            System.out.println("got read exceptions in line 242");
//                        }*/
//
//                        try{
//                            for(int j =0;j<objByte.length;j++){
//                                System.out.print((char)objByte[j]);
//                            }
//                        }
//                        catch(Exception e) {
//                            System.out.println("Something went wrong! Reason: " + e.getMessage());
//                        }
//
//                        int answer = 1;
//                        foo[0] = answer;
//
//                        ////////  this part of the code is also the upload, think about how to store the .txt file
//
//                        ///////////
//                        MPI.COMM_WORLD.Send(foo, 0, 1, MPI.INT, MASTER,tag);
//                    }
//                    int[] buf1 = new int[1];
//                    buf1[0] = 0;
//                    MPI.COMM_WORLD.Send(buf1, 0, 1, MPI.INT, MASTER, 0);
//            }
//
//
//    }
//
//    static class RMIClient {    //got stuck somewhere here
//        //write down methods such as uploading and downloading here and call from the server
//
//        public static void upload(byte[] buf){
//            try {
//                System.out.println("OK looking up");
//                APIInterface remoteapi = (APIInterface)Naming.lookup(RMIServer.getURI(MPJAndRMIDemo.PORT, "SampleAPI"));
//                int res = remoteapi.upload(buf);
//                //inside of the interface, can set up multiple methods, upload and download, using streams
//
////                System.out.println(String.format("OK called foo "+eq+ " through RMI, result: %d", res));
//            }
//            catch(Exception e){
//                System.out.println("ERR " + e.getMessage());
//                e.printStackTrace();
//            }
//        }
//
//        public static void download(OutputStream OS){
//
//        }
//
//
//        public static void main(String[] args) {
//            try {
//                System.out.println("OK looking up");
//                APIInterface remoteapi = (APIInterface)Naming.lookup(RMIServer.getURI(MPJAndRMIDemo.PORT, "SampleAPI"));
//                int res = remoteapi.foo(args[0]);
//                //inside of the interface, can set up multiple methods, upload and download, using streams
//
////                System.out.println(String.format("OK called foo "+eq+ " through RMI, result: %d", res));
//            }
//            catch(Exception e){
//                System.out.println("ERR " + e.getMessage());
//                e.printStackTrace();
//            }
//        }
//    }
}