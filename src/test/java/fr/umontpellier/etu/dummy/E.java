package fr.umontpellier.etu.dummy;

public class E {
	int i;int j;

	public E() {
		i = 0;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		(new E()).getI();
		this.i = i;
	}

	@Override
	public String toString() {
		return "E [i=" + i + ", getI()=" + this.getI() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
}
