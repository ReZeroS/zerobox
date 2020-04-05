package club.qqtim.util;

import club.qqtim.factory.ProcessException;
import org.dom4j.Attribute;
import org.dom4j.Element;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 1726542850@qq.com
 * rezeros.github.io
 * description:
 */
public final class Xml2ObjectParseUtil {

    private Xml2ObjectParseUtil(){}


    public static <D> D parse(Element element, Class<D> clazz) {
        D result = null;
        try {
            result = populateObject(element, clazz);
        } catch (IllegalAccessException e) {

        }
        return result;
    }

    private static <D> D populateObject(Element element, Class<D> clazz) throws IllegalAccessException {
        List<Attribute> attributes = (List<Attribute>) element.attributes();
        Map<String, String> attributeMap = attributes.stream().map(Attribute.class::cast)
                .collect(Collectors.toMap(Attribute::getName, Attribute::getValue, (o, n) -> n));

        try {
            D result = clazz.newInstance();
            Field[] fields = result.getClass().getDeclaredFields();

            for (Field field : fields) {
                // reflection utils force set the field value
                ReflectionUtils.makeAccessible(field);
                boolean existAttribute = attributeMap.containsKey(field.getName());
                if (existAttribute) {
                    field.set(result, attributeMap.get(field.getName()));
                } else {
                    // child element search the field
                    //
                    // issue: how to get the generic type of the list like List<Demo> => Demo.class
                    // tips: java will erase the real type when
                    // handle: this is a 'try step' which supposed to be parse correctly

                    Class listType = getListType(field);
                    List list = new ArrayList();
                    Iterator iterator = element.elementIterator();
                    while (iterator.hasNext()) {
                        Element childElement = (Element) iterator.next();
                        if (field.getName().equals(childElement.getName())) {
                            list.add(populateObject(childElement, listType));
                        }
                    }
                    field.set(result, list);
                }
            }
            return result;
        } catch (InstantiationException e) {

        }
        return null;
    }

    private static Class getListType(Field field) {
        if (Collection.class.isAssignableFrom(field.getType())) {
            // Get Generic type of your field
            return getFieldGenericType(field);
        }
        throw new ProcessException("Not collection class for parsing");
    }

    //Returns generic type of any field
    public static Class getFieldGenericType(Field field) {
        if (ParameterizedType.class.isAssignableFrom(field.getGenericType().getClass())) {
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            return ((Class) (genericType.getActualTypeArguments()[0]));
        }
        //Returns dummy Boolean Class to compare with ValueObject & FormBean
        return new Boolean(false).getClass();
    }


}
