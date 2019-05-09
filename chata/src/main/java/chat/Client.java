package chat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Client {

	public static final int PORT = 32345;

	private static final String KEY = "asdqwerasdqrwqwt";

	private final Socket socket;

	private final String username;
	
	public Client(String username, String host) throws UnknownHostException {
		this(username, host, PORT);
	}

	public Client(String username, String host, int port) throws UnknownHostException {
		this.username = username;
		try {
			socket = new Socket(host, port);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getUsername() {
		return username;
	}

	public String commandRead() {
		return commandRead(KEY);
	}

	public void commandSend(String txt) {
		commandSend(txt, KEY);
	}

	public void commandSend(String txt, String key) {
		if (txt == null) return;
		txt = new SimpleDateFormat("HH:mm:ss").format(new Date()) + " " + this.username + ":" + txt;
		String encrypted = AES.encrypt(txt, key);
		send(encrypted);
	}

	public String commandRead(String key) {
		String decrypted = AES.decrypt(read(), key);
		return decrypted + '\n';
	}

	private void send(String txt) {
		try {
			OutputStream op = socket.getOutputStream();
			op.write(txt.getBytes("UTF-8"));
			socket.shutdownOutput();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String read() {
		StringBuilder output = new StringBuilder();
		try {
			InputStream is = socket.getInputStream();
			byte[] b = new byte[1024];
			int len = 0;
			while ((len = is.read(b)) != -1) {
				output.append(new String(Arrays.copyOf(b, len), "UTF-8"));
			}
			socket.shutdownInput();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return output.toString();
	}


}
