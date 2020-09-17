package mkproject.maskat.PasswordHash;

import static mkproject.maskat.PasswordHash.HashUtils.isEqual;
import static mkproject.maskat.PasswordHash.HashUtils.sha256;

public class Sha256 extends HexSaltedMethod {

    @Override
    public String computeHash(String password, String salt, String name) {
        return "$SHA$" + salt + "$" + sha256(sha256(password) + salt);
    }

    @Override
    public boolean comparePassword(String password, HashedPassword hashedPassword, String name) {
        String hash = hashedPassword.getHash();
        String[] line = hash.split("\\$");
        return line.length == 4 && isEqual(hash, computeHash(password, line[2], name));
    }

    @Override
    public int getSaltLength() {
        return 16;
    }

}