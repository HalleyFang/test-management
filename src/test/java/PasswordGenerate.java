import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;

public class PasswordGenerate {

    public static void main(String[] args) {
        Md5Hash md5Hash =
                new Md5Hash("my123", ByteSource.Util.bytes("zWsVzLCE"),1024);
        System.out.println(md5Hash.toHex());
    }
}
