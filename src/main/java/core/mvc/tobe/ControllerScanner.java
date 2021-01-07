package core.mvc.tobe;

import core.annotation.web.Controller;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.util.Set;

public class ControllerScanner {
    private static final SubTypesScanner SUB_TYPES_SCANNER = new SubTypesScanner(false);
    private static final TypeAnnotationsScanner TYPE_ANNOTATIONS_SCANNER = new TypeAnnotationsScanner();

    private final Reflections reflections;

    public ControllerScanner(String basePackage) {
        this.reflections = new Reflections(basePackage, SUB_TYPES_SCANNER, TYPE_ANNOTATIONS_SCANNER);
    }

    public Set<Class<?>> scan() {
        return reflections.getTypesAnnotatedWith(Controller.class);
    }
}
