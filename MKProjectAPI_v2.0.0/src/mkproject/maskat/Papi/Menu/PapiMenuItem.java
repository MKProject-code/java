package mkproject.maskat.Papi.Menu;

import java.util.List;
import java.util.Map;

public class PapiMenuItem {
	private Object slotOption;
	private Map<Object, Object> mapStorage;
	private List<Object> listSlotData;
	private Object[] arraySlotData;
	
	public PapiMenuItem(Object slotOption) {
		this.slotOption = slotOption;
	}
	public PapiMenuItem(Object slotOption, Map<Object, Object> mapSlotData) {
		this.slotOption = slotOption;
		this.mapStorage = mapSlotData;
	}
	public PapiMenuItem(Object slotOption, List<Object> listSlotData) {
		this.slotOption = slotOption;
		this.listSlotData = listSlotData;
	}
	public PapiMenuItem(Object slotOption, Object... arraySlotData) {
		this.slotOption = slotOption;
		this.arraySlotData = arraySlotData;
	}
	
	public Object getSlotOption()
	{
		return this.slotOption;
	}
	
	public SlotData getSlotData()
	{
		try {
			return new SlotData(this.arraySlotData[0]);
		} catch(Exception ex) {
			return null;
		}
	}
	
	public SlotData getSlotData(int index)
	{
		try {
			if(listSlotData==null)
				return new SlotData(this.arraySlotData[index]);
			else
				return new SlotData(this.listSlotData.get(index));
		} catch(Exception ex) {
			return null;
		}
	}
		
	public SlotData getSlotData(Object key) {
		Object value = this.mapStorage.get(key);
		if(value == null)
			return null;
		return new SlotData(value);
	}
	
	public class SlotData {
		private Object value;
		private SlotData(Object value) {
			this.value = value;
		}
		
		public Object getObject() { return value; }
		public String getString() { return (value instanceof String) ? (String)value : null; }
		public int getInteger() { return (value instanceof Long) ? (int)value : null; }
		public long getLong() { return (value instanceof Long) ? (long)value : null; }
	}
}
