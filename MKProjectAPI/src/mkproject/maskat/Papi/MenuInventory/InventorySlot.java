package mkproject.maskat.Papi.MenuInventory;

import java.util.HashMap;
import java.util.Map;

public enum InventorySlot {
	ROW1_COLUMN1(0),
	ROW1_COLUMN2(1),
	ROW1_COLUMN3(2),
	ROW1_COLUMN4(3),
	ROW1_COLUMN5(4),
	ROW1_COLUMN6(5),
	ROW1_COLUMN7(6),
	ROW1_COLUMN8(7),
	ROW1_COLUMN9(8),
	
	ROW2_COLUMN1(9),
	ROW2_COLUMN2(10),
	ROW2_COLUMN3(11),
	ROW2_COLUMN4(12),
	ROW2_COLUMN5(13),
	ROW2_COLUMN6(14),
	ROW2_COLUMN7(15),
	ROW2_COLUMN8(16),
	ROW2_COLUMN9(17),
	
	ROW3_COLUMN1(18),
	ROW3_COLUMN2(19),
	ROW3_COLUMN3(20),
	ROW3_COLUMN4(21),
	ROW3_COLUMN5(22),
	ROW3_COLUMN6(23),
	ROW3_COLUMN7(24),
	ROW3_COLUMN8(25),
	ROW3_COLUMN9(26),
	
	ROW4_COLUMN1(27),
	ROW4_COLUMN2(28),
	ROW4_COLUMN3(29),
	ROW4_COLUMN4(30),
	ROW4_COLUMN5(31),
	ROW4_COLUMN6(32),
	ROW4_COLUMN7(33),
	ROW4_COLUMN8(34),
	ROW4_COLUMN9(35),
	
	ROW5_COLUMN1(36),
	ROW5_COLUMN2(37),
	ROW5_COLUMN3(38),
	ROW5_COLUMN4(39),
	ROW5_COLUMN5(40),
	ROW5_COLUMN6(41),
	ROW5_COLUMN7(42),
	ROW5_COLUMN8(43),
	ROW5_COLUMN9(44),
	
	ROW6_COLUMN1(45),
	ROW6_COLUMN2(46),
	ROW6_COLUMN3(47),
	ROW6_COLUMN4(48),
	ROW6_COLUMN5(49),
	ROW6_COLUMN6(50),
	ROW6_COLUMN7(51),
	ROW6_COLUMN8(52),
	ROW6_COLUMN9(53);
	
	private int value;
	private static Map<Integer, InventorySlot> map = new HashMap<>();
	
    private InventorySlot(final int value) {
        this.value = value;
    }
    
    static {
        for (InventorySlot inventorySlot : InventorySlot.values()) {
            map.put(inventorySlot.value, inventorySlot);
        }
    }
    
    public static InventorySlot valueOf(final int slotId) {
        return (InventorySlot) map.get(slotId);
    }
    
    public int getValue() {
        return value;
    }
}