/**
 *
 */
package com.ocs.indaba.util;

import org.apache.log4j.Logger;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author TianGuang
 */
public class GenericTypeUtils {
    private static final Logger logger = Logger.getLogger(GenericTypeUtils.class);

    private GenericTypeUtils() {
    }

    /**
     * ͨ����,��ö���Classʱ����ĸ���ķ��Ͳ��������. ��public BookManager extends GenricManager<Book>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or <code>Object.class</code> if cannot be determined
     */
    @SuppressWarnings("unchecked")
    public static Class getSuperClassGenricType(Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * ͨ����,��ö���Classʱ����ĸ���ķ��Ͳ��������. ��public BookManager extends GenricManager<Book>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or <code>Object.class</code> if cannot be determined
     */
    @SuppressWarnings("unchecked")
    public static Class getSuperClassGenricType(Class clazz, int index) {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
                    + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }
        return (Class) params[index];
    }
}
