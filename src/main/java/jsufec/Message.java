package jsufec;

import java.util.ArrayList;
import java.util.Date;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.BufferUnderflowException;
import java.sql.Timestamp;

import jsufec.ByteConv;

public class Message {
	public ArrayList<SufecAddr> otherRecipients;
	public Timestamp timestamp;
	public ArrayList<MessageHash> hashes;
	public MessageContent content;
	public Message(
		ArrayList<SufecAddr> otherRecipients,
		Timestamp timestamp,
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
		output.write(ByteConv.longToBytes(this.timestamp.getTime()), 0, Long.BYTES);
		output.write(this.hashes.size());
		for (MessageHash hash : this.hashes) {
			byte[] hashBytes = hash.toBytes();
			output.write(hashBytes, 0, hashBytes.length);
		}
		byte[] contentBytes = this.content.toBytes();
		output.write(contentBytes, 0, contentBytes.length);
		return output.toByteArray();
	}
	public static Message fromBytes(ByteBuffer bytes) throws InvalidMessageException {
		try {
			ArrayList recipients = new ArrayList();
			// Deal with Java bytes being signed
			int numOtherRecipients = bytes.get() & 0xff;
			for (int i = 0; i < numOtherRecipients; i++) {
				recipients.add(SufecAddr.fromBytes(bytes));
			}
			Timestamp timestamp = new Timestamp(bytes.getLong());
			ArrayList hashes = new ArrayList();
			int numHashes = bytes.get() & 0xff;
			for (int i = 0; i < numHashes; i++) {
				hashes.add(MessageHash.fromBytes(bytes));
			}
			ByteBuffer contentBytes = bytes.slice();
			MessageContent content = MessageContent.fromBytes(contentBytes);
			return new Message(recipients, timestamp, hashes, content);
		} catch (BufferUnderflowException e) {
			throw new InvalidMessageException();
		}
	}
}
