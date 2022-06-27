package libsufec;

public interface MessageContent {
	byte[] toBytes();
	static MessageContent fromBytes(byte[] bytes) {
	}
}
