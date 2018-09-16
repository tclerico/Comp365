package threads;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

class BabyThreadX implements Runnable {
	public BabyThreadX() {
		System.out.println(Thread.currentThread().getName() + ": Baby is born");
	}

	public void run() {
		Socket client;

		try {
			client = new Socket("localhost", 9999);
			InputStream is = client.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String userInput;

			System.out.println(Thread.currentThread().getName() + ": Response from parent");
			while ((userInput = br.readLine()) != null) {
				System.out.println(userInput);
			}

		} catch (UnknownHostException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}

public class ParentAndBabyChat {
	public ParentAndBabyChat() throws InterruptedException {
		System.out.println(Thread.currentThread().getName() + //
				": Parent is born");
	}

	public void manageBaby() throws InterruptedException, IOException {
		Thread baby = new Thread(new BabyThread());
		System.out.println(Thread.currentThread().getName() + //
				": Parent created the baby");
		baby.start();

		ServerSocket serverSocket = new ServerSocket(9999);
		Socket client = serverSocket.accept();
		OutputStream os = client.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os);
		BufferedWriter bw = new BufferedWriter(osw);
		bw.write("Are you hungry?");
		bw.flush();
		bw.close();

		System.out.println(Thread.currentThread().getName() + //
				": Parent is done");
	}

	public static void main(String args[]) throws InterruptedException, IOException {
		ParentAndBabyChat parent = new ParentAndBabyChat();
		parent.manageBaby();
	}
}
