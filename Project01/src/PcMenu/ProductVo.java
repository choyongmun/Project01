package PcMenu;

public class ProductVo {
	private int nu;
	private int category;
	private String name;
	private int price;
	public int getNu() {
		return nu;
	}
	public void setNu(int nu) {
		this.nu = nu;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	/**
	 * ����� ��(category, name, price)�� StringŸ������ return�Ѵ�.
	 */
	public String toString(){
		String category=null;
		switch(getCategory()) {
		case 1:
			category="����";
			break;
		case 2:
			category="����";
			break;
		case 3:
			category="����";
			break;
		case 4:
			category="����";
			break;
		}
		return "���� : "+category+" / �̸� : "+getName()+" / ���� : "+getPrice()+"\n";
	}
	
}
