package com.arunpn.imagesearch.rest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;

import retrofit.converter.ConversionException;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

/**
 * Created by a1nagar on 10/18/15.
 */

public class DefaultGSONConverter extends GsonConverter {

    public static final String JSON_ENVELOPE_NAME = "responseData";

    public DefaultGSONConverter(Gson gson) {
        super(gson);
    }

    @Override
    public Object fromBody(TypedInput body, Type type) throws ConversionException {
        try {
            Reader reader = new InputStreamReader(body.in());
            JsonElement element = new JsonParser().parse(reader);
            String rawJson = element.getAsJsonObject().get(JSON_ENVELOPE_NAME).toString();

            return super.fromBody(new TypedByteArray(body.mimeType(), rawJson.getBytes("UTF8")), type);
        } catch (IOException e) {
            return super.fromBody(body, type);
        }

    }
}