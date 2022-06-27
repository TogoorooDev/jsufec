package libsufec;

import java.util.ArrayList;
import java.time.Instant;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import libsufec.MessageContent;
import libsufec.MessageHash;
import libsufec.SufecAddr;
import libsufec.Byteconv;
import libsufec.InvalidMessageException;

public class Message {
	public ArrayList<SufecAddr> otherRecipients;
	public Instant timestamp;
	public MessageContent content;
	public ArrayList<MessageHash> hashes;
	public byte[] toBytes() {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		output.write(this.otherRecipients.size());
		for (SufecAddr addr : this.otherRecipients) {
			byte[] addrBytes = addr.toBytes();
			output.write(addrBytes, 0, addrBytes.length);
		}
		output.write(Byteconv.longToBytes(this.timestamp.toEpochMilli()), 0, Long.BYTES);
		output.write(this.hashes.size());
		for (MessageHash hash : this.hashes) {
			byte[] hashBytes = hash.toBytes();
			output.write(hashBytes, 0, hashBytes.length);
		}
		byte[] contentBytes = this.content.toBytes();
		output.write(contentBytes, 0, contentBytes.length);
		return output.toByteArray();
	}
	// public Message fromBytes() throws InvalidMessageException {
	// }
}
