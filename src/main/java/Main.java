package libsufec;

import libsufec.SufecAddr;
import java.util.ArrayList;
import java.time.Instant;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import com.goterl.lazysodium.SodiumJava;
import com.goterl.lazysodium.LazySodiumJava;
import com.goterl.lazysodium.utils.Key;

public class Main {
	public static void main(String[] args) throws InvalidMessageException {
		byte[] id = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
		SufecAddr addr1 = new SufecAddr(Key.fromBytes(id), "server.com");
		byte[] id2 = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
		SufecAddr addr2 = new SufecAddr(Key.fromBytes(id2), "server.com");
		//
		SodiumJava sodium = new SodiumJava();
		LazySodiumJava lazySodium = new LazySodiumJava(sodium, StandardCharsets.UTF_8);

		MessageContent content = new MessageContentText("blah");
		Message msg = new Message(new ArrayList(), Instant.now(), new ArrayList(), content);
		byte[] bs = {0, 1, 2};
		Message msg2 = Message.fromBytes(ByteBuffer.wrap(bs));
	}
}
