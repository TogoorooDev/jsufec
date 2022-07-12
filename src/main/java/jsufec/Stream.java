package jsufec;

import com.goterl.lazysodium.LazySodium;
import com.goterl.lazysodium.exceptions.SodiumException;
import com.goterl.lazysodium.interfaces.Box;
import com.goterl.lazysodium.utils.Key;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Stream {
    private InputStream input;
    private OutputStream output;
    private byte[] nonce;
    private Key combinedKey;

    public Stream(InputStream in, OutputStream out, byte[] newNonce){
        this.input = in;
        this.output = out;
        this.nonce = newNonce;
    }

    public InputStream getInput() {
        return input;
    }
    public OutputStream getOutput() {
        return output;
    }
    public byte[] getNonce(){
        return nonce;
    }
    public Key getCombinedKey(){ return combinedKey; }
    public void setInput(InputStream in) {
        this.input = in;
    }
    public void setOutput(OutputStream out) {
        this.output = out;
    }
    public void setNonce(byte[] newNonce) {
        nonce = newNonce;
    }
    public void setCombinedKey(Key key){ combinedKey = key; }

    public void send(LazySodium ls, byte[] sendData, byte[] nonce) throws IOException, SodiumException {
        String encryptedData = ls.cryptoSecretBoxEasy(ls.toHexStr(sendData), nonce, this.combinedKey);
        this.nonce = Crypto.incrementNonce(this.nonce, Box.NONCEBYTES);
        this.output.write(ls.bytes(encryptedData));
    }

    public byte[] receive(LazySodium ls, int len ) throws IOException, SodiumException{
        byte[] out;
        String decryptedString;

        out = this.input.readNBytes(len);

        decryptedString = ls.cryptoSecretBoxOpenEasy(ls.toHexStr(out), this.nonce, this.combinedKey);

        this.nonce = Crypto.incrementNonce(this.nonce, Box.NONCEBYTES);

        return ls.bytes(decryptedString);
    }
}

