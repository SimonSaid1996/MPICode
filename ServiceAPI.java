import java.util.Random;


public class ServiceAPI {
    //implement later, service API is the place where u process the url into the timestemp format
    Random r = new Random();   //need random later as the key, store it somewhere
    long curTime=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();   //later combine the curtime with the rand to form the key

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

}
