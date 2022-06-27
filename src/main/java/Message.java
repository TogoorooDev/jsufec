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
	public ArrayList<MessageHash> hashes;
	public MessageContent content;
	public Message(
		ArrayList<SufecAddr> otherRecipients,
		Instant timestamp,
		ArrayList<MessageHash> hashes,
		MessageContent content
	) {
		this.otherRecipients = otherRecipients;
		this.timestamp = timestamp;
		this.hashes = hashes;
		this.content = content;
	}
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
	public Message fromBytes(ByteBuffer bytes) throws InvalidMessageException {
		ArrayList recipients = new ArrayList();
		// Deal with Java bytes being signed
		int numOtherRecipients = bytes.get() & 0xff;
		for (int i = 0; i < numOtherRecipients; i++) {
			recipients.add(SufecAddr.fromBytes(bytes));
		}
		Instant timestamp = Instant.ofEpochMilli(bytes.getLong());
		ArrayList hashes = new ArrayList();
		int numHashes = bytes.get() & 0xff;
		for (int i = 0; i < numHashes; i++) {
			hashes.add(MessageHash.fromBytes(bytes));
		}
		ByteBuffer contentBytes = bytes.slice();
		MessageContent content = MessageContent.fromBytes(contentBytes);
		return new Message(otherRecipients, timestamp, hashes, content);
	}
}
