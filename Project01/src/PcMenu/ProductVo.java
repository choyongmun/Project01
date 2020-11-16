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
	 * 저장된 값(category, name, price)을 String타입으로 return한다.
	 */
	public String toString(){
		String category=null;
		switch(getCategory()) {
		case 1:
			category="음식";
			break;
		case 2:
			category="음료";
			break;
		case 3:
			category="간식";
			break;
		case 4:
			category="과자";
			break;
		}
		return "종류 : "+category+" / 이름 : "+getName()+" / 가격 : "+getPrice()+"\n";
	}
	
}
