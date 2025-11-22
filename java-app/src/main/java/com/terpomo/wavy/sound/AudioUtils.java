package com.terpomo.wavy.sound;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import java.util.Arrays;

public class AudioUtils {

    public static Mixer.Info getMixerInfoByName(String name) {
        return Arrays.stream(AudioSystem.getMixerInfo()).filter(info -> info.getName().equals(name)).findFirst().orElse(null);
    }

    public static <T extends DataLine> String[] getMixerInfos(Class<T> clazz, AudioFormat audioFormat) {
        return Arrays.stream(AudioSystem.getMixerInfo())
                .filter(info -> AudioSystem.getMixer(info).isLineSupported(new DataLine.Info(clazz, audioFormat)))
                .map(Mixer.Info::getName)
                .toArray(String[]::new);
    }

}
