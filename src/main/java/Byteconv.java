package libsufec;

import java.nio.ByteBuffer;

public class Byteconv {
	public static byte[] longToBytes(long x) {
	    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
	    buffer.putLong(x);
	    return buffer.array();
	}
	// public long bytesToLong(byte[] bytes) {
	    // ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
	    // buffer.put(bytes);
	    // buffer.flip();//need flip
	    // return buffer.getLong();
	// }
}
