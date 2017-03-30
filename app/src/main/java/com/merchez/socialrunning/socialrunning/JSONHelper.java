package com.merchez.socialrunning.socialrunning;

import android.util.Log;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by sebastien on 25/03/2017.
 */

public abstract class JSONHelper {
    public static JsonNode StringToJSON(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jfactory = new JsonFactory();
        JsonParser jParser = jfactory.createParser(json);
        return mapper.readTree(jParser);
    }
}
