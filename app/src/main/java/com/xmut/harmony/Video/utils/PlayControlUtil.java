/**
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xmut.harmony.Video.utils;

import com.huawei.hms.videokit.player.InitBufferTimeStrategy;
import com.huawei.hms.videokit.player.Preloader;
import com.huawei.hms.videokit.player.bean.Proxy;
import com.huawei.hms.videokit.player.common.PlayerConstants.PlayMode;

import java.util.HashMap;
import java.util.Map;

/**
 * Play control tools
 */
public class PlayControlUtil {
    /**
     * Local loads of players if the View is SurfaceView
     */
    private static boolean isSurfaceView = true;

    /**
     * Set play type 0: on demand (the default) 1: live
     */
    private static int videoType = 0;

    /**
     * Set the mute
     * Default is mute
     */
    private static boolean isMute = false;

    /**
     * Set the play mode
     * Default is video play
     */
    private static int playMode = PlayMode.PLAY_MODE_NORMAL;

    /**
     * Set the bandwidth switching mode
     * The default is adaptive
     */
    private static int bandwidthSwitchMode = 0;

    /**
     * Whether to set up the bitrate
     */
    private static boolean initBitrateEnable = false;

    /**
     * The Bitrate type
     * 0：The default priority search upwards
     * 1：The priority search down
     */
    private static int initType = 0;

    /**
     * Bitrate (if set up by resolution rate setting is effective)
     */
    private static int initBitrate = 0;

    /**
     * Resolution width (width height must be set up in pairs)
     */
    private static int initWidth = 0;

    /**
     * Resolution height (width height must be set up in pairs)
     */
    private static int initHeight = 0;

    /**
     * Whether close the logo
     */
    private static boolean closeLogo = false;

    /**
     * Close the logo, whether to affect all sources
     * True: affects the whole play false: only under the influence of the sources of logo
     */
    private static boolean takeEffectOfAll = false;

    /**
     * Save the data
     */
    private static Map<String, Integer> savePlayDataMap = new HashMap<>();

    /**
     * The minimum bitrate
     */
    private static int minBitrate;

    /**
     * The maximum rate
     */
    private static int maxBitrate;

    private static boolean isLoadBuff = true;

    /**
     * 预加载初始化结果
     */
    private static int initResult = -1;

    /**
     * Subtitle preset language
     */
    private static String subtitlePresetLanguage = "";

    /**
     * Play back init bufferTime
     */
    private static InitBufferTimeStrategy initBufferTimeStrategy;

    /**
     * audio preset language
     */
    private static String preferAudio = null;

    private static Preloader preloader;

    /**
     * 是否使用单线程下载
     */
    private static boolean isDownloadLinkSingle = false;

    private static Proxy socksProxy = null;

    private static boolean isWakeOn = true;

    private static boolean isSubtitleRenderByDemo = false;

    public static boolean isSurfaceView() {
        return isSurfaceView;
    }

    public static void setIsSurfaceView(boolean isSurfaceView) {
        com.xmut.harmony.Video.utils.PlayControlUtil.isSurfaceView = isSurfaceView;
    }

    public static int getVideoType() {
        return videoType;
    }

    public static void setVideoType(int videoType) {
        com.xmut.harmony.Video.utils.PlayControlUtil.videoType = videoType;
    }

    public static boolean isMute() {
        return isMute;
    }

    public static void setIsMute(boolean isMute) {
        com.xmut.harmony.Video.utils.PlayControlUtil.isMute = isMute;
    }

    public static int getPlayMode() {
        return playMode;
    }

    public static void setPlayMode(int playMode) {
        com.xmut.harmony.Video.utils.PlayControlUtil.playMode = playMode;
    }

    public static int getBandwidthSwitchMode() {
        return bandwidthSwitchMode;
    }

    public static void setBandwidthSwitchMode(int bandwidthSwitchMode) {
        com.xmut.harmony.Video.utils.PlayControlUtil.bandwidthSwitchMode = bandwidthSwitchMode;
    }

    public static boolean isInitBitrateEnable() {
        return initBitrateEnable;
    }

    public static void setInitBitrateEnable(boolean initBitrateEnable) {
        com.xmut.harmony.Video.utils.PlayControlUtil.initBitrateEnable = initBitrateEnable;
    }

    public static int getInitType() {
        return initType;
    }

    public static void setInitType(int initType) {
        com.xmut.harmony.Video.utils.PlayControlUtil.initType = initType;
    }

    public static int getInitBitrate() {
        return initBitrate;
    }

    public static void setInitBitrate(int initBitrate) {
        com.xmut.harmony.Video.utils.PlayControlUtil.initBitrate = initBitrate;
    }

    public static int getInitWidth() {
        return initWidth;
    }

    public static void setInitWidth(int initWidth) {
        com.xmut.harmony.Video.utils.PlayControlUtil.initWidth = initWidth;
    }

    public static int getInitHeight() {
        return initHeight;
    }

    public static void setInitHeight(int initHeight) {
        com.xmut.harmony.Video.utils.PlayControlUtil.initHeight = initHeight;
    }

    public static boolean isTakeEffectOfAll() {
        return takeEffectOfAll;
    }

    public static void setTakeEffectOfAll(boolean takeEffectOfAll) {
        com.xmut.harmony.Video.utils.PlayControlUtil.takeEffectOfAll = takeEffectOfAll;
    }

    public static void savePlayData(String url, int progress) {
        if (!StringUtil.isEmpty(url)) {
            LogUtil.d("current play url :" + url + ", and current progress is " + progress);
            savePlayDataMap.put(url, progress);
        }
    }

    public static int getPlayData(String url) {
        if (savePlayDataMap.get(url) == null) {
            return 0;
        }
        return savePlayDataMap.get(url);
    }

    public static void clearPlayData(String url) {
        if (StringUtil.isEmpty(url)) {
            LogUtil.d("clear play url :" + url);
            savePlayDataMap.remove(url);
        }
    }

    public static boolean isCloseLogo() {
        return closeLogo;
    }

    public static void setCloseLogo(boolean closeLogo) {
        com.xmut.harmony.Video.utils.PlayControlUtil.closeLogo = closeLogo;
    }

    public static int getMinBitrate() {
        return minBitrate;
    }

    public static void setMinBitrate(int minBitrate) {
        com.xmut.harmony.Video.utils.PlayControlUtil.minBitrate = minBitrate;
    }

    public static int getMaxBitrate() {
        return maxBitrate;
    }

    public static void setMaxBitrate(int maxBitrate) {
        com.xmut.harmony.Video.utils.PlayControlUtil.maxBitrate = maxBitrate;
    }

    /**
     * Whether need to modify the code bitrate range
     */
    public static boolean isSetBitrateRangeEnable() {
        return maxBitrate != 0 || minBitrate != 0;
    }

    /**
     * Remove bitrate range data
     */
    public static void clearBitrateRange() {
        maxBitrate = 0;
        minBitrate = 0;
    }

    public static boolean isLoadBuff() {
        return isLoadBuff;
    }

    public static void setLoadBuff(boolean isLoadBuff) {
        com.xmut.harmony.Video.utils.PlayControlUtil.isLoadBuff = isLoadBuff;
    }

    public static int getInitResult() {
        return initResult;
    }

    public static void setInitResult(int initResult) {
        com.xmut.harmony.Video.utils.PlayControlUtil.initResult = initResult;
    }

    public static Preloader getPreloader() {
        return preloader;
    }

    public static void setPreloader(Preloader preloader) {
        com.xmut.harmony.Video.utils.PlayControlUtil.preloader = preloader;
    }

    /**
     * Set subtitle preset language
     *
     * @param language subtitle preset language
     */
    public static void setSubtitlePresetLanguage(String language) {
        com.xmut.harmony.Video.utils.PlayControlUtil.subtitlePresetLanguage = language;
    }

    /**
     * Get subtitle preset language
     *
     * @return subtitle preset language
     */
    public static String getSubtitlePresetLanguage() {
        return subtitlePresetLanguage;
    }

    /**
     * If subtitle preset language is set
     *
     * @return true：set; false：not set
     */
    public static boolean isSubtitlePresetLanguageEnable() {
        return !subtitlePresetLanguage.isEmpty();
    }

    /**
     * Get initBufferTimeStrategy
     *
     * @return InitBufferTimeStrategy
     */
    public static InitBufferTimeStrategy getInitBufferTimeStrategy() {
        return initBufferTimeStrategy;
    }

    /**
     * Set initBufferTimeStrategy
     *
     * @param initBufferTimeStrategy setting param
     */
    public static void setInitBufferTimeStrategy(InitBufferTimeStrategy initBufferTimeStrategy) {
        com.xmut.harmony.Video.utils.PlayControlUtil.initBufferTimeStrategy = initBufferTimeStrategy;
    }

    /**
     * Clear subtitle preset language
     */
    public static void clearSubtitlePresetLanguage() {
        subtitlePresetLanguage = "";
    }

    /**
     * Set audio preset language
     *
     * @param preferaudio audio preset language
     */
    public static void setPreferAudio(String preferaudio) {
        com.xmut.harmony.Video.utils.PlayControlUtil.preferAudio = preferaudio;
    }

    /**
     * Get audio preset language
     *
     * @return audio preset language
     */
    public static String getPreferAudio() {
        return preferAudio;
    }

    public static void setProxyInfo(Proxy proxy) {
        com.xmut.harmony.Video.utils.PlayControlUtil.socksProxy = proxy;
    }

    public static Proxy getProxyInfo() {
        return socksProxy;
    }

    /**
     * 设置是否使用单线程下载
     *
     * @param isDownloadLinkSingle 是否为单线程
     */
    public static void setIsDownloadLinkSingle(boolean isDownloadLinkSingle) {
        com.xmut.harmony.Video.utils.PlayControlUtil.isDownloadLinkSingle = isDownloadLinkSingle;
    }

    /**
     * 获取是否是单线程
     *
     * @return 是否为单线程
     */
    public static boolean isDownloadLinkSingle() {
        return isDownloadLinkSingle;
    }

    public static boolean isWakeOn() {
        return isWakeOn;
    }

    public static void setWakeOn(boolean wakeOn) {
        isWakeOn = wakeOn;
    }

    public static boolean isSubtitleRenderByDemo() {
        return isSubtitleRenderByDemo;
    }

    public static void setSubtitleRenderByDemo(boolean renderByDemo) {
        isSubtitleRenderByDemo = renderByDemo;
    }
}
