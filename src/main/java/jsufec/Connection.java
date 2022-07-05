package jsufec;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.goterl.lazysodium.LazySodium;
import com.goterl.lazysodium.exceptions.SodiumException;
import com.goterl.lazysodium.interfaces.KeyExchange;
import com.goterl.lazysodium.utils.Key;

public class Connection {
	private static int PORT = 49002;
	private Socket socket;
	private OutputStream output;
	private InputStream input;
	private Key sessionKey;
	private Key serverKey;

	public Key getSessionKey(){
		return sessionKey;
	}

	public Key getServerKey() {
		return this.serverKey;
	}

	public Connection(String server, int port) throws IOException, UnknownHostException {
		this.socket = new Socket(server, port);
		this.input = this.socket.getInputStream();
		this.output = this.socket.getOutputStream();
		this.serverKey = Key.fromBytes(this.input.readNBytes(KeyExchange.PUBLICKEYBYTES));

	}

	public void send(LazySodium ls, Key recipient, Message message) throws IOException, SodiumException {
		output.write(1);
		if (this.sessionKey == null) this.sessionKey = Key.generate(ls, KeyExchange.PUBLICKEYBYTES);

		String encryptedKey = ls.cryptoBoxSealEasy(sessionKey.toString(), this.serverKey);
		output.write(encryptedKey.getBytes(StandardCharsets.UTF_8));

	}
	/*
	public void login(LazySodium ls, , Message message) throws IOException {

	}
	*/
}
