package jsufec;

import java.io.IOException;
import java.util.ArrayList;
import java.time.Instant;
import java.nio.charset.StandardCharsets;
import com.goterl.lazysodium.SodiumJava;
import com.goterl.lazysodium.LazySodiumJava;
import com.goterl.lazysodium.utils.Key;

public class Main {
	// Read the protocol docs at https://yujiri.xyz/sufec.gmi if you want to understand this
	public static void main(String[] args) throws InvalidMessageException {
		int PORT = 49002;
		String server = "server.com";

		byte[] id = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
		SufecAddr addr1 = new SufecAddr(Key.fromBytes(id), server);
		byte[] id2 = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
		SufecAddr addr2 = new SufecAddr(Key.fromBytes(id2), server);

		SodiumJava sodium = new SodiumJava();
		LazySodiumJava lazySodium = new LazySodiumJava(sodium, StandardCharsets.UTF_8);

		MessageContent content = new MessageContentText("blah");
		Message msg = new Message(new ArrayList(), Instant.now(), new ArrayList(), content);
		try {
			Connection con = new Connection(server, PORT);
		} catch (Exception e){
			System.out.println(e);
			return;
		}


	}
}
