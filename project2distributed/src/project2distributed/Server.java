package project2distributed;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.nio.file.*;
import java.util.zip.CRC32;

public class Server extends UnicastRemoteObject implements ServerInterface {
	private final String PASSWORD = "distributed";
	private final Path serverDirectory = Paths.get("serverFolder");
	
	public Server() throws RemoteException {
        super();
        if (!Files.exists(serverDirectory)) {	//Create server directory if it does not exist
            try {
                Files.createDirectories(serverDirectory);
            } catch (Exception e) {
                throw new RemoteException("Failed to create server directory.", e);
            }
        }
    }
	
	public boolean authenticate(String password) {
        if (PASSWORD.equals(password)) {
        	System.out.println("Hello");	//Make sure client password matches predefined password
        }
		return PASSWORD.equals(password);
    }
	
	public byte[] processDownload(String filename) throws RemoteException {
        Path filePath = serverDirectory.resolve(filename);
        try {
            return Files.readAllBytes(filePath);	//Reads all contents of a file and returns them
        } catch (Exception e) {
            throw new RemoteException("File not found or could not be read.", e);
        }
    }
	
	public String processUpload(byte[] fileData, String filename) throws RemoteException {
        Path filePath = serverDirectory.resolve(filename);
        try {
        	//Creating a file with the given name if it does not exist and write the contents from the client into the file
            Files.write(filePath, fileData, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return "File uploaded successfully.";
        } catch (Exception e) {
            throw new RemoteException("File upload failed.", e);
        }
    }
	
	public String verifyCRC(byte[] data, String checksum) throws RemoteException {
        CRC32 crc = new CRC32();	//Create standardized CRC32 object
        crc.update(data);	//Calculate checksum for given data from client
        int shortCRC = (int) (crc.getValue() & 0xFFFF);	//Truncating to 16 bits
        String calculatedChecksum = Long.toHexString(shortCRC); //Converting checksum to hexadecimal string for comparison
        return calculatedChecksum;
    }
}
