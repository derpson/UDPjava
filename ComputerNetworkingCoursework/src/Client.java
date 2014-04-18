import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Scanner;

/**
 * 
 * @author Louise Deason <ldeaso01>
 */
public class Client {
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// 1. Reads an integer from keyboard input and stores its value in a UDP packet.
		// Hint: given the integer num, use the following line of code to build its related array
		// of 4 bytes: byte[] send = ByteBuffer.allocate(4).putInt(num).array();
		System.out.print("Please input an integer: ");
		Scanner in = new Scanner(System.in);
		int num = in.nextInt();
		byte[] sendData = ByteBuffer.allocate(4).putInt(num).array();
		InetAddress ip;
		try {
			ip = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			System.out.println("error getting host by name");
			return;
		}

		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, 1999);

		// 2. Sends the UDP packet to the server, on port number 1999;
		DatagramSocket clientSocket;
		try {
			clientSocket = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println("error making socket");
			return;
		}

		try {
			clientSocket.send(sendPacket);
		} catch (IOException e) {
			System.out.println("error sending packet");
			return;
		}

		while (true) {
			// 3. Listens for UDP packets from the server, until it receives a packet with a number
			// ² 0.
			byte receiveData[] = new byte[4];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				clientSocket.receive(receivePacket);
			} catch (IOException e) {
				System.out.println("error receiving packet");
				return;
			}

			// While listening:
			// (a) Once it receives a UDP packet from the server, it subtracts 2 from the integer
			// value num contained in it. Hint: given the array of 4 bytes receive, use the
			// following line of code to extract the integer value:
			// int num = ByteBuffer.wrap(receive).getInt();

			int receivedNum = ByteBuffer.wrap(receiveData).getInt();
			System.out.println("received " + receivedNum);
			receivedNum -= 2;

			// (b) Checks the integer value num: if the value is greater than 0 (num>0) then the
			// client stores it in a new UDP packet and sends the packet to the server; otherwise
			// (num<=0) the client terminates.

			if (receivedNum > 0) {
				byte replyData[] = ByteBuffer.allocate(4).putInt(receivedNum).array();
				DatagramPacket replyPacket = new DatagramPacket(replyData, replyData.length,
						receivePacket.getAddress(), receivePacket.getPort());

				try {
					clientSocket.send(replyPacket);
				} catch (IOException e) {
					System.out.println("error sending reply packet");
					return;
				}
			} else {
				System.out.println("terminating");
				break;
			}
		}
	}
}
