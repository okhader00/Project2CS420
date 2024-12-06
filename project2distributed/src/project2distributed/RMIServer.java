package project2distributed;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RMIServer {
	public static void main(String[] args) {
		try {
			LocateRegistry.createRegistry(1099);	//Initialize RMI registry
			Server server = new Server();	//Initialize server
			
            Naming.rebind("rmi://localhost/Server", server);	//Name server for client to bind to it
            System.out.println("Server ready");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
