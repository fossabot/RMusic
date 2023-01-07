package cn.rtast.rmusic.utils

import java.io.ByteArrayInputStream
import java.io.File
import java.net.URL
import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem

class MusicConverterUtil {

    fun convert(url: String, name: String, br: Float): String {
        var sBr = br
        if (br > 320000) {
            sBr /= 10
        }
        val dir = File("./rmusic/cache/")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val sourceBytes = URL(url).readBytes()
        ByteArrayInputStream(sourceBytes).use { basis ->
            AudioSystem.getAudioInputStream(basis).use { sourceAIS ->
                val sourceFormat: AudioFormat = sourceAIS.format
                val mp3tFormat = AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    sourceFormat.sampleRate,
                    16,
                    sourceFormat.channels,
                    sourceFormat.channels * 2,
                    sourceFormat.sampleRate,
                    false
                )
                val pcmFormat = AudioFormat(sBr, 32, 1, true, false)
                AudioSystem.getAudioInputStream(mp3tFormat, sourceAIS).use { mp3AIS ->
                    AudioSystem.getAudioInputStream(pcmFormat, mp3AIS).use { pcmAIS ->
                        AudioSystem.write(pcmAIS, AudioFileFormat.Type.WAVE, File("./rmusic/cache/$name"))
                    }
                }
                return name
            }
        }
    }
}
