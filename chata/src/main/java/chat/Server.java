package chat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class Server {

	private static final int PORT = 32345;

	private final ServerSocketChannel server;

	private final BlockingQueue<SocketChannel> sockets;

	private final BlockingQueue<String> messages;

	private final Thread[] threads;

	public Server(String hostIP,int port) throws IOException {
		server = ServerSocketChannel.open();
		server.bind(new InetSocketAddress(hostIP, port));
        server.configureBlocking(false);
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

	public Server(String host) throws IOException {
		this(host, PORT);
	}

	private void readFrom(SocketChannel sc) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte[] b;
		int len = 0;
		StringBuilder output = new StringBuilder();
		while ((len = sc.read(buffer)) > 0) {
			buffer.flip();
			b = new byte[len];
			buffer.get(b, 0, len);
			output.append(new String(b, "UTF-8"));
			//System.out.println(output);
			buffer.clear();
		}
		if (output.length() == 0) return;
		try {
			messages.put(output.toString());
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		
	}
	
//	private void readFrom(InputStream is) throws IOException {
//		byte[] b = new byte[1024];
//		String output = new String(new byte[0], "UTF-8");
//		int len = -2;
//		while ((len = is.read) > -1) {
//			System.out.println(len);
//			len = is.read(b);
//			output += new String(Arrays.copyOf(b, len), "UTF-8");
//			try {
//				messages.put(output);
//			} catch (InterruptedException e) {
//				throw new RuntimeException(e);
//			}
//		}
//	}

	private void process(SocketChannel socket) {
		try {
			sockets.put(socket);
			socket.configureBlocking(false);
			while (!Thread.currentThread().isInterrupted()) {
				readFrom(socket);
			}
		} catch (IOException e) {
			System.out.println("a Host is down\n");
			sockets.remove(socket);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}

	private void send() {
		byte[] message = null;
		try {
			String m = messages.take();
			if (m == null) return;
			message = m.getBytes("UTF-8");
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		
		SocketChannel[] socketsArr = sockets.toArray(new SocketChannel[0]);
		for (SocketChannel socket : socketsArr) {
			if (socket.isOpen()) {
				try {
					ByteBuffer buffer = ByteBuffer.wrap(message);
					socket.write(buffer);
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
						SocketChannel socket = server.accept();
						if (socket == null) continue;
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

	private static void panic() {
		System.out.println("usage:\n\tServer hostIP [port] ");
	}
	
	public static void main(String[] args) {
		String host = "";
		int port = 0;
		if (args.length <= 2 && args.length > 0) {
			try {
				port = args.length == 2 ? Integer.valueOf(args[1]) : PORT;
				new InetSocketAddress(args[0], port);
			} catch (NumberFormatException e) {
				port = PORT;
			} catch (IllegalArgumentException iae) {
				panic();
				return;
			}

		} else {
			panic();
			return;
		}
		try {
			Server s = new Server(host, port);
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

}
