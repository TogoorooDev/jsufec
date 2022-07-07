package jsufec;

import java.net.Socket;
import java.net.UnknownHostException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.lang.Exception;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import com.goterl.lazysodium.LazySodium;
import com.goterl.lazysodium.exceptions.SodiumException;
import com.goterl.lazysodium.interfaces.Box;
import com.goterl.lazysodium.interfaces.KeyExchange;
import com.goterl.lazysodium.utils.Key;
import com.sun.jna.ptr.ByteByReference;

public class Connection {
	private static int PORT = 49002;
	private Socket socket;
	private OutputStream output;
	private InputStream input;
	private Key serverKey;
	private Key sessionKey;
	private Stream stream;
	private byte[] nonce;
	private Key oldEphKeySec;
	private Key newEphKeySec;
	private Account account;


	public Key getServerKey() {
		return this.serverKey;
	}

	public Connection(String server, int port) throws IOException {
		this.nonce = new byte[Box.NONCEBYTES];
		Arrays.fill(this.nonce, (byte) 0);

		this.socket = new Socket(server, port);

		this.stream = new Stream(this.socket.getInputStream(), this.socket.getOutputStream(), this.nonce);
		this.stream.setInput(this.socket.getInputStream());
		this.stream.setOutput(this.socket.getOutputStream());
		this.serverKey = Key.fromBytes(this.stream.getInput().readNBytes(KeyExchange.PUBLICKEYBYTES));

	}

	public void send(LazySodium ls, Account recipient, Message message) throws Exception {
		Key sessionKey;
		String encryptedKey;

		int keyLength;
		byte[] encodedKeyLength;
		ByteBuffer keyLengthBuffer;

		byte[] encodedKey;
		ByteBuffer keyBuffer;

		ArrayList<ByteBuffer> keyList = new ArrayList<>();

		this.stream.getOutput().write((byte) 0x01); // Header for message sending
		sessionKey = Key.generate(ls, KeyExchange.PUBLICKEYBYTES); // Key used until connection is closed

		encryptedKey = ls.cryptoBoxSealEasy(sessionKey.toString(), this.serverKey); // this key is sent
		output.write(ls.toBinary(encryptedKey));

		this.stream.getOutput().write(recipient.DeviceID);
		encodedKeyLength = this.stream.getInput().readNBytes(4);
		keyLengthBuffer = ByteBuffer.wrap(encodedKeyLength);
		keyLength = keyLengthBuffer.getInt();

		if (keyLength <= 0){
			throw new Exception("Invalid Recipient Key");
		}

		//encodedKey = new byte[keyLength];
		encodedKey = this.stream.getInput().readNBytes(keyLength);
		keyBuffer = ByteBuffer.wrap(encodedKey);

		if (keyBuffer.position() % KeyExchange.PUBLICKEYBYTES != 0){
			throw new Exception("Invalid Key Length");
		}

		int i = 1;
		int workingBeg = 0;
		int workingEnd = KeyExchange.PUBLICKEYBYTES;
		int pos = 0;

		while (pos < KeyExchange.PUBLICKEYBYTES){
			keyList.add(ByteBuffer.wrap(Arrays.copyOfRange(keyBuffer.array(), workingBeg, workingEnd)));
			workingBeg = workingEnd + 1;
			workingEnd = KeyExchange.PUBLICKEYBYTES * i;
			pos += KeyExchange.PUBLICKEYBYTES * i;
			i++;
		}

		for (ByteBuffer buf : keyList){
			ByteBuffer copy = Crypto.EncryptMessage(ls, account, recipient.DeviceID, Key.fromBytes(buf.array()), message);
			output.write(copy.position());
			output.write(copy.array());
		}


	}

	public void login(LazySodium ls, Account account, Key oldEphKey, Key newEphKey, Key publicKey) throws IOException, SodiumException {
		String encId;

		this.sessionKey = Key.generate(ls, KeyExchange.PUBLICKEYBYTES);

		this.stream.getOutput().write((byte) 0);

		encId = ls.cryptoBoxSealEasy(account.addr.id.toString(), this.serverKey);
		this.stream.getOutput().write(ls.toBinary(encId));

		this.stream.getOutput().write(account.DeviceID);
		this.stream.getOutput().write(oldEphKey.getAsBytes());

	}

}
