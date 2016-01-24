package pdeConverter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sketch classes with this annotation will not be processed by {@link JavaToPdeConverter}.
 * 
 * @author rza
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface IgnoreSketch {

}
