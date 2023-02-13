import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.io.*;
import java.util.concurrent.*;
import java.nio.file.*;

import org.apache.commons.io.*;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


public class Server extends UnicastRemoteObject implements RmiInterface {

    String currentDir = System.getProperty("user.dir");
    protected Server() throws RemoteException {
    }

    @Override
    public void upload() throws IOException {
        String source = currentDir + "/ClientFolder";
        File srcDir = new File(source);
        String destination = currentDir + "/DropBox";
        File destDir = new File(destination);
        FileUtils.cleanDirectory(destDir);
        FileUtils.copyDirectory(srcDir, destDir,null,false, REPLACE_EXISTING );
        System.out.println("Auuto-upload in progress...");
    }

    @Override
    public void uploadAuto() throws IOException {
        System.out.println("auto upload");
        Path serverFile = Paths.get(currentDir + "/DropBox");
        Path clientFile = Paths.get(currentDir + "/ClientFolder");
        long serverTime = Files.getLastModifiedTime(serverFile).to(TimeUnit.SECONDS);
        long clientTime = Files.getLastModifiedTime(clientFile).to(TimeUnit.SECONDS);
        System.out.println("serverTime "+serverTime);
        System.out.println("clientTime "+clientTime);
        if (serverTime < clientTime) {
            System.out.println("Dropbox time is old than the client time...");
            System.out.println("Initiating Auto Upload...");
            upload();
            System.out.println("Autoupload successful...");
        }
    }

    @Override
    public void download() throws RemoteException {
    }

    public static void main(String[] args) throws RemoteException {
        try {
            Registry reg = LocateRegistry.createRegistry(1098);
            reg.rebind("myServer", new Server());
            System.out.println("server is ready and running...");
        } catch (RemoteException e) {
            System.out.println("exception: " + e);
        }
    }
}
