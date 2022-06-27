package libsufec;

import java.time.Instant;
import java.nio.ByteBuffer;
import com.goterl.lazysodium.interfaces.Hash;

public class MessageHash {
	public Instant timestamp;
	public byte[] hash;
	public MessageHash(Instant timestamp, byte[] hash) {
		this.timestamp = timestamp;
		this.hash = hash;
	}
	public byte[] toBytes() {
		byte[] output = new byte[Long.BYTES + Hash.SHA512_BYTES];
		byte[] timestampBytes = Byteconv.longToBytes(this.timestamp.toEpochMilli());
		System.arraycopy(timestampBytes, 0, output, 0, Long.BYTES);
		System.arraycopy(this.hash, 0, output, Long.BYTES, Hash.SHA512_BYTES);
		return output;
	}
	public static MessageHash fromBytes(ByteBuffer bytes) {
		Instant timestamp = Instant.ofEpochMilli(bytes.getLong());
		byte[] hash = new byte[128];
		bytes.get(hash);
		return new MessageHash(timestamp, hash);
	}
}
