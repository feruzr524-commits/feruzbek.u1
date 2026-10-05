package com.example.util

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

class PrankSoundAndHaptics(private val context: Context) {

    private val audioScope = CoroutineScope(Dispatchers.Default)

    private fun getVibrator(): Vibrator? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                manager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
        } catch (_: Exception) {
            null
        }
    }

    fun playTacticalClick(enabled: Boolean = true) {
        vibrateOneShot(35L, 120)
        if (!enabled) return
        audioScope.launch {
            playToneSequence(
                notes = listOf(880.0 to 45, 1320.0 to 55),
                volume = 0.25f
            )
        }
    }

    fun playUcDropFanfare(enabled: Boolean = true) {
        vibrateWaveform(longArrayOf(0, 70, 50, 120), intArrayOf(0, 180, 0, 255))
        if (!enabled) return
        audioScope.launch {
            // Heroic golden supply crate drop arpeggio: C5 -> E5 -> G5 -> C6
            playToneSequence(
                notes = listOf(
                    523.25 to 90,
                    659.25 to 90,
                    783.99 to 100,
                    1046.50 to 260
                ),
                volume = 0.35f
            )
        }
    }

    fun playPrankLaughHorn(enabled: Boolean = true) {
        vibrateWaveform(longArrayOf(0, 90, 60, 90, 60, 220), intArrayOf(0, 220, 0, 220, 0, 255))
        if (!enabled) return
        audioScope.launch {
            // Classic comical "Wah-Wah-Wah-Waaah" descending prank horn
            playToneSequence(
                notes = listOf(
                    392.00 to 180, // G4
                    369.99 to 180, // F#4
                    349.23 to 180, // F4
                    329.63 to 420  // E4 with vibrato
                ),
                volume = 0.4f,
                addVibratoOnLast = true
            )
        }
    }

    private fun vibrateOneShot(durationMs: Long, amplitude: Int) {
        try {
            val vibrator = getVibrator() ?: return
            if (!vibrator.hasVibrator()) return
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(durationMs, amplitude))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(durationMs)
            }
        } catch (_: Exception) {
        }
    }

    private fun vibrateWaveform(timings: LongArray, amplitudes: IntArray) {
        try {
            val vibrator = getVibrator() ?: return
            if (!vibrator.hasVibrator()) return
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(200L)
            }
        } catch (_: Exception) {
        }
    }

    private fun playToneSequence(
        notes: List<Pair<Double, Int>>,
        volume: Float,
        addVibratoOnLast: Boolean = false
    ) {
        try {
            val sampleRate = 22050
            val totalDurationMs = notes.sumOf { it.second + 20 }
            val totalSamples = (sampleRate * totalDurationMs) / 1000
            val buffer = ShortArray(totalSamples)

            var currentSampleIndex = 0
            notes.forEachIndexed { index, (freq, durationMs) ->
                val noteSamples = (sampleRate * durationMs) / 1000
                val gapSamples = (sampleRate * 20) / 1000
                val isLast = index == notes.lastIndex

                for (i in 0 until noteSamples) {
                    if (currentSampleIndex >= totalSamples) break
                    val t = i.toDouble() / sampleRate
                    val envelope = when {
                        i < noteSamples * 0.1 -> i / (noteSamples * 0.1)
                        i > noteSamples * 0.8 -> (noteSamples - i) / (noteSamples * 0.2)
                        else -> 1.0
                    }
                    val effectiveFreq = if (isLast && addVibratoOnLast) {
                        freq * (1.0 - 0.04 * (i.toDouble() / noteSamples) + 0.015 * sin(2.0 * PI * 7.0 * t))
                    } else {
                        freq
                    }
                    // Mix fundamental + warm harmonic for a gaming synth brass/horn tone
                    val wave = 0.7 * sin(2.0 * PI * effectiveFreq * t) +
                        0.3 * sin(4.0 * PI * effectiveFreq * t)
                    val sampleValue = (wave * envelope * volume * Short.MAX_VALUE).toInt()
                        .coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                    buffer[currentSampleIndex++] = sampleValue.toShort()
                }
                currentSampleIndex = (currentSampleIndex + gapSamples).coerceAtMost(totalSamples)
            }

            val minBufSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            ).coerceAtLeast(buffer.size * 2)

            val track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(minBufSize)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            track.write(buffer, 0, buffer.size)
            track.play()
            Thread.sleep(totalDurationMs.toLong() + 80L)
            track.stop()
            track.release()
        } catch (_: Exception) {
        }
    }
}
