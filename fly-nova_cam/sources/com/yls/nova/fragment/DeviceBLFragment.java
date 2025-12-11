package com.yls.nova.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.cooingdv.bl60xmjpeg.UAV;
import com.cooingdv.bl60xmjpeg.callback.PicDataCallback;
import com.yls.nova.C0549R;
import com.yls.nova.activity.BrowseFileActivity;
import com.yls.nova.adapter.TopListViewAdapter;
import com.yls.nova.base.BaseFragment;
import com.yls.nova.interfaces.OnMjpegListener;
import com.yls.nova.interfaces.OnSocketListener;
import com.yls.nova.libs.HorizontalListView;
import com.yls.nova.libs.MyScaleView;
import com.yls.nova.libs.RockerView;
import com.yls.nova.libs.TrackView;
import com.yls.nova.libs.verticalseekbar.VerticalSeekBar;
import com.yls.nova.models.GravityModel;
import com.yls.nova.models.VideoModel;
import com.yls.nova.socket.Config;
import com.yls.nova.socket.SocketClient;
import com.yls.nova.thread.MjpegThread;
import com.yls.nova.thread.MyTimer;
import com.yls.nova.tools.BufChangeHex;
import com.yls.nova.tools.FlyController;
import com.yls.nova.tools.IConstants;
import com.yls.nova.tools.PreferencesHelper;
import com.yls.nova.tools.ScanFilesHelper;
import com.yls.nova.tools.TimeFormater;
import com.yls.nova.tools.TrackAnimationListener;
import com.yls.nova.utils.AppUtils;
import com.yls.nova.utils.BitmapUtil;
import com.yls.nova.utils.Dbug;
import com.yls.nova.utils.RecognitionUtils;
import com.yls.nova.utils.SoundUtils;
import com.yls.nova.utils.StorageUtil;
import com.yls.nova.utils.WifiIdUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import kotlinx.coroutines.scheduling.WorkQueueKt;

/* loaded from: classes.dex */
public class DeviceBLFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, RockerView.OnShakeListener, GravityModel.OnOperationListener, TrackView.OnTrackListener, TrackAnimationListener.AnimatorTrackListener, SeekBar.OnSeekBarChangeListener, View.OnTouchListener, OnMjpegListener {
    private static final long DEFAULT_VIDEO_SIZE = 31457280;
    private static final int MSG_GESTURE_TOAST = 43972;
    private static final int MSG_UPDATE_RECORDING_UI = 43971;
    private static final int OPERATION_ADD = 1;
    private static final int OPERATION_DEL = 0;
    private RelativeLayout LayoutControlMode;
    private TopListViewAdapter adapter;
    private ImageView air;
    private int airHeight;
    private int airWidth;
    private ViewPropertyAnimator animator;
    private ImageView btnEmergencyStop;
    private ImageView btnGesture;
    private ImageView btnGravitySensor;
    private ImageView btnGyro;
    private ImageView btnLand;
    private ImageView btnScale;
    private ImageView btnTakeoff;
    private ImageView btnTurn360;
    private ImageView btnVRMode;
    private Timer frameTimer;
    private Timer gestureTimer;
    private MyScaleView horizontalBar;
    private MyScaleView horizontalCenterBar;
    private boolean isRightHandMode;
    private ImageView ivBg;
    private ImageView ivBg2;
    private ImageView ivGestureType;
    private ImageView ivRecordNumber;
    private LinearLayout layoutBottom;
    private RelativeLayout layoutControlRightDraw;
    private RelativeLayout layoutControlRightRocker;
    private LinearLayout layoutRight;
    private LinearLayout layoutScaleSeek;
    private RelativeLayout layoutVideoView;
    private LinearLayout layoutViewMode;
    private FlyController mFlyController;
    private UAV mStreamClient;
    private VerticalSeekBar mVerticalSeekBar;
    private byte[] mVideoData;
    private MjpegThread mjpegThread;
    private ImageView mjpegView;
    private ImageView mjpegView2;
    private GravityModel model;
    private MyTimer myTimer;
    private RockerView rvHorizontal;
    private RockerView rvVertical;
    private String savePhotoName;
    private String savePhotoPath;
    private ScanFilesHelper scanFilesHelper;
    private float startX;
    private float startY;
    private HorizontalListView topListView;
    private TrackView trackView;
    private TextView tvScale;
    private MyScaleView verticalBar;
    private VideoModel videoModel;
    private View view;
    private int noCardRecordMax = 30;
    private int deviceVideoTime = 0;
    private boolean noCardRecording = false;
    private boolean noCardTaking = false;
    private boolean isVarMode = false;
    private boolean isSupport = false;
    private boolean isSwitchCamera = false;
    private int horizontalCurrentValue = 0;
    private int horizontalCenterCurrentValue = 0;
    private int verticalCurrentValue = 0;
    private int fakeResolutionType = 0;
    private int numPhoto = 0;
    private int numVideo = 0;
    private int palmCount = 0;
    private boolean isRecognition = false;
    private int[] recordNumber = {C0549R.mipmap.gesture_countdown_3, C0549R.mipmap.gesture_countdown_2, C0549R.mipmap.gesture_countdown_1};
    private int recordNumberIndex = 0;
    private float lastScaleX = 0.0f;
    private float lastScaleY = 0.0f;
    private Handler mHandler = new Handler(new C05681());
    private int currPower = 4;
    ArrayList<String> topList = new ArrayList<>();
    private final PicDataCallback onStreamListener = new C05703();
    private OnSocketListener mOnSocketListener = new C05714();

    @Override // com.yls.nova.libs.RockerView.OnShakeListener
    public void direction(RockerView rockerView, RockerView.Direction direction, double d, int i, int i2) {
    }

    @Override // com.yls.nova.libs.RockerView.OnShakeListener
    public void onFinish(RockerView rockerView) {
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    static /* synthetic */ int access$2908(DeviceBLFragment deviceBLFragment) {
        int i = deviceBLFragment.palmCount;
        deviceBLFragment.palmCount = i + 1;
        return i;
    }

    static /* synthetic */ int access$908(DeviceBLFragment deviceBLFragment) {
        int i = deviceBLFragment.recordNumberIndex;
        deviceBLFragment.recordNumberIndex = i + 1;
        return i;
    }

    /* renamed from: com.yls.nova.fragment.DeviceBLFragment$1 */
    class C05681 implements Handler.Callback {
        C05681() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            if (DeviceBLFragment.this.getActivity() != null && DeviceBLFragment.this.isAdded() && message != null) {
                switch (message.what) {
                    case DeviceBLFragment.MSG_UPDATE_RECORDING_UI /* 43971 */:
                        if (DeviceBLFragment.this.myTimer != null && DeviceBLFragment.this.myTimer.isTimerRunning()) {
                            String strShowRecordingTimeFormat = TimeFormater.showRecordingTimeFormat(((Integer) message.obj).intValue());
                            if (!TextUtils.isEmpty(strShowRecordingTimeFormat)) {
                                DeviceBLFragment.this.adapter.setTimer(strShowRecordingTimeFormat);
                                DeviceBLFragment.this.adapter.notifyDataSetChanged();
                                break;
                            }
                        }
                        break;
                    case DeviceBLFragment.MSG_GESTURE_TOAST /* 43972 */:
                        if (!DeviceBLFragment.this.isRecognition) {
                            int[] iArr = (int[]) message.obj;
                            int i = iArr[0];
                            DeviceBLFragment.this.isRecognition = true;
                            if (i != 2) {
                                if (i == 1) {
                                    if (DeviceBLFragment.this.noCardRecording) {
                                        DeviceBLFragment.this.gestureRecogFrame(iArr);
                                        DeviceBLFragment.this.topListView.performItemClick(DeviceBLFragment.this.topListView.getChildAt(2), 2, DeviceBLFragment.this.topListView.getItemIdAtPosition(1));
                                        DeviceBLFragment.this.mHandler.postDelayed(new Runnable() { // from class: com.yls.nova.fragment.DeviceBLFragment.1.2
                                            @Override // java.lang.Runnable
                                            public void run() {
                                                DeviceBLFragment.this.isRecognition = false;
                                                DeviceBLFragment.this.layoutVideoView.removeAllViews();
                                            }
                                        }, 2000L);
                                        break;
                                    } else {
                                        DeviceBLFragment.this.gestureRecogFrame(iArr);
                                        DeviceBLFragment.this.ivGestureType.setVisibility(0);
                                        DeviceBLFragment.this.ivGestureType.setImageResource(C0549R.mipmap.gesture_fist_red);
                                        DeviceBLFragment.this.ivRecordNumber.setVisibility(0);
                                        DeviceBLFragment.this.ivRecordNumber.setImageResource(DeviceBLFragment.this.recordNumber[DeviceBLFragment.this.recordNumberIndex]);
                                        DeviceBLFragment.access$908(DeviceBLFragment.this);
                                        DeviceBLFragment.this.mHandler.postDelayed(new Runnable() { // from class: com.yls.nova.fragment.DeviceBLFragment.1.3
                                            @Override // java.lang.Runnable
                                            public void run() {
                                                if (DeviceBLFragment.this.recordNumberIndex == 3) {
                                                    DeviceBLFragment.this.ivGestureType.setVisibility(8);
                                                    DeviceBLFragment.this.ivRecordNumber.setVisibility(8);
                                                    DeviceBLFragment.this.layoutVideoView.removeAllViews();
                                                    DeviceBLFragment.this.topListView.performItemClick(DeviceBLFragment.this.topListView.getChildAt(2), 2, DeviceBLFragment.this.topListView.getItemIdAtPosition(1));
                                                } else {
                                                    DeviceBLFragment.this.ivRecordNumber.setImageResource(DeviceBLFragment.this.recordNumber[DeviceBLFragment.this.recordNumberIndex]);
                                                }
                                                DeviceBLFragment.access$908(DeviceBLFragment.this);
                                                if (DeviceBLFragment.this.recordNumberIndex > 3) {
                                                    DeviceBLFragment.this.mHandler.postDelayed(new Runnable() { // from class: com.yls.nova.fragment.DeviceBLFragment.1.3.1
                                                        @Override // java.lang.Runnable
                                                        public void run() {
                                                            DeviceBLFragment.this.isRecognition = false;
                                                            DeviceBLFragment.this.recordNumberIndex = 0;
                                                        }
                                                    }, 2000L);
                                                } else {
                                                    DeviceBLFragment.this.mHandler.postDelayed(this, 1000L);
                                                }
                                            }
                                        }, 1000L);
                                        break;
                                    }
                                }
                            } else if (DeviceBLFragment.this.noCardTaking || DeviceBLFragment.this.noCardRecording) {
                                DeviceBLFragment.this.isRecognition = false;
                                break;
                            } else {
                                DeviceBLFragment.this.gestureRecogFrame(iArr);
                                DeviceBLFragment.this.ivGestureType.setVisibility(0);
                                DeviceBLFragment.this.ivGestureType.setImageResource(C0549R.mipmap.gesture_rpalm_red);
                                DeviceBLFragment.this.ivRecordNumber.setVisibility(0);
                                DeviceBLFragment.this.ivRecordNumber.setImageResource(DeviceBLFragment.this.recordNumber[DeviceBLFragment.this.recordNumberIndex]);
                                DeviceBLFragment.access$908(DeviceBLFragment.this);
                                DeviceBLFragment.this.mHandler.postDelayed(new Runnable() { // from class: com.yls.nova.fragment.DeviceBLFragment.1.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        if (DeviceBLFragment.this.recordNumberIndex == 3) {
                                            DeviceBLFragment.this.ivGestureType.setVisibility(8);
                                            DeviceBLFragment.this.ivRecordNumber.setVisibility(8);
                                            DeviceBLFragment.this.layoutVideoView.removeAllViews();
                                            DeviceBLFragment.this.topListView.performItemClick(DeviceBLFragment.this.topListView.getChildAt(1), 1, DeviceBLFragment.this.topListView.getItemIdAtPosition(0));
                                        } else {
                                            DeviceBLFragment.this.ivRecordNumber.setImageResource(DeviceBLFragment.this.recordNumber[DeviceBLFragment.this.recordNumberIndex]);
                                        }
                                        DeviceBLFragment.access$908(DeviceBLFragment.this);
                                        if (DeviceBLFragment.this.recordNumberIndex > 3) {
                                            DeviceBLFragment.this.mHandler.postDelayed(new Runnable() { // from class: com.yls.nova.fragment.DeviceBLFragment.1.1.1
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    DeviceBLFragment.this.isRecognition = false;
                                                    DeviceBLFragment.this.recordNumberIndex = 0;
                                                }
                                            }, 2000L);
                                        } else {
                                            DeviceBLFragment.this.mHandler.postDelayed(this, 1000L);
                                        }
                                    }
                                }, 1000L);
                                break;
                            }
                        } else {
                            return false;
                        }
                        break;
                }
            }
            return false;
        }
    }

    @Override // com.yls.nova.interfaces.OnMjpegListener
    public void onFrame(final Bitmap bitmap) {
        this.mHandler.post(new Runnable() { // from class: com.yls.nova.fragment.DeviceBLFragment.2
            @Override // java.lang.Runnable
            public void run() {
                DeviceBLFragment.this.mjpegView.setImageBitmap(bitmap);
                if (DeviceBLFragment.this.isVarMode) {
                    DeviceBLFragment.this.mjpegView2.setImageBitmap(bitmap);
                }
            }
        });
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        boolean z = PreferencesHelper.getSharedPreferences(getContext()).getBoolean(IConstants.RIGHT_HAND_MODE, false);
        this.isRightHandMode = z;
        if (z) {
            this.view = layoutInflater.inflate(C0549R.layout.fragment_device_right_hand, viewGroup, false);
        } else {
            this.view = layoutInflater.inflate(C0549R.layout.fragment_device, viewGroup, false);
        }
        this.LayoutControlMode = (RelativeLayout) this.view.findViewById(C0549R.id.device_control_mode);
        this.layoutControlRightRocker = (RelativeLayout) this.view.findViewById(C0549R.id.view_control_right_rocker);
        this.layoutControlRightDraw = (RelativeLayout) this.view.findViewById(C0549R.id.view_control_right_draw);
        this.layoutViewMode = (LinearLayout) this.view.findViewById(C0549R.id.device_view_mode);
        this.layoutBottom = (LinearLayout) this.view.findViewById(C0549R.id.view_control_device_bottom_layout);
        this.layoutRight = (LinearLayout) this.view.findViewById(C0549R.id.view_control_device_right_layout);
        this.topListView = (HorizontalListView) this.view.findViewById(C0549R.id.bar_device_top_list_view);
        this.layoutVideoView = (RelativeLayout) this.view.findViewById(C0549R.id.device_video_view_layout);
        this.ivBg = (ImageView) this.view.findViewById(C0549R.id.iv_bg);
        this.ivBg2 = (ImageView) this.view.findViewById(C0549R.id.iv_bg2);
        ImageView imageView = (ImageView) this.view.findViewById(C0549R.id.device_video_view);
        this.mjpegView = imageView;
        imageView.setOnTouchListener(this);
        this.mjpegView2 = (ImageView) this.view.findViewById(C0549R.id.device_video_view2);
        ImageView imageView2 = (ImageView) this.view.findViewById(C0549R.id.view_control_device_take_off);
        this.btnTakeoff = imageView2;
        imageView2.setOnClickListener(this);
        ImageView imageView3 = (ImageView) this.view.findViewById(C0549R.id.view_control_device_land);
        this.btnLand = imageView3;
        imageView3.setOnClickListener(this);
        ((ImageView) this.view.findViewById(C0549R.id.view_device_mode_headmode)).setOnClickListener(this);
        ImageView imageView4 = (ImageView) this.view.findViewById(C0549R.id.view_device_mode_turn360);
        this.btnTurn360 = imageView4;
        imageView4.setOnClickListener(this);
        ((ImageView) this.view.findViewById(C0549R.id.view_device_mode_track)).setOnClickListener(this);
        ImageView imageView5 = (ImageView) this.view.findViewById(C0549R.id.view_control_device_emergency_stop);
        this.btnEmergencyStop = imageView5;
        imageView5.setOnClickListener(this);
        ImageView imageView6 = (ImageView) this.view.findViewById(C0549R.id.view_device_mode_gravity_sensor);
        this.btnGravitySensor = imageView6;
        imageView6.setOnClickListener(this);
        ImageView imageView7 = (ImageView) this.view.findViewById(C0549R.id.view_device_mode_gyroscope);
        this.btnGyro = imageView7;
        imageView7.setOnClickListener(this);
        ImageView imageView8 = (ImageView) this.view.findViewById(C0549R.id.view_device_mode_gesture);
        this.btnGesture = imageView8;
        imageView8.setOnClickListener(this);
        this.ivRecordNumber = (ImageView) this.view.findViewById(C0549R.id.device_video_gesture_iv);
        this.ivGestureType = (ImageView) this.view.findViewById(C0549R.id.down_menu_im_gesture_type);
        ImageView imageView9 = (ImageView) this.view.findViewById(C0549R.id.view_device_mode_vr);
        this.btnVRMode = imageView9;
        imageView9.setOnClickListener(this);
        ImageView imageView10 = (ImageView) this.view.findViewById(C0549R.id.down_menu_btn_scale);
        this.btnScale = imageView10;
        imageView10.setOnClickListener(this);
        this.horizontalBar = (MyScaleView) this.view.findViewById(C0549R.id.view_control_device_horizontal_bar);
        View viewFindViewById = this.view.findViewById(C0549R.id.view_control_device_horizontal_del);
        View viewFindViewById2 = this.view.findViewById(C0549R.id.view_control_device_horizontal_add);
        this.horizontalCenterBar = (MyScaleView) this.view.findViewById(C0549R.id.view_control_device_horizontal_center_bar);
        View viewFindViewById3 = this.view.findViewById(C0549R.id.view_control_device_horizontal_left);
        View viewFindViewById4 = this.view.findViewById(C0549R.id.view_control_device_horizontal_right);
        this.verticalBar = (MyScaleView) this.view.findViewById(C0549R.id.view_control_device_vertical_bar);
        View viewFindViewById5 = this.view.findViewById(C0549R.id.view_control_device_vertical_del);
        View viewFindViewById6 = this.view.findViewById(C0549R.id.view_control_device_vertical_add);
        this.rvVertical = (RockerView) this.view.findViewById(C0549R.id.view_control_device_left_rocker);
        this.rvHorizontal = (RockerView) this.view.findViewById(C0549R.id.view_control_device_right_rocker);
        this.trackView = (TrackView) this.view.findViewById(C0549R.id.view_control_device_track_view);
        this.layoutScaleSeek = (LinearLayout) this.view.findViewById(C0549R.id.device_scale_seek_layout);
        this.tvScale = (TextView) this.view.findViewById(C0549R.id.device_view_scale_tv);
        VerticalSeekBar verticalSeekBar = (VerticalSeekBar) this.view.findViewById(C0549R.id.device_view_scale_seek_progress);
        this.mVerticalSeekBar = verticalSeekBar;
        verticalSeekBar.setMax(50);
        this.mVerticalSeekBar.setOnSeekBarChangeListener(this);
        viewFindViewById.setOnClickListener(this);
        viewFindViewById2.setOnClickListener(this);
        viewFindViewById3.setOnClickListener(this);
        viewFindViewById4.setOnClickListener(this);
        viewFindViewById5.setOnClickListener(this);
        viewFindViewById6.setOnClickListener(this);
        this.topListView.setOnItemClickListener(this);
        this.rvVertical.setFixHeight(false);
        this.rvVertical.setOnShakeListener(RockerView.DirectionMode.DIRECTION_4_ROTATE_45, this);
        this.rvVertical.setCallBackMode(RockerView.CallBackMode.CALL_BACK_MODE_STATE_DISTANCE_CHANGE);
        this.rvHorizontal.setFixHeight(true);
        this.rvHorizontal.setOnShakeListener(RockerView.DirectionMode.DIRECTION_4_ROTATE_45, this);
        this.rvHorizontal.setCallBackMode(RockerView.CallBackMode.CALL_BACK_MODE_STATE_DISTANCE_CHANGE);
        this.rvHorizontal.setIsJohnCustomMode(true);
        this.trackView.setOnTrackListener(this);
        GravityModel gravityModel = new GravityModel(this);
        this.model = gravityModel;
        gravityModel.setOnOperationListener(this);
        this.videoModel = new VideoModel(this);
        return this.view;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) throws Resources.NotFoundException {
        super.onActivityCreated(bundle);
        if (getActivity() != null) {
            MjpegThread mjpegThread = new MjpegThread(getActivity());
            this.mjpegThread = mjpegThread;
            mjpegThread.setOnMjpegListener(this);
            this.mjpegThread.start();
            initListView();
            setResolution(UAV.getInstance().getResolutionNumber());
            this.mjpegView.setVisibility(8);
            this.mjpegView2.setVisibility(8);
            this.mStreamClient = UAV.getInstance();
            this.mFlyController = new FlyController(this.mHandler);
            if (this.scanFilesHelper == null) {
                this.scanFilesHelper = new ScanFilesHelper(getActivity().getApplicationContext());
            }
            setInserts();
        }
    }

    private void setInserts() {
        if (this.isRightHandMode) {
            ViewCompat.setOnApplyWindowInsetsListener(this.layoutRight, new OnApplyWindowInsetsListener() { // from class: com.yls.nova.fragment.DeviceBLFragment$$ExternalSyntheticLambda0
                @Override // androidx.core.view.OnApplyWindowInsetsListener
                public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                    return DeviceBLFragment.lambda$setInserts$0(view, windowInsetsCompat);
                }
            });
        } else {
            ViewCompat.setOnApplyWindowInsetsListener(this.layoutScaleSeek, new OnApplyWindowInsetsListener() { // from class: com.yls.nova.fragment.DeviceBLFragment$$ExternalSyntheticLambda1
                @Override // androidx.core.view.OnApplyWindowInsetsListener
                public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                    return DeviceBLFragment.lambda$setInserts$1(view, windowInsetsCompat);
                }
            });
        }
        ViewCompat.setOnApplyWindowInsetsListener(this.view, new OnApplyWindowInsetsListener() { // from class: com.yls.nova.fragment.DeviceBLFragment$$ExternalSyntheticLambda2
            @Override // androidx.core.view.OnApplyWindowInsetsListener
            public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                return DeviceBLFragment.lambda$setInserts$2(view, windowInsetsCompat);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(this.layoutBottom, new OnApplyWindowInsetsListener() { // from class: com.yls.nova.fragment.DeviceBLFragment$$ExternalSyntheticLambda3
            @Override // androidx.core.view.OnApplyWindowInsetsListener
            public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                return DeviceBLFragment.lambda$setInserts$3(view, windowInsetsCompat);
            }
        });
    }

    static /* synthetic */ WindowInsetsCompat lambda$setInserts$0(View view, WindowInsetsCompat windowInsetsCompat) {
        view.setPadding(windowInsetsCompat.getInsets(WindowInsetsCompat.Type.displayCutout()).left, 0, 0, 0);
        return windowInsetsCompat;
    }

    static /* synthetic */ WindowInsetsCompat lambda$setInserts$1(View view, WindowInsetsCompat windowInsetsCompat) {
        view.setPadding(windowInsetsCompat.getInsets(WindowInsetsCompat.Type.displayCutout()).left, 0, 0, 0);
        return windowInsetsCompat;
    }

    static /* synthetic */ WindowInsetsCompat lambda$setInserts$2(View view, WindowInsetsCompat windowInsetsCompat) {
        view.setPadding(0, 0, windowInsetsCompat.getInsets(WindowInsetsCompat.Type.navigationBars()).right, 0);
        return windowInsetsCompat;
    }

    static /* synthetic */ WindowInsetsCompat lambda$setInserts$3(View view, WindowInsetsCompat windowInsetsCompat) {
        view.setPadding(0, 0, 0, windowInsetsCompat.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom);
        return windowInsetsCompat;
    }

    @Override // com.yls.nova.base.BaseFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        initStatus();
        startServer();
    }

    private void startServer() {
        this.mStreamClient.setPicDataCallback(this.onStreamListener);
        SocketClient.getInstance().setOnSocketListener(this.mOnSocketListener);
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        cleanStatus();
    }

    public void cleanStatus() {
        if (this.mjpegView.getVisibility() == 0) {
            this.mjpegView.setVisibility(8);
        }
        if (this.mjpegView2.getVisibility() == 0) {
            this.mjpegView2.setVisibility(8);
        }
        if (this.noCardRecording) {
            this.noCardRecording = false;
            stopNoCardRecording();
            TopListViewAdapter topListViewAdapter = this.adapter;
            if (topListViewAdapter != null) {
                topListViewAdapter.setRecordVideo(false);
            }
        }
        if (this.noCardTaking) {
            this.noCardTaking = false;
        }
        FlyController flyController = this.mFlyController;
        if (flyController != null) {
            flyController.setGravitySensor(false);
            this.model.unRegisterListener();
            this.rvHorizontal.setFixHeight(true);
            this.rvHorizontal.setIsGravityMode(false);
            this.btnGravitySensor.setImageResource(C0549R.drawable.drawable_gravity_icon);
        }
        TopListViewAdapter topListViewAdapter2 = this.adapter;
        if (topListViewAdapter2 != null) {
            topListViewAdapter2.notifyDataSetChanged();
        }
        Timer timer = this.gestureTimer;
        if (timer != null) {
            timer.cancel();
            this.gestureTimer = null;
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        if (this.mFlyController.isControlMode()) {
            updateModeUI();
        }
        this.deviceVideoTime = 0;
        MyTimer myTimer = this.myTimer;
        if (myTimer != null) {
            myTimer.stopTimer();
            this.myTimer = null;
        }
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        MjpegThread mjpegThread = this.mjpegThread;
        if (mjpegThread != null) {
            mjpegThread.release();
        }
        ScanFilesHelper scanFilesHelper = this.scanFilesHelper;
        if (scanFilesHelper != null) {
            scanFilesHelper.release();
            this.scanFilesHelper = null;
        }
        GravityModel gravityModel = this.model;
        if (gravityModel != null) {
            gravityModel.unRegisterListener();
        }
        if (this.videoModel.isRecording()) {
            this.videoModel.stopRecorder();
        }
        Timer timer = this.frameTimer;
        if (timer != null) {
            timer.cancel();
            this.frameTimer = null;
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) throws IllegalStateException {
        if (view == null || getActivity() == null) {
            return;
        }
        int id = view.getId();
        switch (id) {
            case C0549R.id.down_menu_btn_scale /* 2131296395 */:
                if (this.layoutScaleSeek.getVisibility() == 0) {
                    this.layoutScaleSeek.setVisibility(8);
                    ((ImageView) view).setImageResource(C0549R.drawable.drawable_scale);
                    break;
                } else {
                    this.layoutScaleSeek.setVisibility(0);
                    ((ImageView) view).setImageResource(C0549R.drawable.drawable_scale_yellow);
                    break;
                }
            case C0549R.id.view_control_device_take_off /* 2131296615 */:
                this.mFlyController.setFastFly((ImageView) view);
                break;
            case C0549R.id.view_control_device_vertical_add /* 2131296617 */:
                updateAdjustBar(this.verticalBar, 1);
                break;
            case C0549R.id.view_control_device_vertical_del /* 2131296619 */:
                updateAdjustBar(this.verticalBar, 0);
                break;
            default:
                switch (id) {
                    case C0549R.id.view_control_device_emergency_stop /* 2131296604 */:
                        this.mFlyController.setEmergencyStop((ImageView) view);
                        break;
                    case C0549R.id.view_control_device_horizontal_add /* 2131296605 */:
                        updateAdjustBar(this.horizontalBar, 1);
                        break;
                    default:
                        switch (id) {
                            case C0549R.id.view_control_device_horizontal_del /* 2131296608 */:
                                updateAdjustBar(this.horizontalBar, 0);
                                break;
                            case C0549R.id.view_control_device_horizontal_left /* 2131296609 */:
                                updateAdjustBar(this.horizontalCenterBar, 0);
                                break;
                            case C0549R.id.view_control_device_horizontal_right /* 2131296610 */:
                                updateAdjustBar(this.horizontalCenterBar, 1);
                                break;
                            case C0549R.id.view_control_device_land /* 2131296611 */:
                                this.mFlyController.setFastDrop((ImageView) view);
                                break;
                            default:
                                switch (id) {
                                    case C0549R.id.view_device_mode_gesture /* 2131296625 */:
                                        openGesture();
                                        break;
                                    case C0549R.id.view_device_mode_gravity_sensor /* 2131296626 */:
                                        FlyController flyController = this.mFlyController;
                                        if (flyController != null) {
                                            flyController.setGravitySensor(!flyController.isGravitySensor());
                                            if (this.mFlyController.isGravitySensor()) {
                                                ((ImageView) view).setImageResource(C0549R.drawable.drawable_gravity_yellow);
                                                this.model.registerSensorListener();
                                                this.rvHorizontal.setIsGravityMode(true);
                                                break;
                                            } else {
                                                ((ImageView) view).setImageResource(C0549R.drawable.drawable_gravity_icon);
                                                this.model.unRegisterListener();
                                                this.rvHorizontal.setIsGravityMode(false);
                                                this.rvHorizontal.setFixHeight(true);
                                                FlyController flyController2 = this.mFlyController;
                                                Objects.requireNonNull(flyController2);
                                                flyController2.setControlByte1(128);
                                                FlyController flyController3 = this.mFlyController;
                                                Objects.requireNonNull(flyController3);
                                                flyController3.setControlByte2(128);
                                                break;
                                            }
                                        }
                                        break;
                                    case C0549R.id.view_device_mode_gyroscope /* 2131296627 */:
                                        this.mFlyController.setGyroCorrection((ImageView) view);
                                        break;
                                    case C0549R.id.view_device_mode_headmode /* 2131296628 */:
                                        this.mFlyController.setNoHeadMode((ImageView) view);
                                        break;
                                    case C0549R.id.view_device_mode_track /* 2131296629 */:
                                        FlyController flyController4 = this.mFlyController;
                                        if (flyController4 != null) {
                                            flyController4.setTrackMode(true ^ flyController4.isTrackMode());
                                            if (this.mFlyController.isTrackMode()) {
                                                ((ImageView) view).setImageResource(C0549R.drawable.drawable_track_yellow);
                                                this.layoutControlRightRocker.setVisibility(8);
                                                this.layoutControlRightDraw.setVisibility(0);
                                                break;
                                            } else {
                                                ((ImageView) view).setImageResource(C0549R.drawable.drawable_track_icon);
                                                this.layoutControlRightRocker.setVisibility(0);
                                                this.layoutControlRightDraw.setVisibility(8);
                                                ViewPropertyAnimator viewPropertyAnimator = this.animator;
                                                if (viewPropertyAnimator != null) {
                                                    viewPropertyAnimator.cancel();
                                                    break;
                                                }
                                            }
                                        }
                                        break;
                                    case C0549R.id.view_device_mode_turn360 /* 2131296630 */:
                                        this.mFlyController.setCircleTurn((ImageView) view);
                                        break;
                                    case C0549R.id.view_device_mode_vr /* 2131296631 */:
                                        if (this.isVarMode) {
                                            this.isVarMode = false;
                                            ((ImageView) view).setImageResource(C0549R.drawable.drawable_vr_yellow);
                                            this.mjpegView2.setVisibility(8);
                                            this.ivBg2.setVisibility(8);
                                            this.mjpegView.setScaleType(ImageView.ScaleType.FIT_XY);
                                            if (this.mFlyController.isControlMode()) {
                                                this.LayoutControlMode.setVisibility(0);
                                                break;
                                            }
                                        } else {
                                            this.mjpegView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                            this.ivBg2.setVisibility(0);
                                            if (!this.mStreamClient.isActive() && !SocketClient.getInstance().isActive()) {
                                                this.ivBg2.setVisibility(0);
                                            }
                                            this.isVarMode = true;
                                            ((ImageView) view).setImageResource(C0549R.drawable.drawable_vr);
                                            FlyController flyController5 = this.mFlyController;
                                            if (flyController5 != null) {
                                                if (flyController5.isControlMode()) {
                                                    this.LayoutControlMode.setVisibility(8);
                                                }
                                                if (this.mFlyController.isGravitySensor()) {
                                                    this.mFlyController.setGravitySensor(false);
                                                    this.model.unRegisterListener();
                                                    this.rvHorizontal.setIsGravityMode(false);
                                                    this.rvHorizontal.setFixHeight(true);
                                                    break;
                                                }
                                            }
                                        }
                                        break;
                                }
                        }
                }
        }
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) throws IllegalStateException, IOException {
        if (this.adapter == null || getActivity() == null) {
            return;
        }
        String str = (String) this.adapter.getItem(i);
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (str.equals(getString(C0549R.string.tab_control_mode))) {
            updateModeUI();
            return;
        }
        if (str.equals(getString(C0549R.string.tab_take_photo))) {
            if (BufChangeHex.isFastDoubleClick(Config.RECONNECTION_INTERVAL)) {
                return;
            }
            if (this.noCardTaking) {
                showLongToast(getString(C0549R.string.taking_photo));
                return;
            } else if (!this.noCardRecording) {
                startNoCardTaking();
                return;
            } else {
                showShortToast(C0549R.string.no_card_taking_video_error);
                return;
            }
        }
        if (str.equals(getString(C0549R.string.tab_record_video))) {
            if (BufChangeHex.isFastDoubleClick(1000)) {
                return;
            }
            if (this.noCardRecording) {
                stopNoCardRecording();
                return;
            } else {
                startNoCardRecording();
                return;
            }
        }
        if (str.equals(getString(C0549R.string.tab_media_library))) {
            if (this.noCardRecording) {
                return;
            }
            stopNoCardRecording();
            Intent intent = new Intent(getActivity(), (Class<?>) BrowseFileActivity.class);
            intent.putExtra(IConstants.KEY_DIR_TYPE, IConstants.VIEW_FRONT);
            startActivity(intent);
            return;
        }
        if (str.equals(getString(C0549R.string.tab_power))) {
            int i2 = this.currPower;
            if (i2 == 4) {
                this.currPower = 9;
            } else if (i2 == 9) {
                this.currPower = 14;
            } else if (i2 == 14) {
                this.currPower = 4;
            }
            this.adapter.setCurrPower(this.currPower);
            this.adapter.notifyDataSetChanged();
            return;
        }
        if (str.equals(getString(C0549R.string.tab_down_menu))) {
            this.adapter.setMenuOpen(!r1.isMenuOpen());
            this.adapter.notifyDataSetChanged();
            if (this.adapter.isMenuOpen()) {
                this.layoutViewMode.setVisibility(0);
                return;
            } else {
                this.layoutViewMode.setVisibility(8);
                return;
            }
        }
        if (str.equals(getString(C0549R.string.tab_fixed_height))) {
            FlyController flyController = this.mFlyController;
            if (flyController != null) {
                flyController.setFixedHeightMode(!flyController.isFixedHeightMode());
                if (this.mFlyController.isFixedHeightMode()) {
                    this.rvVertical.setFixHeight(true);
                    this.adapter.setFixedHeightMode(true);
                    ((ImageView) view).setImageResource(C0549R.drawable.drawable_fixed_height_yellow);
                    updateFixHeightUI(true);
                    PreferencesHelper.putBooleanValue(getActivity(), IConstants.FIX_HEIGHT_SHARE_KEY, true);
                    FlyController flyController2 = this.mFlyController;
                    Objects.requireNonNull(flyController2);
                    flyController2.setControlAccelerator(128);
                } else {
                    this.rvVertical.setFixHeight(false);
                    this.adapter.setFixedHeightMode(false);
                    ((ImageView) view).setImageResource(C0549R.drawable.drawable_fixed_height);
                    updateFixHeightUI(false);
                    PreferencesHelper.putBooleanValue(getActivity(), IConstants.FIX_HEIGHT_SHARE_KEY, false);
                    this.mFlyController.setControlAccelerator(0);
                }
                this.adapter.notifyDataSetChanged();
                return;
            }
            return;
        }
        if (str.equals(getString(C0549R.string.tab_turn_180))) {
            if (!UAV.getInstance().isActive() && !SocketClient.getInstance().isActive()) {
                showShortToast(C0549R.string.connect_device_tip);
                return;
            }
            MjpegThread mjpegThread = this.mjpegThread;
            if (mjpegThread != null) {
                mjpegThread.setTurnBitmap(!mjpegThread.getTurnBitmap());
                PreferencesHelper.putBooleanValue(getActivity(), IConstants.SCREEN_DIRECTION_SHARE_KEY, this.mjpegThread.getTurnBitmap());
                this.adapter.setTurn180(this.mjpegThread.getTurnBitmap());
                return;
            }
            return;
        }
        if (str.equals(getString(C0549R.string.tab_camera_switch))) {
            if (SocketClient.getInstance().isActive()) {
                SocketClient.getInstance().switchCamera();
                return;
            } else {
                this.isSwitchCamera = !this.isSwitchCamera;
                UAV.getInstance().switchActiveCamera(this.isSwitchCamera);
                return;
            }
        }
        if (str.equals(getString(C0549R.string.tab_turn_back))) {
            getActivity().onBackPressed();
        } else {
            showShortToast(C0549R.string.no_function_tip);
        }
    }

    private void updateFixHeightUI(boolean z) {
        if (z) {
            this.btnTakeoff.setVisibility(0);
            this.btnEmergencyStop.setVisibility(0);
            this.btnLand.setVisibility(0);
        } else {
            this.btnTakeoff.setVisibility(8);
            this.btnEmergencyStop.setVisibility(8);
            this.btnLand.setVisibility(8);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x00a9  */
    /* JADX WARN: Removed duplicated region for block: B:38:? A[RETURN, SYNTHETIC] */
    @Override // com.yls.nova.libs.RockerView.OnShakeListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onMove(RockerView rockerView, float f, float f2, int i) {
        int i2;
        if (this.mFlyController.isCircleTurnEnd()) {
            return;
        }
        if (rockerView == this.rvVertical) {
            float f3 = i;
            int i3 = ((int) ((f / f3) * 127.0f)) + (this.horizontalCurrentValue * 4);
            Objects.requireNonNull(this.mFlyController);
            Objects.requireNonNull(this.mFlyController);
            this.mFlyController.setControlAccelerator(((int) ((f2 / f3) * 127.0f)) + 128);
            this.mFlyController.setControlTurn(i3 + 128);
        }
        if (rockerView != this.rvHorizontal) {
            return;
        }
        float f4 = i;
        float halfLen = getHalfLen();
        int i4 = (int) ((f / f4) * halfLen);
        int i5 = (int) ((f2 / f4) * halfLen);
        int i6 = this.currPower;
        if (i6 == 4) {
            int i7 = i4 + (this.horizontalCenterCurrentValue * 2);
            Objects.requireNonNull(this.mFlyController);
            i4 = i7 + 128;
            i2 = i5 + (this.verticalCurrentValue * 2);
            Objects.requireNonNull(this.mFlyController);
        } else if (i6 == 9) {
            int i8 = i4 + (this.horizontalCenterCurrentValue * 3);
            Objects.requireNonNull(this.mFlyController);
            i4 = i8 + 128;
            i2 = i5 + (this.verticalCurrentValue * 3);
            Objects.requireNonNull(this.mFlyController);
        } else {
            if (i6 == 14) {
                int i9 = i4 + (this.horizontalCenterCurrentValue * 4);
                Objects.requireNonNull(this.mFlyController);
                i4 = i9 + 128;
                i2 = i5 + (this.verticalCurrentValue * 4);
                Objects.requireNonNull(this.mFlyController);
            }
            this.mFlyController.setControlByte1(i4);
            this.mFlyController.setControlByte2(i5);
            if (this.mFlyController.isCircleTurn()) {
                return;
            }
            if (Math.abs(f) / rockerView.getRegionRadius() > 0.7d && f2 > -30.0f && f2 < 30.0f) {
                turn360(1, f);
                return;
            } else {
                if (Math.abs(f2) / rockerView.getRegionRadius() <= 0.7d || f <= -30.0f || f >= 30.0f) {
                    return;
                }
                turn360(2, f2);
                return;
            }
        }
        i5 = i2 + 128;
        this.mFlyController.setControlByte1(i4);
        this.mFlyController.setControlByte2(i5);
        if (this.mFlyController.isCircleTurn()) {
        }
    }

    private void turn360(int i, float f) {
        this.mFlyController.setCircleTurn(this.btnTurn360);
        if (i == 1) {
            FlyController flyController = this.mFlyController;
            Objects.requireNonNull(flyController);
            flyController.setControlByte1(f <= 0.0f ? 1 : 255);
        } else {
            FlyController flyController2 = this.mFlyController;
            Objects.requireNonNull(flyController2);
            flyController2.setControlByte2(f <= 0.0f ? 1 : 255);
        }
        this.mFlyController.setCircleTurnEnd();
    }

    private int getHalfLen() {
        int i = this.currPower;
        if (i == 9) {
            return 60;
        }
        if (i != 14) {
            return 40;
        }
        return WorkQueueKt.MASK;
    }

    @Override // com.yls.nova.models.GravityModel.OnOperationListener
    public void onChange(int i, int i2) {
        if (this.rvHorizontal == null || !this.mFlyController.isControlMode()) {
            return;
        }
        this.rvHorizontal.setPosition(i, i2);
    }

    private void initListView() throws Resources.NotFoundException {
        int i;
        if (getActivity() != null) {
            String[] stringArray = getResources().getStringArray(C0549R.array.device_top_list);
            this.topList.clear();
            Collections.addAll(this.topList, stringArray);
            HashMap map = new HashMap();
            Iterator<String> it = this.topList.iterator();
            while (it.hasNext()) {
                String next = it.next();
                if (!TextUtils.isEmpty(next)) {
                    if (next.equals(getString(C0549R.string.tab_take_photo))) {
                        i = C0549R.drawable.drawable_take_photo;
                    } else if (next.equals(getString(C0549R.string.tab_record_video))) {
                        i = C0549R.drawable.drawable_video;
                    } else if (next.equals(getString(C0549R.string.tab_power))) {
                        i = C0549R.mipmap.icon_power_30;
                    } else if (next.equals(getString(C0549R.string.tab_gravity_sensor))) {
                        i = C0549R.drawable.drawable_gravity_icon;
                    } else if (next.equals(getString(C0549R.string.tab_control_mode))) {
                        FlyController flyController = this.mFlyController;
                        i = (flyController == null || !flyController.isControlMode()) ? C0549R.mipmap.icon_control_model_off : C0549R.mipmap.icon_control_model_on;
                    } else if (next.equals(getString(C0549R.string.tab_gyroscope_correction))) {
                        i = C0549R.drawable.drawable_gyr_icon;
                    } else if (next.equals(getString(C0549R.string.tab_turn_180))) {
                        i = C0549R.drawable.drawable_turn_screen_icon;
                    } else if (next.equals(getString(C0549R.string.tab_media_library))) {
                        i = C0549R.drawable.drawable_icon_media;
                    } else if (next.equals(getString(C0549R.string.tab_down_menu))) {
                        i = C0549R.drawable.drawable_down_menu;
                    } else if (next.equals(getString(C0549R.string.tab_fixed_height))) {
                        FlyController flyController2 = this.mFlyController;
                        i = (flyController2 == null || !flyController2.isFixedHeightMode()) ? C0549R.drawable.drawable_fixed_height : C0549R.drawable.drawable_fixed_height_yellow;
                    } else {
                        i = next.equals(getString(C0549R.string.tab_turn_back)) ? C0549R.mipmap.play_back_icon : -1;
                    }
                    if (i != -1) {
                        map.put(next, Integer.valueOf(i));
                    }
                }
            }
            TopListViewAdapter topListViewAdapter = new TopListViewAdapter(getContext(), this.topList, map);
            this.adapter = topListViewAdapter;
            this.topListView.setAdapter((ListAdapter) topListViewAdapter);
        }
    }

    private void updateAdjustBar(MyScaleView myScaleView, int i) throws IllegalStateException {
        boolean z = PreferencesHelper.getSharedPreferences(getContext()).getBoolean(IConstants.FINE_TUNE_PARAMS, false);
        if (myScaleView == null || z) {
            return;
        }
        int currentValue = myScaleView.getCurrentValue();
        int maxValue = myScaleView.getMaxValue();
        boolean z2 = true;
        if (i != 0) {
            if (i == 1 && currentValue < maxValue) {
                currentValue++;
            }
        } else if (currentValue > 0) {
            currentValue--;
        }
        if (currentValue != 12 && currentValue != 0 && currentValue != 24) {
            z2 = false;
        }
        SoundUtils.didiSound(z2, getActivity());
        myScaleView.setCurrentValue(currentValue);
        if (myScaleView == this.horizontalBar) {
            if (currentValue < 12) {
                this.horizontalCurrentValue = -(12 - currentValue);
                return;
            } else if (currentValue > 12) {
                this.horizontalCurrentValue = currentValue - 12;
                return;
            } else {
                this.horizontalCurrentValue = 0;
                return;
            }
        }
        if (myScaleView == this.horizontalCenterBar) {
            if (currentValue < 12) {
                this.horizontalCenterCurrentValue = -(12 - currentValue);
                return;
            } else if (currentValue > 12) {
                this.horizontalCenterCurrentValue = currentValue - 12;
                return;
            } else {
                this.horizontalCenterCurrentValue = 0;
                return;
            }
        }
        if (myScaleView == this.verticalBar) {
            if (currentValue < 12) {
                this.verticalCurrentValue = 12 - currentValue;
            } else if (currentValue > 12) {
                this.verticalCurrentValue = -(currentValue - 12);
            } else {
                this.verticalCurrentValue = 0;
            }
        }
    }

    private void updateModeUI() {
        boolean zIsControlMode = this.mFlyController.isControlMode();
        this.adapter.setControlMode(!r1.isControlMode());
        this.adapter.notifyDataSetChanged();
        if (!zIsControlMode) {
            if (this.LayoutControlMode.getVisibility() != 0) {
                this.LayoutControlMode.setVisibility(0);
            }
            if (!this.mFlyController.isFixedHeightMode()) {
                this.rvVertical.setFixHeight(false);
                this.mFlyController.setControlAccelerator(0);
            } else {
                FlyController flyController = this.mFlyController;
                Objects.requireNonNull(flyController);
                flyController.setControlAccelerator(128);
            }
            this.mFlyController.setController(true);
            return;
        }
        if (this.LayoutControlMode.getVisibility() != 8) {
            this.LayoutControlMode.setVisibility(8);
        }
        ViewPropertyAnimator viewPropertyAnimator = this.animator;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
        }
        this.mFlyController.setController(false);
    }

    /* renamed from: com.yls.nova.fragment.DeviceBLFragment$3 */
    class C05703 implements PicDataCallback {
        C05703() {
        }

        @Override // com.cooingdv.bl60xmjpeg.callback.PicDataCallback
        public void onData(byte[] bArr, long j, byte b) throws InterruptedException {
            if (DeviceBLFragment.this.isSupport) {
                if (DeviceBLFragment.this.noCardTaking) {
                    DeviceBLFragment.this.noCardTaking = false;
                    if (!TextUtils.isEmpty(DeviceBLFragment.this.savePhotoPath) && !TextUtils.isEmpty(DeviceBLFragment.this.savePhotoName)) {
                        DeviceBLFragment.this.mjpegThread.savePicture(DeviceBLFragment.this.savePhotoPath + File.separator + DeviceBLFragment.this.savePhotoName);
                    }
                }
                if (DeviceBLFragment.this.mjpegView.getVisibility() != 0) {
                    DeviceBLFragment.this.mHandler.post(new Runnable() { // from class: com.yls.nova.fragment.DeviceBLFragment$3$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.m540lambda$onData$0$comylsnovafragmentDeviceBLFragment$3();
                        }
                    });
                }
                if (DeviceBLFragment.this.isVarMode && DeviceBLFragment.this.mjpegView2.getVisibility() != 0) {
                    DeviceBLFragment.this.mHandler.post(new Runnable() { // from class: com.yls.nova.fragment.DeviceBLFragment$3$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.m541lambda$onData$1$comylsnovafragmentDeviceBLFragment$3();
                        }
                    });
                }
                if (DeviceBLFragment.this.mjpegThread != null) {
                    DeviceBLFragment.this.mjpegThread.drawBitmap(bArr);
                }
                if (DeviceBLFragment.this.mFlyController.isGestureMode()) {
                    DeviceBLFragment.this.mVideoData = bArr;
                    DeviceBLFragment.this.gestureRecognitionTimer();
                }
            }
        }

        /* renamed from: lambda$onData$0$com-yls-nova-fragment-DeviceBLFragment$3, reason: not valid java name */
        /* synthetic */ void m540lambda$onData$0$comylsnovafragmentDeviceBLFragment$3() {
            DeviceBLFragment.this.mjpegView.setVisibility(0);
            DeviceBLFragment.this.ivBg.setVisibility(8);
        }

        /* renamed from: lambda$onData$1$com-yls-nova-fragment-DeviceBLFragment$3, reason: not valid java name */
        /* synthetic */ void m541lambda$onData$1$comylsnovafragmentDeviceBLFragment$3() {
            DeviceBLFragment.this.mjpegView2.setVisibility(0);
            DeviceBLFragment.this.ivBg2.setVisibility(8);
        }

        @Override // com.cooingdv.bl60xmjpeg.callback.PicDataCallback
        public void onReceiver(byte[] bArr) {
            int iIntValue;
            if (DeviceBLFragment.this.mStreamClient.getDeviceType() == 2) {
                iIntValue = Integer.valueOf(BufChangeHex.byteToHex(bArr[1]), 16).intValue();
            } else {
                iIntValue = Integer.valueOf(BufChangeHex.byteToHex(bArr[0]), 16).intValue();
            }
            if (iIntValue == 77) {
                DeviceBLFragment.this.mHandler.post(new Runnable() { // from class: com.yls.nova.fragment.DeviceBLFragment.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        DeviceBLFragment.this.topListView.performItemClick(DeviceBLFragment.this.topListView.getChildAt(1), 1, DeviceBLFragment.this.topListView.getItemIdAtPosition(1));
                    }
                });
                return;
            }
            if (iIntValue == 88) {
                DeviceBLFragment.this.mHandler.post(new Runnable() { // from class: com.yls.nova.fragment.DeviceBLFragment.3.2
                    @Override // java.lang.Runnable
                    public void run() {
                        DeviceBLFragment.this.topListView.performItemClick(DeviceBLFragment.this.topListView.getChildAt(2), 2, DeviceBLFragment.this.topListView.getItemIdAtPosition(2));
                    }
                });
                return;
            }
            DeviceBLFragment.this.mStreamClient.setResolutionNumber(iIntValue);
            if (DeviceBLFragment.this.getActivity() != null) {
                DeviceBLFragment.this.setResolution(iIntValue);
            }
        }
    }

    /* renamed from: com.yls.nova.fragment.DeviceBLFragment$4 */
    class C05714 implements OnSocketListener {
        C05714() {
        }

        @Override // com.yls.nova.interfaces.OnSocketListener
        public void onConnected() {
            DeviceBLFragment.this.numPhoto = 0;
            DeviceBLFragment.this.numVideo = 0;
        }

        @Override // com.yls.nova.interfaces.OnSocketListener
        public void onVideo(byte[] bArr) throws InterruptedException {
            if (DeviceBLFragment.this.isSupport) {
                if (DeviceBLFragment.this.noCardTaking) {
                    DeviceBLFragment.this.noCardTaking = false;
                    if (!TextUtils.isEmpty(DeviceBLFragment.this.savePhotoPath) && !TextUtils.isEmpty(DeviceBLFragment.this.savePhotoName)) {
                        DeviceBLFragment.this.mjpegThread.savePicture(DeviceBLFragment.this.savePhotoPath + File.separator + DeviceBLFragment.this.savePhotoName);
                    }
                }
                if (DeviceBLFragment.this.mjpegView.getVisibility() != 0) {
                    DeviceBLFragment.this.mHandler.post(new Runnable() { // from class: com.yls.nova.fragment.DeviceBLFragment$4$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.m546lambda$onVideo$0$comylsnovafragmentDeviceBLFragment$4();
                        }
                    });
                }
                if (DeviceBLFragment.this.isVarMode && DeviceBLFragment.this.mjpegView2.getVisibility() != 0) {
                    DeviceBLFragment.this.mHandler.post(new Runnable() { // from class: com.yls.nova.fragment.DeviceBLFragment$4$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.m547lambda$onVideo$1$comylsnovafragmentDeviceBLFragment$4();
                        }
                    });
                }
                if (DeviceBLFragment.this.mjpegThread != null) {
                    DeviceBLFragment.this.mjpegThread.drawBitmap(bArr);
                }
                if (DeviceBLFragment.this.mFlyController.isGestureMode()) {
                    DeviceBLFragment.this.mVideoData = bArr;
                    DeviceBLFragment.this.gestureRecognitionTimer();
                }
            }
        }

        /* renamed from: lambda$onVideo$0$com-yls-nova-fragment-DeviceBLFragment$4, reason: not valid java name */
        /* synthetic */ void m546lambda$onVideo$0$comylsnovafragmentDeviceBLFragment$4() {
            DeviceBLFragment.this.mjpegView.setVisibility(0);
            DeviceBLFragment.this.ivBg.setVisibility(8);
        }

        /* renamed from: lambda$onVideo$1$com-yls-nova-fragment-DeviceBLFragment$4, reason: not valid java name */
        /* synthetic */ void m547lambda$onVideo$1$comylsnovafragmentDeviceBLFragment$4() {
            DeviceBLFragment.this.mjpegView2.setVisibility(0);
            DeviceBLFragment.this.ivBg2.setVisibility(8);
        }

        @Override // com.yls.nova.interfaces.OnSocketListener
        public void onReceiver(byte[] bArr) throws NumberFormatException {
            if (bArr.length >= 1) {
                DeviceBLFragment.this.setResolution(bArr[0]);
            }
            if (bArr.length > 4) {
                byte b = bArr[2];
                if (b == 77) {
                    int i = Integer.parseInt(BufChangeHex.byteToHex(bArr[3]), 16);
                    if (i > DeviceBLFragment.this.numPhoto || (DeviceBLFragment.this.numPhoto == 255 && i < DeviceBLFragment.this.numPhoto)) {
                        DeviceBLFragment.this.mHandler.post(new Runnable() { // from class: com.yls.nova.fragment.DeviceBLFragment$4$$ExternalSyntheticLambda2
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.m542lambda$onReceiver$2$comylsnovafragmentDeviceBLFragment$4();
                            }
                        });
                        DeviceBLFragment.this.numPhoto = i;
                        SocketClient.getInstance().debugSend(new byte[]{9, 1});
                        return;
                    }
                    return;
                }
                if (b == 88) {
                    int i2 = Integer.parseInt(BufChangeHex.byteToHex(bArr[4]), 16);
                    if (i2 > DeviceBLFragment.this.numVideo || (DeviceBLFragment.this.numVideo == 255 && i2 < DeviceBLFragment.this.numVideo)) {
                        DeviceBLFragment.this.mHandler.post(new Runnable() { // from class: com.yls.nova.fragment.DeviceBLFragment$4$$ExternalSyntheticLambda3
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.m543lambda$onReceiver$3$comylsnovafragmentDeviceBLFragment$4();
                            }
                        });
                        DeviceBLFragment.this.numVideo = i2;
                        SocketClient.getInstance().debugSend(new byte[]{9, 2});
                        return;
                    }
                    return;
                }
                return;
            }
            if (bArr.length >= 3) {
                byte b2 = bArr[2];
                if (b2 == 77) {
                    DeviceBLFragment.this.mHandler.post(new Runnable() { // from class: com.yls.nova.fragment.DeviceBLFragment$4$$ExternalSyntheticLambda4
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.m544lambda$onReceiver$4$comylsnovafragmentDeviceBLFragment$4();
                        }
                    });
                } else if (b2 == 88) {
                    DeviceBLFragment.this.mHandler.post(new Runnable() { // from class: com.yls.nova.fragment.DeviceBLFragment$4$$ExternalSyntheticLambda5
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.m545lambda$onReceiver$5$comylsnovafragmentDeviceBLFragment$4();
                        }
                    });
                }
            }
        }

        /* renamed from: lambda$onReceiver$2$com-yls-nova-fragment-DeviceBLFragment$4, reason: not valid java name */
        /* synthetic */ void m542lambda$onReceiver$2$comylsnovafragmentDeviceBLFragment$4() {
            DeviceBLFragment.this.topListView.performItemClick(DeviceBLFragment.this.topListView.getChildAt(1), 1, DeviceBLFragment.this.topListView.getItemIdAtPosition(1));
        }

        /* renamed from: lambda$onReceiver$3$com-yls-nova-fragment-DeviceBLFragment$4, reason: not valid java name */
        /* synthetic */ void m543lambda$onReceiver$3$comylsnovafragmentDeviceBLFragment$4() {
            DeviceBLFragment.this.topListView.performItemClick(DeviceBLFragment.this.topListView.getChildAt(2), 2, DeviceBLFragment.this.topListView.getItemIdAtPosition(2));
        }

        /* renamed from: lambda$onReceiver$4$com-yls-nova-fragment-DeviceBLFragment$4, reason: not valid java name */
        /* synthetic */ void m544lambda$onReceiver$4$comylsnovafragmentDeviceBLFragment$4() {
            DeviceBLFragment.this.topListView.performItemClick(DeviceBLFragment.this.topListView.getChildAt(1), 1, DeviceBLFragment.this.topListView.getItemIdAtPosition(1));
        }

        /* renamed from: lambda$onReceiver$5$com-yls-nova-fragment-DeviceBLFragment$4, reason: not valid java name */
        /* synthetic */ void m545lambda$onReceiver$5$comylsnovafragmentDeviceBLFragment$4() {
            DeviceBLFragment.this.topListView.performItemClick(DeviceBLFragment.this.topListView.getChildAt(2), 2, DeviceBLFragment.this.topListView.getItemIdAtPosition(2));
        }
    }

    private void showRecordingUI() {
        if (getActivity() == null || this.myTimer != null) {
            return;
        }
        MyTimer myTimer = new MyTimer(this.mHandler);
        this.myTimer = myTimer;
        myTimer.setTimer(this.deviceVideoTime);
        this.myTimer.start();
    }

    private void HideRecordingUI() {
        MyTimer myTimer;
        if (getActivity() == null || (myTimer = this.myTimer) == null) {
            return;
        }
        myTimer.stopTimer();
        this.deviceVideoTime = 0;
        this.myTimer = null;
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        if (this.noCardRecording) {
            stopNoCardRecording();
        }
        ViewPropertyAnimator viewPropertyAnimator = this.animator;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
        }
    }

    private void startNoCardRecording() throws IOException {
        if (this.mjpegView.getVisibility() != 0) {
            showShortToast(C0549R.string.connect_device_tip);
            return;
        }
        initRecordStreamer(20, this.mjpegThread.getContrastCompressWidth(), this.mjpegThread.getContrastCompressHeight());
        VideoModel videoModel = this.videoModel;
        if (videoModel == null || videoModel.isRecording()) {
            return;
        }
        if ("mounted".equals(Environment.getExternalStorageState())) {
            long sdCardFreeBytes = StorageUtil.getSdCardFreeBytes();
            if (sdCardFreeBytes <= IConstants.MIN_STORAGE_SPACE) {
                showLongToast(C0549R.string.phone_space_inefficient);
                return;
            }
            if (sdCardFreeBytes < this.noCardRecordMax * DEFAULT_VIDEO_SIZE) {
                this.noCardRecordMax = (int) (((sdCardFreeBytes / 5) * 4) / DEFAULT_VIDEO_SIZE);
            }
            showRecordingUI();
            Dbug.m416e(this.TAG, "startNoCardRecording startRecording=");
            this.videoModel.startRecorder();
            this.mjpegThread.setModel(this.videoModel);
            this.noCardRecording = true;
            this.adapter.setRecordVideo(true);
            this.adapter.notifyDataSetChanged();
            return;
        }
        showLongToast(C0549R.string.not_found_phone_sdcard);
    }

    private void stopNoCardRecording() {
        if (this.videoModel != null) {
            TopListViewAdapter topListViewAdapter = this.adapter;
            if (topListViewAdapter != null) {
                topListViewAdapter.setRecordVideo(false);
                this.adapter.notifyDataSetChanged();
            }
            VideoModel videoModel = this.videoModel;
            if (videoModel != null && videoModel.isRecording()) {
                this.videoModel.stopRecorder();
            }
            this.noCardRecording = false;
        }
        HideRecordingUI();
    }

    private void startNoCardTaking() throws IllegalStateException {
        if (this.mjpegView.getVisibility() != 0) {
            showShortToast(C0549R.string.connect_device_tip);
            return;
        }
        if (!"mounted".equals(Environment.getExternalStorageState())) {
            showLongToast(C0549R.string.not_found_phone_sdcard);
            return;
        }
        if (StorageUtil.getSdCardFreeBytes() <= IConstants.MIN_STORAGE_SPACE) {
            showLongToast(C0549R.string.phone_space_inefficient);
            return;
        }
        String photoPath = AppUtils.getPhotoPath(getActivity());
        if (TextUtils.isEmpty(photoPath)) {
            Dbug.m416e(this.TAG, "Record dir is null");
            return;
        }
        File file = new File(photoPath);
        if (!file.exists() && !file.mkdir()) {
            throw new IllegalAccessError("Create " + photoPath + " failure.");
        }
        this.savePhotoPath = photoPath;
        this.savePhotoName = "JPG_" + TimeFormater.formatYMD_HMS(System.currentTimeMillis()) + ".jpg";
        SoundUtils.shootSound(getActivity());
        this.noCardTaking = true;
    }

    private void openGesture() {
        FlyController flyController = this.mFlyController;
        if (flyController != null) {
            flyController.setGestureMode(!flyController.isGestureMode());
            if (this.mFlyController.isGestureMode()) {
                this.btnGesture.setImageResource(C0549R.drawable.drawable_gesture_recognition_open);
                this.layoutVideoView.setVisibility(0);
                return;
            }
            this.btnGesture.setImageResource(C0549R.drawable.drawable_gesture_recognition_close);
            this.layoutVideoView.setVisibility(8);
            this.mVideoData = null;
            Timer timer = this.gestureTimer;
            if (timer != null) {
                timer.cancel();
                this.gestureTimer = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void gestureRecognitionTimer() {
        if (this.gestureTimer == null) {
            Timer timer = new Timer();
            this.gestureTimer = timer;
            timer.schedule(new TimerTask() { // from class: com.yls.nova.fragment.DeviceBLFragment.5
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    if (DeviceBLFragment.this.mFlyController.isGestureMode()) {
                        String documentsPath = AppUtils.getDocumentsPath(DeviceBLFragment.this.getActivity());
                        if (DeviceBLFragment.this.isRecognition || DeviceBLFragment.this.mVideoData == null || DeviceBLFragment.this.mVideoData.length <= 0 || !BufChangeHex.byte2File(DeviceBLFragment.this.mVideoData, documentsPath, IConstants.GESTRUE_FILE_NAME)) {
                            return;
                        }
                        if (DeviceBLFragment.this.mjpegThread.getTurnBitmap()) {
                            String str = documentsPath + File.separator + IConstants.GESTRUE_FILE_NAME;
                            BitmapUtil.saveBitmap(str, BitmapUtil.rotateBitmap(BitmapUtil.getImage(str, false), -180.0f));
                        }
                        int[] iArrGestureRecognition = RecognitionUtils.gestureRecognition();
                        int i = (iArrGestureRecognition == null || iArrGestureRecognition.length <= 0) ? 0 : iArrGestureRecognition[0];
                        if (i == 2 || DeviceBLFragment.this.palmCount > 0) {
                            DeviceBLFragment.access$2908(DeviceBLFragment.this);
                        }
                        if (i == 1 || (i == 2 && DeviceBLFragment.this.palmCount > 1)) {
                            Message messageObtainMessage = DeviceBLFragment.this.mHandler.obtainMessage();
                            messageObtainMessage.what = DeviceBLFragment.MSG_GESTURE_TOAST;
                            messageObtainMessage.obj = iArrGestureRecognition;
                            DeviceBLFragment.this.mHandler.sendMessage(messageObtainMessage);
                            DeviceBLFragment.this.palmCount = 0;
                        }
                        if (DeviceBLFragment.this.palmCount > 2) {
                            DeviceBLFragment.this.palmCount = 0;
                        }
                        Dbug.m416e(DeviceBLFragment.this.TAG, "gesture values = " + String.valueOf(i));
                    }
                }
            }, 1000L, 1000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void gestureRecogFrame(int[] iArr) {
        ImageView imageView = new ImageView(getActivity());
        imageView.setImageResource(C0549R.mipmap.gesture_recognition_confirm);
        int width = this.layoutVideoView.getWidth();
        float height = (this.layoutVideoView.getHeight() * 1.0f) / this.mjpegThread.getContrastCompressHeight();
        int i = iArr[1];
        int i2 = iArr[2];
        int i3 = iArr[3];
        int i4 = iArr[4];
        imageView.setX((int) (i * r1));
        imageView.setY((int) (i2 * height));
        this.layoutVideoView.addView(imageView, (int) (i3 * ((width * 1.0f) / this.mjpegThread.getContrastCompressWidth())), (int) (i4 * height));
    }

    private void initRecordStreamer(int i, int i2, int i3) throws IOException {
        String videoPath = AppUtils.getVideoPath(getActivity());
        if (TextUtils.isEmpty(videoPath)) {
            Dbug.m416e(this.TAG, "Record dir is null");
            return;
        }
        File file = new File(videoPath);
        if (!file.exists() && !file.mkdir()) {
            Dbug.m416e(this.TAG, "mkdir Record dir failed!");
            return;
        }
        Dbug.m417i(this.TAG, "initRecordStreamer dirPath = " + videoPath);
        if (this.videoModel != null) {
            this.mjpegThread.setFps(i);
            int i4 = this.fakeResolutionType;
            if (i4 == 1) {
                i2 = 640;
                i3 = 480;
            } else if (i4 != 2) {
                if (i4 != 3) {
                    i2 = (i4 == 4 || i4 == 5) ? 2048 : 1920;
                }
                i3 = 1080;
            } else {
                i2 = 1280;
                i3 = 720;
            }
            this.videoModel.init(i2, i3, i);
            Dbug.m417i(this.TAG, "video model init : width = " + i2 + ", height = " + i3);
        }
    }

    public void initFixHeight() {
        boolean z = PreferencesHelper.getSharedPreferences(getActivity()).getBoolean(IConstants.FIX_HEIGHT_SHARE_KEY, true);
        FlyController flyController = this.mFlyController;
        if (flyController != null) {
            flyController.setFixedHeightMode(z);
            if (z) {
                this.rvVertical.setFixHeight(true);
                updateFixHeightUI(true);
                FlyController flyController2 = this.mFlyController;
                Objects.requireNonNull(flyController2);
                flyController2.setControlAccelerator(128);
                this.adapter.setFixedHeightMode(true);
                this.adapter.notifyDataSetChanged();
                return;
            }
            this.rvVertical.setFixHeight(false);
            updateFixHeightUI(false);
            this.mFlyController.setControlAccelerator(0);
            this.adapter.setFixedHeightMode(false);
            this.adapter.notifyDataSetChanged();
        }
    }

    public void initScreen() {
        boolean z = PreferencesHelper.getSharedPreferences(getActivity()).getBoolean(IConstants.SCREEN_DIRECTION_SHARE_KEY, true);
        MjpegThread mjpegThread = this.mjpegThread;
        if (mjpegThread != null) {
            mjpegThread.setTurnBitmap(z);
        }
        this.adapter.setTurn180(z);
        this.adapter.notifyDataSetChanged();
        if (this.isVarMode) {
            this.ivBg2.setVisibility(0);
        } else {
            this.mjpegView2.setVisibility(8);
            this.ivBg2.setVisibility(8);
        }
        saveGestureXml();
    }

    private void saveGestureXml() {
        new Thread(new Runnable() { // from class: com.yls.nova.fragment.DeviceBLFragment.6
            @Override // java.lang.Runnable
            public void run() throws Resources.NotFoundException, IOException {
                StorageUtil.copyFilesFromRaw(DeviceBLFragment.this.getActivity(), C0549R.raw.fist, "fist.xml", AppUtils.getDocumentsPath(DeviceBLFragment.this.getActivity()));
                StorageUtil.copyFilesFromRaw(DeviceBLFragment.this.getActivity(), C0549R.raw.rpalm, "rpalm.xml", AppUtils.getDocumentsPath(DeviceBLFragment.this.getActivity()));
            }
        }).start();
    }

    public void initStatus() {
        initScreen();
        initFixHeight();
    }

    @Override // com.yls.nova.libs.TrackView.OnTrackListener
    public void onTrackStart(List<Point> list, List<Point> list2) {
        ImageView imageView = this.air;
        if (imageView != null) {
            imageView.setVisibility(8);
            this.air = null;
        }
        if (list.size() > 2) {
            ImageView imageView2 = new ImageView(getActivity());
            this.air = imageView2;
            imageView2.setImageResource(C0549R.mipmap.aircraft);
            this.airWidth = this.air.getDrawable().getIntrinsicWidth();
            this.airHeight = this.air.getDrawable().getIntrinsicHeight();
            this.air.setX(list.get(0).x - (this.airWidth / 2));
            this.air.setY(list.get(0).y - (this.airHeight / 2));
            this.layoutControlRightDraw.addView(this.air);
            this.animator = this.air.animate();
            startTrackAnimation(list, list2);
        }
    }

    @Override // com.yls.nova.libs.TrackView.OnTrackListener
    public void onTrackStop() {
        ViewPropertyAnimator viewPropertyAnimator = this.animator;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
        }
    }

    private void startTrackAnimation(List<Point> list, List<Point> list2) {
        this.animator.translationX(list.get(0).x - (this.airWidth / 2)).translationY(list.get(0).y - (this.airHeight / 2)).setInterpolator(new LinearInterpolator()).setDuration(500L).setListener(new TrackAnimationListener(list, list2, this.air, this.trackView, this.airWidth, this.airHeight, this));
    }

    @Override // com.yls.nova.tools.TrackAnimationListener.AnimatorTrackListener
    public void trackFly(float f, float f2) {
        int i;
        float halfLen = getHalfLen();
        int i2 = (int) ((f / 214.0f) * halfLen);
        int i3 = (int) ((f2 / 214.0f) * halfLen);
        int i4 = this.currPower;
        if (i4 == 4) {
            int i5 = i2 + (this.horizontalCenterCurrentValue * 2);
            Objects.requireNonNull(this.mFlyController);
            i2 = i5 + 128;
            i = i3 + (this.verticalCurrentValue * 2);
            Objects.requireNonNull(this.mFlyController);
        } else if (i4 == 9) {
            int i6 = i2 + (this.horizontalCenterCurrentValue * 3);
            Objects.requireNonNull(this.mFlyController);
            i2 = i6 + 128;
            i = i3 + (this.verticalCurrentValue * 3);
            Objects.requireNonNull(this.mFlyController);
        } else {
            if (i4 == 14) {
                int i7 = i2 + (this.horizontalCenterCurrentValue * 4);
                Objects.requireNonNull(this.mFlyController);
                i2 = i7 + 128;
                i = i3 + (this.verticalCurrentValue * 4);
                Objects.requireNonNull(this.mFlyController);
            }
            this.mFlyController.setControlByte1(i2);
            this.mFlyController.setControlByte2(i3);
        }
        i3 = i + 128;
        this.mFlyController.setControlByte1(i2);
        this.mFlyController.setControlByte2(i3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setResolution(int i) {
        this.isSupport = WifiIdUtils.isSupport(i);
        int fakeResolution = WifiIdUtils.getFakeResolution(i);
        this.fakeResolutionType = fakeResolution;
        if ((fakeResolution == 4 || fakeResolution == 5) && AppUtils.isNotSupport4K()) {
            this.fakeResolutionType = 3;
        }
        MjpegThread mjpegThread = this.mjpegThread;
        if (mjpegThread != null) {
            mjpegThread.setFakeResolution(this.fakeResolutionType);
        }
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        if (i <= 1) {
            this.tvScale.setVisibility(4);
            this.lastScaleY = 0.0f;
            this.lastScaleX = 0.0f;
        } else {
            this.tvScale.setVisibility(0);
            this.tvScale.setText(String.valueOf(i) + "X");
        }
        if (i == 50) {
            i = 49;
        }
        this.mjpegThread.setFocusScale(i);
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.getId() == C0549R.id.device_video_view) {
            int action = motionEvent.getAction();
            if (action == 0) {
                this.startX = motionEvent.getX();
                this.startY = motionEvent.getY();
            } else if (action == 1) {
                this.lastScaleX += motionEvent.getX() - this.startX;
                this.lastScaleY += motionEvent.getY() - this.startY;
            } else if (action == 2) {
                this.mjpegThread.setFocusMove((motionEvent.getX() - this.startX) + this.lastScaleX, (motionEvent.getY() - this.startY) + this.lastScaleY);
            }
        }
        return true;
    }
}
