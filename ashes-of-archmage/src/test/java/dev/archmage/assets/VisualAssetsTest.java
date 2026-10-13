package dev.archmage.assets;

import com.google.gson.JsonParser;
import javax.imageio.ImageIO;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VisualAssetsTest {
    private static final String ROOT="/assets/ashes_of_archmage/";
    private com.google.gson.JsonObject json(String path) throws Exception {
        try(var stream=getClass().getResourceAsStream(ROOT+path)) {
            assertNotNull(stream,path);
            return JsonParser.parseReader(new InputStreamReader(stream,StandardCharsets.UTF_8)).getAsJsonObject();
        }
    }
    @Test void texturesUseValidMinecraftDimensions() throws Exception {
        String[] paths={"textures/entity/archmage.png","textures/entity/archmage_outfit.png","textures/item/quas_orb.png"};
        int[] dims={64,256,32};
        for(int i=0;i<paths.length;i++)try(var stream=getClass().getResourceAsStream(ROOT+paths[i])) {
            assertNotNull(stream);var image=ImageIO.read(stream);assertNotNull(image);
            assertEquals(dims[i],image.getWidth());assertEquals(dims[i],image.getHeight());
        }
    }
    @Test void quasHasActualThreeDimensionalGeometryWithBoundedUv() throws Exception {
        var elements=json("models/item/quas_orb.json").getAsJsonArray("elements");
        assertTrue(elements.size()>100);
        for(var e:elements) {
            var o=e.getAsJsonObject();var from=o.getAsJsonArray("from");var to=o.getAsJsonArray("to");
            for(int axis=0;axis<3;axis++) {
                assertTrue(from.get(axis).getAsDouble()>=0&&to.get(axis).getAsDouble()<=16);
                assertTrue(to.get(axis).getAsDouble()>from.get(axis).getAsDouble());
            }
            assertEquals(15,o.get("light_emission").getAsInt());
            for(var face:o.getAsJsonObject("faces").entrySet())for(var uv:face.getValue().getAsJsonObject().getAsJsonArray("uv"))assertTrue(uv.getAsDouble()>=0&&uv.getAsDouble()<=16);
        }
    }
    @Test void quasItemReferencesOurMeshAndTexture() throws Exception {
        var def=json("items/quas_orb.json").getAsJsonObject("model");
        assertEquals("minecraft:model",def.get("type").getAsString());
        assertEquals("ashes_of_archmage:item/quas_orb",def.get("model").getAsString());
        assertEquals("ashes_of_archmage:item/quas_orb",json("models/item/quas_orb.json").getAsJsonObject("textures").get("ice").getAsString());
    }
}
