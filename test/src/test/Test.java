package test;

import anno.TestAnno;

public class Test {

  @TestAnno(testFlg=true)
  public void testMethod() {


    String className = this.getClass().getName();
    String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
    System.out.println("invoked " + className + ":" + methodName);

  }

  @TestAnno(testFlg=false)
  public void testMethod2() {


    String className = this.getClass().getName();
    String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
    System.out.println("invoked " + className + ":" + methodName);

  }

  @TestAnno(testFlg=true)
  public void testMethod3() {


    String className = this.getClass().getName();
    String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
    System.out.println("invoked " + className + ":" + methodName);

  }

}
