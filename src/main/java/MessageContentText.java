package libsufec;

import java.nio.charset.StandardCharsets;

import libsufec.MessageContent;

public class MessageContentText implements MessageContent {
	public String text;
	public byte[] toBytes() {
		byte[] textBytes = this.text.getBytes(StandardCharsets.UTF_8);
		byte[] result = new byte[textBytes.length + 1];
		result[0] = 0;
		System.arraycopy(textBytes, 0, result, 1, textBytes.length);
		return result;
	}
}
