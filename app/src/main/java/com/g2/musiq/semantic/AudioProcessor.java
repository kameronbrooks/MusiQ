package com.g2.musiq.semantic;

import java.util.ArrayList;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.resample.Resampler;


public class AudioProcessor {
    public interface IAudioProcessorHandler {
        void handleAudioData(float[] data);
    }

    IAudioProcessorHandler _handler;
    ArrayList<Float> _buffer;

    public AudioProcessor(IAudioProcessorHandler handler) {
        _handler = handler;
    }

    protected void onDataProcessingComplete() {
        if(_handler != null) {
            float[] outputArray = new float[_buffer.size()];
            for(int i=0; i<_buffer.size(); i++) {
                outputArray[i] = _buffer.get(i);
             }
            _handler.handleAudioData(outputArray);
        }
    }
    protected void processData(float[] buffer) {
        for (float v : buffer) {
            _buffer.add(v);
        }
    }
    public void getAudioSampleFromFile(String path) {
        try {
            AudioDispatcher dispatcher = AudioDispatcherFactory.fromPipe(
                    path,
                    -1,
                    1024,
                    512
            );

            dispatcher.addAudioProcessor(new be.tarsos.dsp.AudioProcessor() {
                @Override
                public boolean process(AudioEvent audioEvent) {
                    float[] audioBuffer = audioEvent.getFloatBuffer();
                    processData(audioBuffer);
                    return true;
                }

                @Override
                public void processingFinished() {
                    onDataProcessingComplete();
                }
            });

            // Start the dispatcher to begin processing
            dispatcher.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
