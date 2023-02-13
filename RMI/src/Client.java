import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import  java.util.*;

public class Client {
    public static void main(String[] args) throws RemoteException {
        Client c = new Client();
        c.connectServer();
    }

    private void connectServer() throws RemoteException{

        try {
            Scanner sc = new Scanner(System.in);
            Registry reg = LocateRegistry.getRegistry("localhost",1098);
            RmiInterface ri = (RmiInterface) reg.lookup("myServer");
            //ri.uploadAuto();
            {
                Timer timer = new Timer();
                timer.schedule( new TimerTask()
                {
                    public void run()  {
                        try {
                            ri.uploadAuto();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, 0, 60*(1000*1));
            }
            System.out.println("1. Upload");
            System.out.println("2. Download");
            int choice = sc.nextInt();
            switch (choice){
                case 1: ri.upload();
                        break;
                case 2: ri.download();
                        break;
                default: System.out.println("wrong choice!!!");
            }

        }
        catch (NotBoundException | IOException e){
            System.out.println("exception : "+e);
        }
    }

}



