import java.util.Random;

public class testing {   //deleting file function

    /*//////////listfile function, jsut a loop and print
    public static void listFile(){
        System.out.println("listing the names of the files");
        for(int i =0;i<fileNames.length;i++){
            System.out.println(fileNames[i]);
        }
    }*/

    ////////////below is the part needed
    String[]fileNames={"a","b","c","d"};
    boolean deleted = false;
    public void removeFile(String fileName){//remove file function
        System.out.println("removing file");
        for(int i =0;i<fileNames.length;i++){
            if(fileNames[i].equals(fileName) ){
                deleted = true;
            }
            if(deleted){
                if(i!=fileNames.length-1){
                    fileNames[i] = fileNames[i+1];
                }
                else{
                    fileNames[i] = null;
                }
            }
        }
    }
    //////////////////////
    public void printFiles(){
        for(int i =0;i<fileNames.length;i++){
            System.out.println(fileNames[i]);
        }
    }


    public testing(){

    }
    public static void main(String[] args) {
        testing a = new testing();
        a.removeFile("c");
        a.printFiles();

        /////////// for the random filename generation part， jsut the file name plus the time stamp
        long curTime=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        String fileName = String.valueOf(curTime);
        System.out.println("file name is "+fileName);
        ///////

    }
}
