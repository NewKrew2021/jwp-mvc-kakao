package core.mvc.tobe;

import core.mvc.handlermapping.ControllerScanner;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {
    @Test
    void scan() {
        ControllerScanner scanner = new ControllerScanner(new String[]{"core.mvc.tobe"});

        Set<Class<?>> classes = scanner.scan();

        assertThat(classes).contains(MyController.class);
    }
}