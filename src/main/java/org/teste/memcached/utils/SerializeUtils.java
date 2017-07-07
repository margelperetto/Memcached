package org.teste.memcached.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

public class SerializeUtils {

	public static <T> String toJSON(T obj) {
		try {
			ObjectMapper mapper = buildMapper();
			String json =  mapper.writeValueAsString(obj);
			String compressed = compressIfNecessary(json);
			return compressed;
		} catch (Exception e) {
			throw new RuntimeException("Erro ao converter para JSON: "+obj, e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromJSON(String json)  {
		try {
			json = decompressIfNecessary(json);
			ObjectMapper mapper = buildMapper();
			return (T) mapper.readValue(json, Object.class);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao converter para Objeto: "+json, e);
		}
	}

	private static ObjectMapper buildMapper() {
		ObjectMapper om = new ObjectMapper();
		om.disable(MapperFeature.AUTO_DETECT_CREATORS);
		om.disable(MapperFeature.AUTO_DETECT_GETTERS);
		om.disable(MapperFeature.AUTO_DETECT_SETTERS);
		om.disable(MapperFeature.AUTO_DETECT_IS_GETTERS);
		om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		om.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		om.enableDefaultTyping();
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

		SimpleModule module = new SimpleModule();
		module.addSerializer(ByteArrayInputStream.class, new ByteArraySerializer());
		module.addDeserializer(ByteArrayInputStream.class, new ByteArrayDeserializer());
		om.registerModule(module);

		return om;
	}

	private static class ByteArraySerializer extends StdScalarSerializer<ByteArrayInputStream>{
		private static final long serialVersionUID = 1L;

		protected ByteArraySerializer() {
			super(ByteArrayInputStream.class);
		}

		@Override
		public void serialize(ByteArrayInputStream value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
			gen.writeBinary(IOUtils.toByteArray(value));
		}

	}

	private static class ByteArrayDeserializer extends StdScalarDeserializer<ByteArrayInputStream>{
		private static final long serialVersionUID = 1L;

		protected ByteArrayDeserializer() {
			super(ByteArrayInputStream.class);
		}

		@Override
		public ByteArrayInputStream deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			byte[] buf = p.getBinaryValue();
			return new ByteArrayInputStream(buf);
		}
	}

	public static String compressIfNecessary(String str) throws Exception {
		if (str == null || str.length() <= 300 ) {
			return str;
		}

		ByteArrayOutputStream obj = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(obj);
		gzip.write(str.getBytes());
		gzip.close();
		obj.close();

		byte[] bytes = obj.toByteArray();
		byte[] encoded = Base64.encodeBase64(bytes);
		return new String(encoded);
	}

	public static String decompressIfNecessary(String str) throws Exception {
		if (str == null || str.length() == 0 || !Base64.isBase64(str)) {
			return str;
		}
		byte[] decoded = Base64.decodeBase64(str);
		
		if(!isCompressed(decoded)){
			return str;
		}

		GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(decoded));
		BufferedReader bf = new BufferedReader(new InputStreamReader(gis));
		String outStr = "";
		String line;
		while ((line=bf.readLine())!=null) {
			outStr += line;
		}
		gis.close();
		bf.close();

		return outStr;
	}

	public static boolean isCompressed(byte[] bytes) throws IOException {
		if (bytes == null || bytes.length < 2){
			return false;
		} else {
			return ((bytes[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) && (bytes[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8)));
		}
	}

}