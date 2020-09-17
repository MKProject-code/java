package mkproject.maskat.Papi;

public enum PapiHeadBase64 {
	BLUE_Information("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTY0MzlkMmUzMDZiMjI1NTE2YWE5YTZkMDA3YTdlNzVlZGQyZDUwMTVkMTEzYjQyZjQ0YmU2MmE1MTdlNTc0ZiJ9fX0="),
	GREEN_Information("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzU3NDcwMTBkODRhYTU2NDgzYjc1ZjYyNDNkOTRmMzRjNTM0NjAzNTg0YjJjYzY4YTQ1YmYzNjU4NDAxMDVmZCJ9fX0="),
	YELLOW_Information("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDAxYWZlOTczYzU0ODJmZGM3MWU2YWExMDY5ODgzM2M3OWM0MzdmMjEzMDhlYTlhMWEwOTU3NDZlYzI3NGEwZiJ9fX0="),
	Orange_Question_Mark("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGUxMDMwMTBkYmUyZGIyYWEwMzczZTYwYTgzZGRlNWJkY2I3NTg0OGM2ZmM3NzU4MzI2ZmVjZjczNDU5NDVlIn19fQ=="),
	Green_Question_Mark("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDdhMzcwMzgzODc1NThkYjY1ZmRlOTMyNGMxZTU3MjlmYmIzNDkwYWM2YWFkZDc3MjVmZjJjN2ExZmU2In19fQ=="),
	Discord("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzg3M2MxMmJmZmI1MjUxYTBiODhkNWFlNzVjNzI0N2NiMzlhNzVmZjFhODFjYmU0YzhhMzliMzExZGRlZGEifX19="),
	Sky("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2Q2ZDEzM2UxZGZmMTQyMGY1ZWM4MGNkMWYzZTdiYTIyMmZjOGMzM2ZhMDQ0OTMxZjY1ZTNiYzY5NzNhNDgifX19==");
	
	private String value;
//	private static Map<String, PapiHead> map = new HashMap<>();
	
    private PapiHeadBase64(final String string) {
        this.value = string;
    }
//    
//    static {
//        for (PapiHead papiHead : PapiHead.values()) {
//            map.put(papiHead.value, papiHead);
//        }
//    }
//    
//    public static PapiHead valueOf(final String headBase64) {
//        return (PapiHead) map.get(headBase64);
//    }
    
    public String getValue() {
        return value;
    }
}
