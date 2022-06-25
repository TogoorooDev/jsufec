package libsufec;

import libsufec.SufecAddr;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import com.goterl.lazysodium.SodiumJava;
import com.goterl.lazysodium.LazySodiumJava;
import com.goterl.lazysodium.utils.Key;

public class Main {
	public static void main(String[] args) {
		byte[] id = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
		SufecAddr addr = new SufecAddr(Key.fromBytes(id), "server.com");
		//
		SodiumJava sodium = new SodiumJava();
		LazySodiumJava lazySodium = new LazySodiumJava(sodium, StandardCharsets.UTF_8);
	}
}
