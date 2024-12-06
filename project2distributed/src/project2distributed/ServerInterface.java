package project2distributed;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
	boolean authenticate(String password) throws RemoteException;
	byte[] processDownload(String filename) throws RemoteException;
    String processUpload(byte[] fileData, String filename) throws RemoteException;
    String verifyCRC(byte[] data, String checksum) throws RemoteException;
}
