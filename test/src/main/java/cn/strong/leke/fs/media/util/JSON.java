package cn.strong.leke.fs.media.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.*;

public class JSON {

	private static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper(new JsonFactory());
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setTimeZone(TimeZone.getDefault());
	}

	/**
	 * 对象转JSON
	 * @param object
	 * @return
	 */
	public static String stringify(Object object) {
		if (object == null) {
			return null;
		}
		try {
			return mapper.writeValueAsString(object);
		} catch (Exception e) {
			throw new RuntimeException("Json serial error.", e);
		}
	}

	/**
	 * 对象转美化JSON
	 * @param object
	 * @return
	 */
	public static String pretty(Object object) {
		if (object == null) {
			return null;
		}
		try {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
		} catch (Exception e) {
			throw new RuntimeException("Json serial error.", e);
		}
	}

	/**
	 * JSON转对象
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static final <T> T parse(String json, Class<T> clazz) {
		return parse(json, getTypeFactory().constructType(clazz));
	}

	/**
	 * JSON转对象
	 * @param json
	 * @param valueType
	 * @return
	 */
	public static <T> T parse(String json, JavaType valueType) {
		if (json == null) {
			return null;
		}
		try {
			return mapper.readValue(json, valueType);
		} catch (Exception e) {
			throw new RuntimeException("Json deserial error.", e);
		}
	}

	/**
	 * JSON转对象数组
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static final <T> T[] parseArray(String json, Class<T> clazz) {
		return parse(json, getTypeFactory().constructArrayType(clazz));
	}

	/**
	 * JSON转对象列表
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static final <T> List<T> parseList(String json, Class<T> clazz) {
		return parse(json, getTypeFactory().constructCollectionType(ArrayList.class, clazz));
	}

	/**
	 * JSON转Map
	 * @param json
	 * @param keyClazz
	 * @param valueClazz
	 * @return
	 */
	public static final <K, V> Map<K, V> parseMap(String json, Class<K> keyClazz, Class<V> valueClazz) {
		return parse(json, getTypeFactory().constructMapType(HashMap.class, keyClazz, valueClazz));
	}

	public static final TypeFactory getTypeFactory() {
		return mapper.getTypeFactory();
	}
}
