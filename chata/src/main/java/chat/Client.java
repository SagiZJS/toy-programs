package chat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Client {

	public static final int PORT = 32345;

	private static final String KEY = "asdqwerasdqrwqwt";

	private final SocketChannel socket;

	private final String username;
	
	
	public Client(String username, String host) throws UnknownHostException {
		this(username, host, PORT);
	}

	public Client(String username, String host, int port) throws UnknownHostException {
		this.username = username;
		try {
			System.out.println(host+port);
			socket = SocketChannel.open();
			socket.connect(new InetSocketAddress(host, port));
			socket.configureBlocking(false);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		commandSend("is connected");
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
		byte[] c = {','};
		try {
			send(encrypted+new String(c, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public String commandRead(String key) {
		StringBuilder output = new StringBuilder();
		for (String temp : read().split(",")) {
			output.append(AES.decrypt(temp, key)+'\n');
		}
		return output.toString();
	}

	private void send(String txt) {
		try {
			socket.write(ByteBuffer.wrap(txt.getBytes("UTF-8")));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String read() {
		StringBuilder output = new StringBuilder();
		try {
			
			byte[] b = new byte[1024];
			int len = 0;
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			while ((len = socket.read(buffer)) > 0) {
				buffer.flip();
				buffer.get(b, 0, len);
				output.append(new String(Arrays.copyOf(b, len), "UTF-8"));
				buffer.clear();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return output.toString();
	}


}
