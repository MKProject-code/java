package mkproject.maskat.PasswordHash;

public enum MessageDigestAlgorithm {

    MD5("MD5"),

    SHA1("SHA-1"),

    SHA256("SHA-256"),

    SHA512("SHA-512");

    private final String key;

    MessageDigestAlgorithm(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}