import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class JunitTest {
    @Test
      void test01() {
        System.out.println("junit中的test01");
    }
    @ParameterizedTest
    @ValueSource(strings = {"1","2","3"})
    void test02() {

    }

}

