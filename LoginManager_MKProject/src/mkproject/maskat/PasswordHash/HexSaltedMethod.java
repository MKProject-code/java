package mkproject.maskat.PasswordHash;

public abstract class HexSaltedMethod implements EncryptionMethod {

    public abstract int getSaltLength();

    @Override
    public abstract String computeHash(String password, String salt, String name);

    @Override
    public HashedPassword computeHash(String password, String name) {
        String salt = generateSalt();
        return new HashedPassword(computeHash(password, salt, null));
    }

    @Override
    public abstract boolean comparePassword(String password, HashedPassword hashedPassword, String name);

    @Override
    public String generateSalt() {
        return RandomStringUtils.generateHex(getSaltLength());
    }

    @Override
    public boolean hasSeparateSalt() {
        return false;
    }
}