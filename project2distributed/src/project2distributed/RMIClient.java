package project2distributed;

import java.rmi.Naming;
import java.util.Scanner;


public class RMIClient {
	public static void main (String[] args) {
		try {
			ServerInterface server = (ServerInterface) Naming.lookup("//localhost/Server");
			Client client = new Client(server);	//Initialize client and connect it to server
			System.out.println("Client ready");
			
			client.login();	//Prompt for password
			
			Scanner kb = new Scanner(System.in);
			while (true) {
                System.out.println("Choose an option:\n1)Download\n2)Upload\n3)CRC Check\n4)Disconnect");
                String input = kb.nextLine();
                //kb.nextLine(); // Consume newline
                try {
                    if (input.equals("1")) {
                        //Download file
                        System.out.print("Enter file name to download: ");
                        String filename = kb.nextLine();
                        client.download(filename);
                    } else if (input.equals("2")) {
                        //Upload file
                        System.out.print("Enter file name to upload: ");
                        String filename = kb.nextLine();
                        client.upload(filename);
                    } else if (input.equals("3")) {
                        //CRC check
                        System.out.print("Enter data to check: ");
                        String data = kb.nextLine();
                        client.performCRCCheck(data);
                    } else if (input.equals("4")) {
                    	//Loop continues to ask for input until client chooses to disconnect
                        System.out.println("Successfully disconnected");
                        System.exit(0);
                    } else {
                        System.out.println("Invalid choice.");
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    System.exit(-1);
                }
            }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
