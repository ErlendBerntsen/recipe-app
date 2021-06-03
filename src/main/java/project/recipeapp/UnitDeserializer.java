package project.recipeapp;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import project.recipeapp.units.Unit;

import java.io.IOException;

public class UnitDeserializer extends StdDeserializer<Unit> {

    UnitRepository unitRepository;
    public UnitDeserializer() {
        this(null);
    }

    public UnitDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Unit deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        String name = node.get("name").asText();
        return unitRepository.findByName(name).get();
    }
}