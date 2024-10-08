package dummy;

import java.util.ArrayList;
import dummy.C;

public class A extends B {
	static class D {}
	int i = 9;
	public int j;
	private ArrayList<Integer> al = new ArrayList<Integer>();
	
	public A() {
		this.j = 1;
	}
	
	private void foo(int i, int j, float f) { dummy.C c = new C(); c.getI(); }
	private void oof(double d, String s) { foo(0, 1, 2); }
	private void bar(Object o) {}
}
