package com.xmut.harmony.Fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.xmut.harmony.R;
import com.xmut.harmony.Video.activity.PlayActivity;
import com.xmut.harmony.Video.adapter.SelectPlayDataAdapter;
import com.xmut.harmony.Video.adapter.SelectPlayDataAdapter1;
import com.xmut.harmony.Video.contract.OnItemClickListener;
import com.xmut.harmony.Video.entity.PlayEntity;
import com.xmut.harmony.Video.utils.Constants;
import com.xmut.harmony.Video.utils.DataFormatUtil;
import com.xmut.harmony.util.httputil.DatabaseUtil;
import com.xmut.harmony.util.httputil.http.HttpAddress;

import java.util.ArrayList;
import java.util.List;


public class BrowserFragment extends Fragment implements  OnItemClickListener, View.OnClickListener {

    //服务端获取的视频对象josn
   public static String urljson ;

    private LinearLayout input_path_layout;
    private Context context;

    //视频播放列表
    private List<PlayEntity> playEntityList;

    private View view;

    // Play recyclerView
    private RecyclerView playRecyclerView;

    // Input play url
    private EditText addressEt;

    // Play button
    private Button playBt;

    // Load view
    private ProgressBar playLoading;

    // Play adapter
    private SelectPlayDataAdapter selectPlayDataAdapter;

    private SelectPlayDataAdapter1 selectPlayDataAdapter1;

    //abstract
//    private static WisePlayer.IRecommendVideoCallback recommendVideoCallback =
//            new WisePlayer.IRecommendVideoCallback() {
//                @Override
//                public void onSuccess(List<RecommendVideo> list) {
//                    LogUtil.i("query recommend video success.");
//                }
//
//                @Override
//                public void onFailed(int what, int extra, Object obj) {
//                    LogUtil.i("query recommend video fail, and error code is " + what);
//                }
//            };
//    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=getActivity();
        playEntityList = new ArrayList<>();
        //Used to get Videos from Cloud database in Control Class
        urljson= DatabaseUtil.selectList(HttpAddress.video(),"list").getResult();
        view = LayoutInflater.from(context).inflate(R.layout.activity_browser_fragment, null);

        initView();
        return view;
    }

    private void initView() {
        input_path_layout = view.findViewById(R.id.input_path_layout);
        input_path_layout.setVisibility(View.GONE);
        playRecyclerView = (RecyclerView) view.findViewById(R.id.player_recycler_view);
        playLoading = (ProgressBar) view.findViewById(R.id.play_loading);
        addressEt = (EditText) view.findViewById(R.id.input_path_ed);
        playBt = (Button) view.findViewById(R.id.main_play_btn);
        playBt.setOnClickListener(this);
//        selectPlayDataAdapter = new SelectPlayDataAdapter(context, this);

        selectPlayDataAdapter1 = new SelectPlayDataAdapter1(context, this);
        playRecyclerView.setLayoutManager(new GridLayoutManager(context,2));
        playRecyclerView.setAdapter(selectPlayDataAdapter1);
//        playRecyclerView.setLayoutManager(new LinearLayoutManager(context));
//        playRecyclerView.setAdapter(selectPlayDataAdapter);
        playRecyclerView.setVisibility(View.GONE);
        playLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //// https://blog.csdn.net/weixin_44380128/article/details/89183871 解决fragment重影
//        outState.putParcelable("android:support:fragments",null);

    }

    @Override
public void onStart() {
    super.onStart();
    updateView();
}
    //获取视频List 并载入Recycleview
    private void updateView() {
        playEntityList.clear();
        //playEntityList.addAll(DataFormatUtil.getPlayList(context));
        playEntityList.addAll(DataFormatUtil.getPlayList(context, urljson));
        updateRecyclerView(playEntityList);
    }


    public BrowserFragment() {
        super();

    }


    @Override
    public void onItemClick(int pos) {
        PlayEntity playEntity = getPlayFromPosition(pos);
        if (playEntity != null) {
            PlayActivity.startPlayActivity(context, playEntity);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_play_btn:
                String inputUrl = getInputUrl();
                if (TextUtils.isEmpty(inputUrl)) {
                    Toast.makeText(context, getResources().getString(R.string.input_path), Toast.LENGTH_SHORT).show();
                } else {
                    PlayActivity.startPlayActivity(context, getInputPlay(inputUrl));
                }
                break;
            default:
                break;
        }
    }

    public void updateRecyclerView(List<PlayEntity> playList) {
        selectPlayDataAdapter1.setSelectPlayList(playList);
//        selectPlayDataAdapter.setSelectPlayList(playList);
        playLoading.setVisibility(View.GONE);
        playRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Get input text
     *
     * @return Text value
     */
    public String getInputUrl() {
        if (addressEt.getText() == null) {
            return "";
        } else {
            return addressEt.getText().toString();
        }
    }

    public PlayEntity getInputPlay(String inputUrl) {
        PlayEntity playEntity = new PlayEntity();
        playEntity.setUrl(inputUrl);
        playEntity.setUrlType(Constants.UrlType.URL);
        return playEntity;
    }



    /**
     * If the data is empty
     *
     * @return The data is empty
     */
    private boolean isPlayListEmpty() {
        return playEntityList == null || playEntityList.size() == 0;
    }

    /**
     * The currently selected data is valid
     *
     * @param position Select position
     * @return Effective
     */
    private boolean isPlayEffective(int position) {
        return !isPlayListEmpty() && playEntityList.size() > position;
    }

    /**
     * Data for the selected titles
     *
     * @param position Select position
     * @return Data
     */
    public PlayEntity getPlayFromPosition(int position) {
        if (isPlayEffective(position)) {
            return playEntityList.get(position);
        }
        return null;
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//
//        inflater.inflate(R.menu.menu_main, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//
//    }

//    @Override
//    public void onSettingItemClick(String itemSelect, int settingType) {
//        switch (settingType) {
//            case Constants.PLAYER_SWITCH_VIDEO_MODE:
//                if (TextUtils.equals(itemSelect, getResources().getString(R.string.video_on_demand))) {
//                    homePageControl.setVideoType(Constants.VIDEO_TYPE_ON_DEMAND);
//                } else {
//                    homePageControl.setVideoType(Constants.VIDEO_TYPE_LIVE);
//                }
//                break;
//            case Constants.PLAYER_SWITCH_VIDEO_VIEW:
//                if (TextUtils.equals(itemSelect, getResources().getString(R.string.video_surfaceview_setting))) {
//                    homePageControl.setSurfaceViewView(true);
//                } else {
//                    homePageControl.setSurfaceViewView(false);
//                }
//                break;
//            case Constants.PLAYER_SWITCH_VIDEO_MUTE:
//                if (TextUtils.equals(itemSelect, getResources().getString(R.string.video_mute))) {
//                    homePageControl.setMute(true);
//                } else {
//                    homePageControl.setMute(false);
//                }
//                break;
//            case Constants.PLAYER_SWITCH_VIDEO_PLAY:
//                if (TextUtils.equals(itemSelect, getResources().getString(R.string.play_audio))) {
//                    homePageControl.setPlayMode(PlayerConstants.PlayMode.PLAY_MODE_AUDIO_ONLY);
//                } else {
//                    homePageControl.setPlayMode(PlayerConstants.PlayMode.PLAY_MODE_NORMAL);
//                }
//                break;
//            case Constants.PLAYER_SWITCH_BANDWIDTH:
//                if (TextUtils.equals(itemSelect, getResources().getString(R.string.close_adaptive_bandwidth))) {
//                    homePageControl.setBandwidthSwitchMode(PlayerConstants.BandwidthSwitchMode.MANUAL_SWITCH_MODE);
//                } else {
//                    homePageControl.setBandwidthSwitchMode(PlayerConstants.BandwidthSwitchMode.AUTO_SWITCH_MODE);
//                }
//                break;
//            case Constants.PLAYER_SWITCH_INIT_BANDWIDTH:
//                if (TextUtils.equals(itemSelect, getResources().getString(R.string.video_init_bitrate_use))) {
//                    homePageControl.setInitBitrateEnable(true);
//                    DialogUtil.setInitBitrate(context);
//                } else {
//                    homePageControl.setInitBitrateEnable(false);
//                }
//                break;
//            case Constants.PLAYER_SWITCH_CLOSE_LOGO:
//                if (TextUtils.equals(itemSelect, getResources().getString(R.string.video_open_logo_setting))) {
//                    homePageControl.setCloseLogo(false);
//                } else {
//                    List<String> list = new ArrayList<>();
//                    list.add(getResources().getString(R.string.video_close_logo_one));
//                    list.add(getResources().getString(R.string.video_close_logo_all));
//                    showVideoTypeDialog(Constants.PLAYER_SWITCH_CLOSE_LOGO_EFFECT, list,
//                            PlayControlUtil.isTakeEffectOfAll() ? Constants.DIALOG_INDEX_TWO : Constants.DIALOG_INDEX_ONE);
//                }
//                break;
//            case Constants.PLAYER_SWITCH_CLOSE_LOGO_EFFECT:
//                homePageControl.setCloseLogo(true);
//                if (TextUtils.equals(itemSelect, getResources().getString(R.string.video_close_logo_all))) {
//                    homePageControl.setBandwidthSwitchMode(true);
//                } else {
//                    homePageControl.setBandwidthSwitchMode(false);
//                }
//                break;
//            case Constants.DOWNLOAD_LINK_NUM:
//                if (TextUtils.equals(itemSelect, getResources().getString(R.string.download_link_single))) {
//                    homePageControl.setDownloadLink(true);
//                } else {
//                    homePageControl.setDownloadLink(false);
//                }
//                break;
//            case Constants.SET_WAKE_MODE:
//                if (TextUtils.equals(itemSelect, getResources().getString(R.string.set_wake_mode))) {
//                    homePageControl.setWakeMode(true);
//                } else {
//                    homePageControl.setWakeMode(false);
//                }
//                break;
//            case Constants.SET_SUBTITLE_RENDER_MODE:
//                if (TextUtils.equals(itemSelect, getResources().getString(R.string.subtitle_render_player))) {
//                    homePageControl.setSubtitleRenderByDemo(false);
//                } else {
//                    homePageControl.setSubtitleRenderByDemo(true);
//                }
//                break;
//            default:
//                break;
//        }
//    }



    /**
     * Show settings dialog
     *
     * @param playSettingType Play setting type
     * @param settingList Play setting text list
     * @param defaultSelect Default selection
     */
//    public void showVideoTypeDialog(int playSettingType, List<String> settingList, int defaultSelect) {
//        DialogUtil.showVideoTypeDialog(context, playSettingType, settingList, defaultSelect, this);
//    }
}