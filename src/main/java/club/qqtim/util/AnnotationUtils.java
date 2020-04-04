package club.qqtim.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * @Author: ReZero
 * @Date: 4/8/19 5:47 PM
 * @Version 1.0
 */
public class AnnotationUtils {

    public static <T extends Annotation> T getAnnotation(AnnotatedElement annotatedElement, Class<T> annotationType) {
        T annotation = annotatedElement.getAnnotation(annotationType);
        if (annotation == null) {
            for (Annotation metaAnn : annotatedElement.getAnnotations()) {
                annotation = metaAnn.annotationType().getAnnotation(annotationType);
                if (annotation != null) {
                    break;
                }
            }
        }
        return annotation;
    }


}
