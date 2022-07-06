package jsufec;

import java.nio.ByteBuffer;

import com.goterl.lazysodium.LazySodium;
import com.goterl.lazysodium.interfaces.KeyExchange;
import com.goterl.lazysodium.interfaces.Sign;
import com.goterl.lazysodium.utils.Key;
import com.goterl.lazysodium.utils.KeyPair;

import jsufec.Account;
import jsufec.Message;


public class Crypto {
    static ByteBuffer EncryptMessage(LazySodium ls, Account account, Key recipientID, Key recipientEph, Message msg){
        ByteBuffer out;

        Key pub = Key.generate(ls, KeyExchange.PUBLICKEYBYTES);
        Key sec = Key.generate(ls, KeyExchange.SECRETKEYBYTES);

        Key sharedKey = Key.

        return out;
    }

    static void tripledh(Key ownid, Key owneph, Key theirid, Key theireph){

    }
}
