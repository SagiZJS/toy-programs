package chat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Server {

	private static final int PORT = 32345;

	private final ServerSocket server;

	private final BlockingQueue<Socket> sockets;

	private final BlockingQueue<String> messages;

	private final Thread[] threads;

	public Server(int port) throws IOException {
		this.server = new ServerSocket(PORT);
		this.sockets = new ArrayBlockingQueue<>(100);
		this.messages = new ArrayBlockingQueue<>(1000);
		threads = new Thread[2];
		serverStart();

	}

	public void serverShutDown() {
		for (Thread thread : threads) {
			if (thread.isAlive()) {
				thread.interrupt();
			}
		}
		try {
			server.close();
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	public Server() throws IOException {
		this(PORT);
	}

	private void readFrom(InputStream is) throws IOException {
		byte[] b = new byte[1024];
		String output = new String(new byte[0], "UTF-8");
		int len = -2;
		while ((len = is.read(b)) != -1) {
			output += new String(Arrays.copyOf(b, len), "UTF-8");
		}
		try {
			messages.put(output);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private void process(Socket socket) {
		try {
			sockets.put(socket);
			readFrom(socket.getInputStream());
			socket.shutdownInput();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
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
		Socket[] socketsArr = sockets.toArray(new Socket[0]);
		for (Socket socket : socketsArr) {
			if (!socket.isClosed()) {
				try (OutputStream os = socket.getOutputStream()) {
					os.write(message);
					socket.shutdownOutput();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			} else {
				sockets.remove(socket);
			}
		}
	}

	private void serverStart() {
		threads[0] = new Thread(new Runnable() {

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
					} catch (SocketException e) {
						continue;
					} catch (IOException ioe) {
						throw new RuntimeException(ioe);
					}
				}
			}
		});
		threads[1] = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!Thread.currentThread().isInterrupted()) {
					while (!messages.isEmpty()) {
						send();
					}
				}
			}
		});
		threads[0].start();
		threads[1].start();
	}

	public static void main(String[] args) {
		int port = argsCheck(args);
		try {
			Server s = new Server(port);
			Scanner scanner = new Scanner(System.in);
			System.out.println("Server started");
			while (true) {
				System.out.print("Server>>");
				if (scanner.next().equals("shutdown")) {
					s.serverShutDown();
					scanner.close();
					System.out.println("Server shut down");
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static int argsCheck(String[] args) {
		if (args.length == 1) {
			try {
				return Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				return PORT;
			}
		}
		return PORT;
	}

}
