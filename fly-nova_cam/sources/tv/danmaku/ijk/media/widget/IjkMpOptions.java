package tv.danmaku.ijk.media.widget;

import java.util.HashMap;
import java.util.Map;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/* loaded from: classes.dex */
public class IjkMpOptions {
    public static final int MJPEG_PIX_FMT_YUVJ420P = 0;
    public static final int MJPEG_PIX_FMT_YUVJ422P = 1;
    public static final int MJPEG_PIX_FMT_YUVJ444P = 2;
    public static final int PREFERRED_IMAGE_TYPE_JPEG = 0;
    public static final int PREFERRED_IMAGE_TYPE_PNG = 1;
    public static final int PREFERRED_VIDEO_TYPE_H264 = 2;
    public static final int PREFERRED_VIDEO_TYPE_MJPEG = 0;
    public static final int PREFERRED_VIDEO_TYPE_MPEG4 = 1;
    public static final int RTP_JPEG_PARSE_PACKET_METHOD_DROP = 1;
    public static final int RTP_JPEG_PARSE_PACKET_METHOD_FILL = 2;
    public static final int RTP_JPEG_PARSE_PACKET_METHOD_ORIGIN = 0;
    public static final int X264_PRESET_FAST = 4;
    public static final int X264_PRESET_FASTER = 3;
    public static final int X264_PRESET_MEDIUM = 5;
    public static final int X264_PRESET_PLACEBO = 9;
    public static final int X264_PRESET_SLOW = 6;
    public static final int X264_PRESET_SLOWER = 7;
    public static final int X264_PRESET_SUPERFAST = 1;
    public static final int X264_PRESET_ULTRAFAST = 0;
    public static final int X264_PRESET_VERYFAST = 2;
    public static final int X264_PRESET_VERYSLOW = 8;
    public static final int X264_PROFILE_BASELINE = 0;
    public static final int X264_PROFILE_HIGH = 2;
    public static final int X264_PROFILE_HIGH10 = 3;
    public static final int X264_PROFILE_HIGH422 = 4;
    public static final int X264_PROFILE_HIGH444 = 5;
    public static final int X264_PROFILE_MAIN = 1;
    public static final int X264_TUNE_ANIMATION = 1;
    public static final int X264_TUNE_FASTDECODE = 4;
    public static final int X264_TUNE_FILM = 0;
    public static final int X264_TUNE_GRAIN = 2;
    public static final int X264_TUNE_PSNR = 6;
    public static final int X264_TUNE_SSIM = 7;
    public static final int X264_TUNE_STILLIMAGE = 3;
    public static final int X264_TUNE_ZEROLATENCY = 5;
    private final HashMap<String, Object> codecOptions;
    private final HashMap<String, Object> formatOptions;
    private final HashMap<Integer, HashMap<String, Object>> optionCategories;
    private final HashMap<String, Object> playerOptions;
    private final HashMap<String, Object> swrOptions;
    private final HashMap<String, Object> swsOptions;

    public static IjkMpOptions defaultOptions() {
        IjkMpOptions ijkMpOptions = new IjkMpOptions();
        ijkMpOptions.setOption(4, "mediacodec", 1L);
        ijkMpOptions.setOption(4, "opensles", 0L);
        ijkMpOptions.setOption(4, "overlay-format", 842225234L);
        ijkMpOptions.setOption(4, "framedrop", 1L);
        ijkMpOptions.setOption(4, "start-on-prepared", 0L);
        ijkMpOptions.setOption(1, "initial_timeout", 500000L);
        ijkMpOptions.setOption(1, "stimeout", 500000L);
        ijkMpOptions.setOption(1, "http-detect-range-support", 0L);
        ijkMpOptions.setOption(2, "skip_loop_filter", 48L);
        ijkMpOptions.setOption(4, "rtp-jpeg-parse-packet-method", 0L);
        return ijkMpOptions;
    }

    public IjkMpOptions() {
        HashMap<String, Object> map = new HashMap<>();
        this.playerOptions = map;
        HashMap<String, Object> map2 = new HashMap<>();
        this.formatOptions = map2;
        HashMap<String, Object> map3 = new HashMap<>();
        this.codecOptions = map3;
        HashMap<String, Object> map4 = new HashMap<>();
        this.swsOptions = map4;
        HashMap<String, Object> map5 = new HashMap<>();
        this.swrOptions = map5;
        HashMap<Integer, HashMap<String, Object>> map6 = new HashMap<>();
        this.optionCategories = map6;
        map6.put(4, map);
        map6.put(1, map2);
        map6.put(2, map3);
        map6.put(3, map4);
        map6.put(5, map5);
    }

    public void applyToMediaPlayer(IjkMediaPlayer ijkMediaPlayer) {
        for (Map.Entry<Integer, HashMap<String, Object>> entry : this.optionCategories.entrySet()) {
            int iIntValue = entry.getKey().intValue();
            for (Map.Entry<String, Object> entry2 : entry.getValue().entrySet()) {
                String key = entry2.getKey();
                Object value = entry2.getValue();
                if (value instanceof Long) {
                    ijkMediaPlayer.setOption(iIntValue, key, ((Long) value).longValue());
                } else if (value instanceof String) {
                    ijkMediaPlayer.setOption(iIntValue, key, (String) value);
                }
            }
        }
    }

    public void setOption(int i, String str, String str2) {
        HashMap<String, Object> map;
        if (str == null || (map = this.optionCategories.get(Integer.valueOf(i))) == null) {
            return;
        }
        map.put(str, str2);
    }

    public void setOption(int i, String str, long j) {
        HashMap<String, Object> map;
        if (str == null || (map = this.optionCategories.get(Integer.valueOf(i))) == null) {
            return;
        }
        map.put(str, Long.valueOf(j));
    }

    public void setFormatOption(String str, String str2) {
        setOption(1, str, str2);
    }

    public void setCodecOption(String str, String str2) {
        setOption(2, str, str2);
    }

    public void setSwsOption(String str, String str2) {
        setOption(3, str, str2);
    }

    public void setPlayerOption(String str, String str2) {
        setOption(4, str, str2);
    }

    public void setSwrOption(String str, String str2) {
        setOption(5, str, str2);
    }

    public void setFormatOption(String str, long j) {
        setOption(1, str, j);
    }

    public void setCodecOption(String str, long j) {
        setOption(2, str, j);
    }

    public void setSwsOption(String str, long j) {
        setOption(3, str, j);
    }

    public void setPlayerOption(String str, long j) {
        setOption(4, str, j);
    }

    public void setSwrOption(String str, long j) {
        setOption(5, str, j);
    }
}
