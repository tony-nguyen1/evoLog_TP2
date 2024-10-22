package fr.umontpellier.etu.dummy;

import dummy.B;

public class C {
	int i;int j;
	
	public C() {
		i = 0;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		(new C()).getI();
		this.i = i;
	}

	@Override
	public String toString() {
		return "C [i=" + i + ", getI()=" + this.getI() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
}
