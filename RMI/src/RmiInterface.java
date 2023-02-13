import java.io.IOException;
import java.rmi.*;

public interface RmiInterface extends Remote {
    public void upload() throws IOException;
   public void uploadAuto() throws IOException;
    public void download() throws RemoteException;
}
