import java.io.*;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.Scanner;

class RMIClient {

    public static void upload(String sourceFilePath){
        try {
            System.out.println("upload: "+sourceFilePath);
            InputStream fin1 = new FileInputStream(new File(sourceFilePath));
            RemoteInputStream fin =  new RemoteInputStream(fin1);
            System.out.println("OK looking up");
            APIInterface remoteapi = (APIInterface) Naming.lookup(RMIServer.getURI(6231, "SampleAPI"));
            remoteapi.upload(fin,sourceFilePath);


        }
        catch(Exception e){
            System.out.println("ERR " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void list(){
        try{
            APIInterface remoteapi = (APIInterface) Naming.lookup(RMIServer.getURI(6231, "SampleAPI"));
            System.out.println("The list of files in Repo:");
            for (String s : remoteapi.listFiles()) {
                System.out.println(s);
            }
        }
        catch(Exception e){
            System.out.println("ERR " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void download(String sourceFilePath,String desFilePath){
        try {
            System.out.println("download: "+sourceFilePath);
            RemoteOuputStream os=new RemoteOuputStream(new FileOutputStream(desFilePath));
            APIInterface remoteapi = (APIInterface) Naming.lookup(RMIServer.getURI(6231, "SampleAPI"));
            remoteapi.download(sourceFilePath,os);
        }catch(Exception e){
            System.out.println("ERR " + e.getMessage());
            e.printStackTrace();
        }

    }
    public static void remove(String sourceFilePath){
        try {
            System.out.println("remove: "+sourceFilePath);
            APIInterface remoteapi = (APIInterface) Naming.lookup(RMIServer.getURI(6231, "SampleAPI"));
            remoteapi.remove(sourceFilePath);
        }catch(Exception e){
            System.out.println("ERR " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        try {
            Scanner myObj = new Scanner(System.in);
            while (true) {
                String s = myObj.nextLine();
                String[] command = s.split(" ");
                if (command[0].equals("end")) {
                    break;
                }

                switch (command[0].toUpperCase()) {
                    case "UPDATE":
                        if(command.length==2){
                            upload(command[1]);
                        }
                        else{
                            System.out.println("Error");
                        }
                        break;
                    case "DOWNLOAD":
                        if(command.length==3){
                            download(command[1],command[2]);
                        }
                        else{
                            System.out.println("Error");
                        }
                        break;
                    case "REMOVE":    //kind of fixed
                        if(command.length==2){
                            remove(command[1]);
                        }
                        else{
                            System.out.println("Error");
                        }
                        break;

                    case "TEST":    //kind of fixed
                        if(command.length==1){
                            upload("Clientnote.txt");
                            upload("notes.txt");
                            remove("Clientnote.txt");
                            download("notes.txt","test.txt");
                            list();
                        }
                        else{
                            System.out.println("Error");
                        }
                        break;

                    case "LIST":
                        if(command.length==1){
                            list();
                            break;
                        }

                    default:
                        System.out.println("Error");
                }
            }



        }
        catch(Exception e){
            System.out.println("ERR " + e.getMessage());
            e.printStackTrace();
        }
    }
}