package fr.umontpellier.etu.dummy;

import java.util.ArrayList;

public class A extends B {
//	static class D {}
	int i = 9;
	public int j;
	private ArrayList<Integer> al = new ArrayList<>();

	public A() {
		this.j = 1;
		(new C()).getI();
		System.out.print("");
	}

	private void foo(int i, int j, float f) { C c = new C(); c.getI(); }
	private void oof(double d, String s) { foo(0, 1, 2); (new C()).getI(); }
	private void bar(Object o) {}
}
