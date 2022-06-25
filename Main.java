import java.nio.ByteBuffer;

public class Main {
	public static void main(String[] args) {
		byte[] id = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
		SufecAddr addr = new SufecAddr(id, "server.com");
		ByteBuffer bs = ByteBuffer.wrap(addr.toBytes());
		System.out.println(bs);
		SufecAddr addr2 = SufecAddr.fromBytes(bs);
		System.out.println(addr2.id);
		System.out.println(addr2.at);
		//
		SodiumJava sodium = new SodiumJava();
		LazySodiumJava lazySodium = new LazySodiumJava(sodium, StandardCharsets.UTF_8);
		
	}
}
