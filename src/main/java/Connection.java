package libsufec;

import java.net.Socket;
import java.net.UnknownHostException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import com.goterl.lazysodium.LazySodium;
import com.goterl.lazysodium.interfaces.KeyExchange;
import com.goterl.lazysodium.utils.Key;

public class Connection {
	static int PORT = 49002;
	Socket socket;
	public Key serverKey;
	public Connection(String server) throws IOException, UnknownHostException {
		this.socket = new Socket(server, PORT);
		InputStream recv = this.socket.getInputStream();
		this.serverKey = Key.fromBytes(recv.readNBytes(KeyExchange.PUBLICKEYBYTES));
	}
	public void send(LazySodium ls, Key recipient, Message message) throws IOException {
		OutputStream send = this.socket.getOutputStream();
		send.write(1);
		Key sessionKey = Key.generate(ls, KeyExchange.PUBLICKEYBYTES);
		encryptedKey = ls.cryptoBoxSealEasy(Key.toBytes(), this.serverKey);
	}
}
