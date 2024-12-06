package project2distributed;

import java.rmi.server.UnicastRemoteObject;
import java.nio.file.*;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.zip.CRC32;
import java.util.Random;

public class Client extends UnicastRemoteObject implements ClientInterface {
	private final ServerInterface server;
	private final Path clientDirectory = Paths.get("clientFolder");
	
	public Client (ServerInterface server) throws RemoteException {
		super();
		this.server = server;
		
        if (!Files.exists(clientDirectory)) {	//Create client directory if it does not already exist
            try {
                Files.createDirectories(clientDirectory);
            } catch (Exception e) {
                throw new RemoteException("Failed to create client directory.", e);
            }
        }
	}
	
	@Override
	public void login() throws RemoteException {
		System.out.println("Enter password: ");	//Prompt user for password
		Scanner kb = new Scanner(System.in);
		String password = kb.nextLine();
		while(!server.authenticate(password)) {	//Keep asking for password until correct password is given
            System.out.println("Wrong password. Try again.");
            password = kb.nextLine();
		}
		System.out.println("Authentication successful.");
        System.out.println("Hi");
    }
	
	@Override
	public void download(String filename) throws RemoteException {
        try {
            byte[] fileData = server.processDownload(filename);	//Array to hold file contents of file being downloaded
            Path filePath = clientDirectory.resolve(filename);
            Files.write(filePath, fileData);	//Copy file contents into new file created in client directory
            System.out.println("File downloaded successfully to " + filePath.toString());
        } catch (Exception e) {
            throw new RemoteException("Failed to download file: " + filename, e);
        }
    }
	
	@Override
    public void upload(String filename) throws RemoteException {
        try {
            Path filePath = clientDirectory.resolve(filename);	//Get file path for given file
            byte[] fileData = Files.readAllBytes(filePath);	//Copy file contents into byte array
            System.out.println(server.processUpload(fileData, filename));	//Send byte array with file contents to server
        } catch (Exception e) {
            throw new RemoteException("Failed to upload file: " + filename, e);
        }
    }
	
	@Override
    public void performCRCCheck(String data) throws RemoteException {
        CRC32 crc = new CRC32();	//Creating standardized CRC32 object
        Random rand = new Random();	//Random class that will be used to help simulate data corruption
        String corrupted = data + "a";	//"Corrupted" message that will be used 
        crc.update(data.getBytes());	//CRC calculation
        int shortCRC = (int) (crc.getValue() & 0xFFFF);	//Truncating checksum to 16 bits
        String checksum = Long.toHexString(shortCRC);	//Converting checksum to hexadecimal string for comparison
        String serverChecksum;
        
        if (rand.nextInt(2) == 0) {	//Random chance to "corrupt" the data to ensure CRC actually works and does not return good data every time
            serverChecksum = server.verifyCRC(data.getBytes(), checksum);
        }
        else {
            serverChecksum = server.verifyCRC(corrupted.getBytes(), checksum);
        }
               
        System.out.println("Client checksum: " + checksum);
        System.out.println("Server checksum: " + serverChecksum);
        System.out.println("CRC Check: " + (checksum.equals(serverChecksum) ? "Good data" : "Bad data"));
    }
	

}
