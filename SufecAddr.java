import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class SufecAddr {
	public String at;
	public byte[] id;
	public SufecAddr(byte[] id, String at) {
		this.id = id;
		this.at = at;
	}
	public byte[] toBytes() {
		byte[] atBytes = this.at.getBytes(StandardCharsets.UTF_8);
		byte[] output = new byte[32 + 1 + atBytes.length];
		System.arraycopy(this.id, 0, output, 0, this.id.length);
		output[this.id.length] = (byte) atBytes.length;
		System.arraycopy(atBytes, 0, output, this.id.length + 1, atBytes.length);
		return output;

		// ByteBuffer concat = ByteBuffer.allocate(32 + 1 + at.length);
		// concat.put(this.id);
		// concat.put((byte) at.length);
		// concat.put(at);
		// concat.position(0);
		// return concat;
	}
	public static SufecAddr fromBytes(ByteBuffer bytes) {
		byte[] id = new byte[32];
		bytes.get(id);
		int length = bytes.get();
		byte[] atBytes = new byte[length];
		bytes.get(atBytes);
		String at = new String(atBytes, StandardCharsets.UTF_8);
		return new SufecAddr(id, at);
	}
}
