package testunit;

import junit.textui.TestRunner;

public class Test {

	public static void main(String[] args) {
		TestRunner.run(TestData.class);
		TestRunner.run(TestEngine.class);
	}

}
