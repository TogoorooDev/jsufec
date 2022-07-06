package jsufec;

import jsufec.SufecAddr;
import com.goterl.lazysodium.utils.Key;

public class Account {
    public SufecAddr addr;
    public Key SecKey;
    public String DeviceID;
}
