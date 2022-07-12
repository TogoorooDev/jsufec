package jsufec;

import jsufec.SufecAddr;
import com.goterl.lazysodium.utils.Key;
import com.goterl.lazysodium.utils.KeyPair;

public class Account {
    public SufecAddr addr;
    public Key SecKey; // optional
    public Key PubKey;
    public byte[] DeviceID;

    public Account(SufecAddr a, KeyPair k, byte[] d){
        this.addr = a;
        this.PubKey = k.getPublicKey();
        this.SecKey = k.getSecretKey();
        this.DeviceID = d;
    }

    public Account(SufecAddr a, Key k, byte[] d){
        this.addr = a;
        this.PubKey = k;
        this.SecKey = null;
        this.DeviceID = d;
    }
}
