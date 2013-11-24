package brick.comm;

public class Mensaje {
	
	private int id;
	private byte[] bytes;
	
	public Mensaje(int id, String str) {
		super();
		this.id = id;
		this.bytes = str.getBytes();
	}
	
	public Mensaje(int id, byte[] bytes) {
		super();
		this.id = id;
		this.bytes = bytes;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStr() {
		return new String(bytes);
	}
	public void setStr(String str) {
		this.bytes = str.getBytes();
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] partsMsg) {
		bytes = partsMsg;
		
	}
	
	

}
