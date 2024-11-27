package semantic;

import android.content.Context;
import android.content.res.AssetFileDescriptor;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class LiteRTModelLoader {

    private final Interpreter interpreter;

    // Constructor to initialize the Interpreter with the model
    public LiteRTModelLoader(Context context, String modelPath) throws IOException {
        MappedByteBuffer modelBuffer = loadModelFile(context, modelPath);
        interpreter = new Interpreter(modelBuffer);
    }

    // Load model file from assets folder
    private MappedByteBuffer loadModelFile(Context context, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    // Method to run inference
    public void runInference(Object input, Object output) {
        interpreter.run(input, output);
    }

    // Close the interpreter when done
    public void close() {
        interpreter.close();
    }
}