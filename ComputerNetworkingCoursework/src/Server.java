import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;

/**
 * A UDP server listening on port 1999. Accepts packets of 4 bytes, which are assumed to be an
 * int. The server will send a UDP packet in reply with 2 less than the int it received.
 * 
 * @author Louise Deason <ldeaso01>
 */
public class Server {

	/**
	 * Starts a server listening on port 1999. TODO(ldeas01): elaborate.
	 * 
	 * @param args command line arguments, currently ignored.
	 */
	public static void main(String[] args) {
		// 1. Listens on port 1999;
		DatagramSocket serverSocket;
		try {
			serverSocket = new DatagramSocket(1999);
		} catch (SocketException e) {
			System.err.println("error making socket");
			return;
		}

		while (true) {
			// 2. For each UDP packet it receives from a client:
			byte receiveData[] = new byte[4];
			DatagramPacket receivedPacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				serverSocket.receive(receivedPacket);
			} catch (IOException e) {
				System.err.println("error receiving packet");
				return;
			}

			// (a) extracts the integer value n contained in it; (b) decreases
			// the value of n by 2;
			int receivedInt = ByteBuffer.wrap(receiveData).getInt();
			receivedInt -= 2;

			// (c) sends back to the client a UDP packet containing the new
			// value of n.
			byte replyData[] = ByteBuffer.allocate(4).putInt(receivedInt).array();
			DatagramPacket replyPacket = new DatagramPacket(replyData, replyData.length,
					receivedPacket.getAddress(), receivedPacket.getPort());

			try {
				serverSocket.send(replyPacket);
			} catch (IOException e) {
				System.out.println("error sending reply packet");
				return;
			}
		}
	}
}
