import mpi.MPI;
import mpi.Status;

import java.util.ArrayList;
import java.util.HashMap;

class ChildNode {
    private static char[]buf;
    private static byte[]chunk = new byte[4096];   //as suggested size
    static final int MASTER = 0;

    static final int quit = 0;

    static final int upload = 1;

    static final int download = 2;

    static final int remove = 3;
    static final int NCLIENTS_TEST = 1;

    static HashMap<String, byte[]> db;

    public ChildNode() {
        db=new HashMap<String, byte[]>();
    }

    public static byte[] download(String Url){
        return  db.get(Url);
    }

    public static void upload(int fileSize, byte[] data){
        System.out.println("uploading files");
    }


    public static void removeFile(String fileName){
        db.remove(fileName);
        System.out.println("removing file");
    }

    public static void main(String[] args) {
        System.out.println("child");
        db= new HashMap<String, byte[]>();

        byte[] objByte = new byte[140];
        Object testInput = new Object();
        int[]foo = new int[1];
        char[] buf = new char[10];
        while (true){
            //later, this part needs to be changed into reading the catagory first, then deciding the functions to use
            //***************
            //
            //MPI.COMM_WORLD.Recv(buf, 0, buf.length, MPI.CHAR, MASTER, MPI.ANY_TAG);
//            System.out.println("received message is "+String.valueOf(buf));
            //then do the upload function
            Status s = MPI.COMM_WORLD.Recv(objByte, 0, 140, MPI.BYTE, MASTER, MPI.ANY_TAG);
            int tag = s.tag;
//            System.out.println("***********************"+tag);
            byte info[] = new byte[100];
            System.out.println("***********************"+new String(info));
            byte data[] = new byte[objByte.length-info.length];
            System.arraycopy(objByte, 0, info, 0, info.length);
            System.arraycopy(objByte, info.length, data, 0, data.length);
            switch (tag){   //.toUpperCase()
                case quit:
                        System.out.println("listing");

                        break;
                case upload:

                    db.put(new String(info),data);
                    int answer = 1;
                    foo[0] = answer;
                    MPI.COMM_WORLD.Send(foo, 0, 1, MPI.INT, MASTER,tag);
                    try{
                        System.out.print(new String(data));
                    }
                    catch(Exception e) {
                        System.out.println("Something went wrong! Reason: " + e.getMessage());
                    }

//                    System.out.println("done upload");
                        break;
                case download:
                    data=download(new String(info));
                        System.out.println("downLoad");
                    MPI.COMM_WORLD.Send(data, 0, data.length, MPI.BYTE, MASTER,tag);
                        break;
                case remove:
                        removeFile(new String(info));
                        break;
                default:
                        System.out.println("nothing happened");
                        break;
                }
            //check the testInput is the same as the original inputStream
                        /*FileInputStream testInput1 = (FileInputStream) testInput;
                        try{
                            testInput1.read(objByte);
                        }catch (Exception E){
                            System.out.println("got read exceptions in line 242");
                        }*/


//            MPI.COMM_WORLD.Send(foo, 0, foo.length, MPI.BYTE, MASTER,tag);
//            int answer = 1;
//            foo[0] = answer;
//            MPI.COMM_WORLD.Send(foo, 0, 1, MPI.INT, MASTER,tag);
        }
//        int[] buf1 = new int[1];
//        buf1[0] = 0;
//        MPI.COMM_WORLD.Send(buf1, 0, 1, MPI.INT, MASTER, 0);
    }


}
