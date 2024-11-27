package semantic;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

public class SemanticEngine {

    public SemanticEngine(Context context) {
        try {
            LiteRTModelLoader loader = new LiteRTModelLoader(context, "models/openl3_model.tflite");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
