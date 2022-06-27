package libsufec;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import libsufec.MessageContentText;
import libsufec.InvalidMessageException;

public interface MessageContent {
	byte[] toBytes();
	static MessageContent fromBytes(ByteBuffer buf) throws InvalidMessageException {
		byte[] contentBytes = new byte[buf.array().length - buf.arrayOffset() - 1];
		System.arraycopy(buf.array(), buf.arrayOffset() + 1, contentBytes, 0, contentBytes.length);
		switch (buf.get()) {
			case 0:
				return new MessageContentText(new String(contentBytes, StandardCharsets.UTF_8));
			default:
				throw new InvalidMessageException();
		}
	}
}
