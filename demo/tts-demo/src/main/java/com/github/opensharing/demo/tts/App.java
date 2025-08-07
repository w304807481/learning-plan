package com.github.opensharing.demo.tts;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.alibaba.dashscope.audio.tts.SpeechSynthesisResult;
import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesisAudioFormat;
import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesisParam;
import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesizer;
import com.alibaba.dashscope.common.ResultCallback;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;

class TimeUtils {
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static String getTimestamp() {
        return LocalDateTime.now().format(formatter);
    }
}

public class App {
    private static final String model = "cosyvoice-v1";
    private static final String voice = "longxiaochun_v1";
    private static final String[] textArray = {"虽然目前分期办理后，",
            "当下可用额度会稍微被占用，", "但您办了分期以后，", "我们会把这个数据上传反馈，",
            "也是加强了和银行的一个合作嘛。", "也多了一个数据可以正向参考，",
            "对于后续您办理业务往来呀，","也会有一些帮助。"};

    public static void process() throws NoApiKeyException, InputRequiredException {
        // Playback thread
        class PlaybackRunnable implements Runnable {
            // Set the audio format. Please configure according to your actual device,
            // synthesized audio parameters, and platform choice Here it is set to
            // 22050Hz16bit single channel. It is recommended that customers choose other
            // sample rates and formats based on the model sample rate and device
            // compatibility.
            private final AudioFormat af = new AudioFormat(22050, 16, 1, true, false);
            private final DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
            private SourceDataLine targetSource = null;
            private final AtomicBoolean runFlag = new AtomicBoolean(true);
            private final ConcurrentLinkedQueue<ByteBuffer> queue =
                    new ConcurrentLinkedQueue<>();

            // Prepare the player
            public void prepare() throws LineUnavailableException {
                targetSource = (SourceDataLine) AudioSystem.getLine(info);
                targetSource.open(af, 4096);
                targetSource.start();
            }

            public void put(ByteBuffer buffer) {
                queue.add(buffer);
            }

            // Stop playback
            public void stop() {
                runFlag.set(false);
            }

            @Override
            public void run() {
                if (targetSource == null) {
                    return;
                }

                while (runFlag.get()) {
                    if (queue.isEmpty()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                        }
                        continue;
                    }

                    ByteBuffer buffer = queue.poll();
                    if (buffer == null) {
                        continue;
                    }

                    byte[] data = buffer.array();
                    targetSource.write(data, 0, data.length);
                }

                // Play all remaining cache
                if (!queue.isEmpty()) {
                    ByteBuffer buffer = null;
                    while ((buffer = queue.poll()) != null) {
                        byte[] data = buffer.array();
                        targetSource.write(data, 0, data.length);
                    }
                }
                // Release the player
                targetSource.drain();
                targetSource.stop();
                targetSource.close();
            }
        }

        // Create a subclass inheriting from ResultCallback<SpeechSynthesisResult>
        // to implement the callback interface
        class ReactCallback extends ResultCallback<SpeechSynthesisResult> {
            private PlaybackRunnable playbackRunnable = null;

            public ReactCallback(PlaybackRunnable playbackRunnable) {
                this.playbackRunnable = playbackRunnable;
            }

            // Callback when the service side returns the streaming synthesis result
            @Override
            public void onEvent(SpeechSynthesisResult result) {
                // Get the binary data of the streaming result via getAudio
                if (result.getAudioFrame() != null) {
                    System.out.println(TimeUtils.getTimestamp() + " 收到音频");
                    // Stream the data to the player
                    playbackRunnable.put(result.getAudioFrame());
                }
            }

            // Callback when the service side completes the synthesis
            @Override
            public void onComplete() {
                System.out.println(TimeUtils.getTimestamp() + " 收到Complete，语音合成结束");
                // Notify the playback thread to end
                playbackRunnable.stop();
            }

            // Callback when an error occurs
            @Override
            public void onError(Exception e) {
                // Tell the playback thread to end
                System.out.println(e);
                playbackRunnable.stop();
            }
        }

        PlaybackRunnable playbackRunnable = new PlaybackRunnable();
        try {
            playbackRunnable.prepare();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        Thread playbackThread = new Thread(playbackRunnable);
        // Start the playback thread
        playbackThread.start();
        /*******  Call the Generative AI Model to get streaming text *******/
        /**
        // Prepare for the LLM call
        Generation gen = new Generation();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content("虽然目前分期办理后，当下可用额度会稍微被占用，但您办了分期以后，我们会把这个数据上传反馈，也是加强了和银行的一个合作嘛。也多了一个数据可以正向参考，对于后续您办理业务往来呀，也会有一些帮助。")
                .build();
        GenerationParam genParam =
                GenerationParam.builder()
                        // 若没有将API Key配置到环境变量中，需将下面这行代码注释放开，并将apiKey替换为自己的API Key
                        .apiKey("sk-9a4ac9d342b3433c93b4f34e8ea10bfe")
                        .model("qwen-turbo")
                        .messages(Arrays.asList(userMsg))
                        .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                        .topP(0.8)
                        .incrementalOutput(true)
                        .build();
         **/
        // Prepare the speech synthesis task
        SpeechSynthesisParam param =
                SpeechSynthesisParam.builder()
                        // 若没有将API Key配置到环境变量中，需将下面这行代码注释放开，并将apiKey替换为自己的API Key
                        .apiKey("sk-XXXX")
                        .model(model)
                        .voice(voice)
                        .format(SpeechSynthesisAudioFormat
                                .PCM_22050HZ_MONO_16BIT)
                        .build();
        SpeechSynthesizer synthesizer =
                new SpeechSynthesizer(param, new ReactCallback(playbackRunnable));
//        Flowable<GenerationResult> result = gen.streamCall(genParam);
//        result.blockingForEach(message -> {
//            String text =
//                    message.getOutput().getChoices().get(0).getMessage().getContent();
//            System.out.println("LLM output：" + text);
//            synthesizer.streamingCall(text);
//        });

        //流式调用
        for (String text : textArray) {
            // 发送文本片段，在回调接口的onEvent方法中实时获取二进制音频
            synthesizer.streamingCall(text);
        }

        synthesizer.streamingComplete();

        System.out.println(
                "[Metric] requestId为："
                        + synthesizer.getLastRequestId()
                        + "，首包延迟（毫秒）为："
                        + synthesizer.getFirstPackageDelay());

        try {
            // Wait for the playback thread to finish playing all
            playbackThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws NoApiKeyException, InputRequiredException {
        process();
        System.exit(0);
    }
}
