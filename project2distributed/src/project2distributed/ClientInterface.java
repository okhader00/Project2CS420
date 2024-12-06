package project2distributed;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
	void login() throws RemoteException;
	void download(String filename) throws RemoteException;
	void upload(String filename) throws RemoteException;
	void performCRCCheck(String data) throws RemoteException;
}
