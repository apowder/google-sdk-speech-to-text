package com.opensourceteams.module.google.speech.to.text.simple.model.phoneCall;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import org.apache.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * - 美国英语 (en-US) 或英国英语 (en-GB)
 * - 普通话 (中国大陆)	cmn-Hans-CN	中文、普通话（中国简体）
 */
public class RunEnglish {

    static Logger logger = Logger.getLogger(RunEnglish.class);
    public static void main(String[] args) throws Exception {
        logger.info("开始");
        //高级模型收费更高
        transcribeFileWithEnhancedModel("data/wav/english/commercial_mono.wav");
        logger.info("结束");

    }


    /**
     * Transcribe the given audio file using an enhanced model.
     *
     * @param fileName the path to an audio file.
     */
    public static void transcribeFileWithEnhancedModel(String fileName) throws Exception {
        Path path = Paths.get(fileName);
        byte[] content = Files.readAllBytes(path);

        try (SpeechClient speechClient = SpeechClient.create()) {
            // Get the contents of the local audio file
            RecognitionAudio recognitionAudio =
                    RecognitionAudio.newBuilder().setContent(ByteString.copyFrom(content)).build();

            // Configure request to enable enhanced models
            RecognitionConfig config =
                    RecognitionConfig.newBuilder()
                            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                            .setLanguageCode("en-US")
                            .setSampleRateHertz(8000)
                            .setUseEnhanced(true)
                            // A model must be specified to use enhanced model.
                            //.setModel("phone_call")
                            .setModel("default")
                            .build();

            // Perform the transcription request
            RecognizeResponse recognizeResponse = speechClient.recognize(config, recognitionAudio);

            // Print out the results
            for (SpeechRecognitionResult result : recognizeResponse.getResultsList()) {
                // There can be several alternative transcripts for a given chunk of speech. Just use the
                // first (most likely) one here.
                SpeechRecognitionAlternative alternative = result.getAlternatives(0);
                System.out.format("Transcript: %s\n\n", alternative.getTranscript());
            }
        }
    }
}
