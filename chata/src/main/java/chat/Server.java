package chat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Server {
	
	private static final int PORT = 32345;
	
	private final ServerSocket server;
	
	private final BlockingQueue<Socket> sockets;
	
	private final BlockingQueue<String> messages;
	
	public Server(int port) throws IOException {
		this.server = new ServerSocket(PORT);
		this.sockets = new ArrayBlockingQueue<>(100);
		this.messages = new ArrayBlockingQueue<>(1000);
		serverStart();
		
	}

	private void readFrom(InputStream is) throws IOException {
		byte[] b = new byte[1024];
		String output = new String("UTF-8");
		int len = -2;
		while ((len = is.read(b)) != -1) {
			output += new String(Arrays.copyOf(b, len), "UTF-8");
		}
		try {
			messages.put(output);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		is.close();
	}
	private void process(Socket socket) {
		try {
			readFrom(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void send() {
		byte[] message = null;
		try {
			message = messages.take().getBytes("UTF-8");
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		for (Socket socket : sockets) {
			if (!socket.isClosed()) {
				try (OutputStream os = socket.getOutputStream()){
					os.write(message);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			} else {
				//should remove socket
			}
		}
	}
	
	private void serverStart() {
		Thread listening = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (!Thread.currentThread().isInterrupted()) {
					try {
						Socket socket = server.accept();
						new Thread(new Runnable() {
							@Override
							public void run() {
								process(socket);
							}
						}).start();
					} catch (IOException ioe) {
						throw new RuntimeException(ioe);
					}
				}
			}
		});
		Thread sending = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!Thread.currentThread().isInterrupted()) {
					while (!messages.isEmpty()) {
						send();
					}
				}
			}
		});
	}
	

	public static void main(String[] args) {

	}

}
