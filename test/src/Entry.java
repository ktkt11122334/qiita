import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import anno.TestAnno;

/**
 *
 * reference site
 * https://blog1.mammb.com/entry/2015/03/31/001620
 * https://www.it-swarm.dev/ja/java/java%E3%81%8B%E3%82%89%E3%83%95%E3%82%A9%E3%83%AB%E3%83%80%E5%86%85%E3%81%AE%E3%81%99%E3%81%B9%E3%81%A6%E3%81%AE%E3%83%95%E3%82%A1%E3%82%A4%E3%83%AB%E3%82%92%E8%AA%AD%E3%81%BF%E5%8F%96%E3%82%8B%E6%96%B9%E6%B3%95%E3%81%AF%EF%BC%9F/968527708/
 *
 */
public class Entry {

  public static void main(String[] args) throws Exception {

    // テストクラスのルートパッケージを指定
    final String resourceName = "test";
    final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    final URL testRoot = classLoader.getResource(resourceName);
    final String appRootStr = classLoader.getResource(".").getFile().toString();



    try (Stream<Path> paths = Files.walk(Paths.get(testRoot.toURI()))) {

      paths.filter(Files::isRegularFile).forEach(path -> {


        // 実行するクラス名をパッケージ名込みで取得
        String targertAbsolutePath = path.normalize().toFile().getAbsolutePath().toString();
        String targetClassName = targertAbsolutePath.substring(appRootStr.length(), targertAbsolutePath.length()).replace(".class", "").replace("/", ".");

        // 実行するクラスとインスタンスを取得
        Class<?> testClass;
        Object testClassInstance;
        try {
          testClass = Class.forName(targetClassName);
          testClassInstance = testClass.newInstance();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }

        for( Method method : testClass.getMethods() ) {

          TestAnno testAnnotation = method.getAnnotation(TestAnno.class);
          if ( testAnnotation == null ) continue;

          try {
            if ( testAnnotation.testFlg() )
              method.invoke(testClassInstance);
          } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
          }

        }


      });


      System.out.println("Test End");
    }



  }

}
