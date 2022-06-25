import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class SufecAddr {
	public String at;
	public Key id;
	public SufecAddr(Key id, String at) {
		this.id = id;
		this.at = at;
	}
	public byte[] toBytes() {
		byte[] idBytes = this.id.getAsBytes();
		byte[] atBytes = this.at.getBytes(StandardCharsets.UTF_8);
		byte[] output = new byte[32 + 1 + atBytes.length];
		System.arraycopy(id, 0, output, 0, id.length);
		output[id.length] = (byte) atBytes.length;
		System.arraycopy(atBytes, 0, output, id.length + 1, atBytes.length);
		return output;
	}
	public static SufecAddr fromBytes(ByteBuffer bytes) {
		byte[] idBytes = new byte[32];
		bytes.get(idBytes);
		Key id = Key.fromBytes(idBytes);
		int length = bytes.get();
		byte[] atBytes = new byte[length];
		bytes.get(atBytes);
		String at = new String(atBytes, StandardCharsets.UTF_8);
		return new SufecAddr(id, at);
	}
}
