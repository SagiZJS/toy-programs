package chat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;

public class FuckFinal {
	
	private static String readFromFile(String path) {
		try (FileInputStream reader = new FileInputStream(new File(path))) {
			StringBuilder output = new StringBuilder();
			int len = 0;
			byte[] b = new byte[1024];
			while ((len = reader.read(b)) != -1) {
				output.append(new String(Arrays.copyOf(b, len), "UTF-8"));
			}
			return output.toString();
		} catch (FileNotFoundException e) {
			System.out.println("error: file not found");
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
		return null;
	}
	
	private static boolean processCMDS(String cmd, Client client){
		String[] cmds = cmd.split(" ", 2);
		if (cmds[0].equals("send") && cmds.length == 2) {
			String message = null;
			if (cmds[1].split(" ", 2)[0].equals("-f")) {
				message = readFromFile(cmd.split(" ", 3)[2]);
			} else {
				message = cmd.split(" ", 2)[1];
			}
			client.commandSend(message);
		} else if (cmds[0].equals("exit!")) {
			return true;
		} else if (cmds[0].equals("read")){
			String message = client.commandRead();
			System.out.print(message);
			File file = null;
			file = new File("./log.txt");
			if (!file.isFile()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			try (FileOutputStream writer = new FileOutputStream(file, true)) {
				 writer.write(message.getBytes("UTF-8"));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return false;
	}
	
	private static Client argsCheck(String[] args) {
		Client client = null;
		if (args.length == 2) {
			try {
				client =  new Client(args[0], args[1]);
			} catch (UnknownHostException e) {
				System.out.println("host not found");
				panic();
			}
		} else if (args.length == 3) {
			try {
				client =  new Client(args[0], args[1], Integer.valueOf(args[2]));
			} catch (UnknownHostException e) {
				System.out.println("host not found");
				panic();
			} catch (NumberFormatException e) {
				panic();
			}
		} else {
			panic();
		}
		return client;
	}
	private static void panic() {
		System.out.println("usage:\n\tjava -cp fuckfinal.jar chat.FuckFinal username host [port]\n\n\t\tusername\tas long as you're identifibale\n\t\thost\t\t192.168.1.1 like this\n\t\t[port]\t\tport number");
	}

	public static void main(String[] args) {
		Client client = argsCheck(args);
		if (client ==null)
			return; 
		Scanner scanner = new Scanner(System.in);
		processCMDS("read", client);
		while (client != null) {
			System.out.print(client.getUsername() + ">>");
			if (scanner.hasNextLine()) {
				if (processCMDS(scanner.nextLine(), client)) {
					client.commandRead();
					break;
				}
			}
		}
		scanner.close();
	}

}
