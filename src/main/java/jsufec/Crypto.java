package jsufec;

import java.nio.ByteBuffer;

import com.goterl.lazysodium.LazySodium;
import com.goterl.lazysodium.exceptions.SodiumException;
import com.goterl.lazysodium.interfaces.Box;
import com.goterl.lazysodium.interfaces.KeyExchange;
import com.goterl.lazysodium.interfaces.Sign;
import com.goterl.lazysodium.utils.Key;
import com.goterl.lazysodium.utils.KeyPair;


import jsufec.Account;
import jsufec.Message;


public class Crypto {
    public static ByteBuffer EncryptMessage(LazySodium ls, Account account, byte[] recipientID, Key recipientEph, Message msg) throws SodiumException{
        ByteBuffer out;
        String outstr;

        Key pub = Key.generate(ls, KeyExchange.PUBLICKEYBYTES);
        Key sec = Key.generate(ls, KeyExchange.SECRETKEYBYTES);

        Key sharedKey = Crypto.GenSharedKey(ls, account.DeviceID, sec, recipientID, recipientEph);
        byte[] nonce = ls.nonce(Box.NONCEBYTES);

        outstr = ls.cryptoSecretBoxEasy(msg.toString(), nonce, sharedKey);
        out = ByteBuffer.wrap(ls.bytes(outstr));
        return out;
    }

    public static Key GenSharedKey(LazySodium ls, byte[] ownid, Key owneph, byte[] theirid, Key theireph) throws SodiumException {
        String ownEphTheirEphStr = ls.cryptoBoxBeforeNm(owneph.getAsBytes(), theireph.getAsBytes());
        String ownIDTheirEphStr = ls.cryptoBoxBeforeNm(ownid, theireph.getAsBytes());
        String ownEphTheirIDStr = ls.cryptoBoxBeforeNm(owneph.getAsBytes(), theirid);

        byte[] ownEphTheirEph = ls.bytes(ownEphTheirEphStr);
        byte[] ownIDTheirEph = ls.bytes(ownIDTheirEphStr);
        byte[] ownEphTheirID = ls.bytes(ownEphTheirIDStr);

        byte[] x = new byte[Box.BEFORENMBYTES];
        byte[] y = new byte[Box.BEFORENMBYTES];

        for (int i = 0; i < Box.BEFORENMBYTES; i++){
            x[i] = (byte) (ownEphTheirEph[i] ^ ownIDTheirEph[i]);
        }

        for (int i = 0; i < Box.BEFORENMBYTES; i++){
            y[i] = (byte) (x[i] ^ ownEphTheirID[i]);
        }

        return Key.fromBytes(y);
    }

    public static byte[] incrementNonce(byte[] nonce, int size){
        ByteBuffer temp = ByteBuffer.wrap(nonce);
        int i = temp.getInt();
        ByteBuffer ret = ByteBuffer.allocate(size);
        ret.putInt(i);
        return ret.array();
    }
}
