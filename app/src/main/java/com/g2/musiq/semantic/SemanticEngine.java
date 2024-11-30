package com.g2.musiq.semantic;

import android.content.Context;

import java.io.IOException;

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
