package nars.util.language;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.IOException;
import java.util.Map;

/**
 * JSON utilities
 */
public class JSON {

    static final ObjectMapper om = new ObjectMapper()

            .enableDefaultTyping()

            .configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false)

            .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.PROTECTED_AND_PUBLIC)
            .registerModule(new SimpleModule().addSerializer(StackTraceElement.class, new ToStringSerializer()));
    static final ObjectWriter omPretty = om.copy().writerWithDefaultPrettyPrinter();

    public static final ObjectMapper omDeep = new ObjectMapper()
            .enableDefaultTyping()
            .enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION)
            .enable(SerializationFeature.USE_EQUALITY_FOR_OBJECT_ID)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .disable(SerializationFeature.WRITE_NULL_MAP_VALUES)
            .disable(MapperFeature.USE_STATIC_TYPING)
            .registerModule(new SimpleModule().addSerializer(StackTraceElement.class, new ToStringSerializer()));

//
    public static JsonNode toJSON(String json) throws IOException {
        return om.readTree(json);
    }

    public static Map<String,Object> toMap(String json) {
        try {
            return om.readValue(json, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
//
//    public static Object toObj(String json) {
//        try {
//            return def.anyFrom(json);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
    public static String stringFrom(Object obj) {
        try {
            return om.writeValueAsString(obj);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String pretty(JsonNode j) {
        if (j == null)
            return "['null']";

        try {
            return omPretty.writeValueAsString(j);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

    }
//    public static String stringFromUnreflected(Object obj) {
//        try {
//            return unRef.asString(obj);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//
//    public static Map<String,Object> mapFrom(Object obj) {
//        try {
//            return def.mapFrom(obj);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }


}
