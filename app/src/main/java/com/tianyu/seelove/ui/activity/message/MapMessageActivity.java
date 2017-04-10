package com.tianyu.seelove.ui.activity.message;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiResult;
import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.SearchPoiAdapter;
import com.tianyu.seelove.common.Actions;
import com.tianyu.seelove.common.Constant;
import com.tianyu.seelove.dao.UserDao;
import com.tianyu.seelove.dao.impl.SessionDaoImpl;
import com.tianyu.seelove.dao.impl.UserDaoImpl;
import com.tianyu.seelove.injection.ControlInjection;
import com.tianyu.seelove.manager.DirectoryManager;
import com.tianyu.seelove.model.entity.message.LocationBean;
import com.tianyu.seelove.model.entity.message.SLLocationMessage;
import com.tianyu.seelove.model.entity.message.SLMessage;
import com.tianyu.seelove.model.entity.message.SLSession;
import com.tianyu.seelove.model.enums.SessionType;
import com.tianyu.seelove.task.InsertMessageTask;
import com.tianyu.seelove.task.base.BaseTask;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.BaiduMapUtilByRacer;
import com.tianyu.seelove.utils.DensityUtil;
import com.tianyu.seelove.utils.LogUtil;
import com.tianyu.seelove.utils.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author shisheng.zhao
 * @Description: 位置消息Activity
 * @date 2016-01-04 下午14:47:22
 */
public class MapMessageActivity extends BaseActivity {
    private static Context mContext;
    @ControlInjection(R.id.topLayout)
    private RelativeLayout topLayout;
    @ControlInjection(R.id.titleView)
    private TextView titleView;
    @ControlInjection(R.id.ibMLLocate)
    private ImageButton ibMLLocate;
    @ControlInjection(R.id.etMLCityPoi)
    private EditText etMLCityPoi;
    @ControlInjection(R.id.tvShowLocation)
    private TextView tvShowLocation;
    @ControlInjection(R.id.lvPoiList)
    private ListView lvAroundPoi;
    @ControlInjection(R.id.lvMLCityPoi)
    private ListView lvSearchPoi;
    @ControlInjection(R.id.ivMLPLoading)
    private ImageView ivMLPLoading;
    @ControlInjection(R.id.btMapZoomIn)
    private Button btMapZoomIn;
    @ControlInjection(R.id.btMapZoomOut)
    private Button btMapZoomOut;
    @ControlInjection(R.id.llMLMain)
    private LinearLayout llMLMain;
    @ControlInjection(R.id.mMapView)
    private MapView mMapView;
    private LocationBean mLocationBean;
    // 定位poi地名信息数据源
    private List<PoiInfo> aroundPoiList;
    private AroundPoiAdapter mAroundPoiAdapter;
    // 搜索模块，也可去掉地图模块独立使用
    private Marker mMarker = null;
    // 搜索当前城市poi数据源
    private static List<LocationBean> searchPoiList;
    private SearchPoiAdapter mSearchPoiAdapter;
    private BaiduMap mBaiduMap;
    // 标识
    public static final int SHOW_MAP = 0;
    private static final int SHOW_SEARCH_RESULT = 1;
    // 延时多少秒diss掉dialog
    private static final int DELAY_DISMISS = 1000 * 30;
    private PoiInfo poiInfo = null;
    private String target, targetGroup;
    private UserDao userDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_position);
        this.mContext = this;
        userDao = new UserDaoImpl();
        target = getIntent().getExtras().getString("target");
        targetGroup = getIntent().getExtras().getString("targetGroup");
        Constant.deviceWidthHeight = DensityUtil.getDeviceInfo(this);
        initView();
        locate();
        iniEvent();
    }

    private void initView() {
        titleView.setText(getString(R.string.meet_fellowship_position));
        // 地图初始化
        BaiduMapUtilByRacer.goneMapViewChild(mMapView, true, true);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
        mBaiduMap.setOnMapStatusChangeListener(mapStatusChangeListener);
        mBaiduMap.setOnMapClickListener(mapOnClickListener);
        mBaiduMap.getUiSettings().setZoomGesturesEnabled(false);// 缩放手势
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
    }

    // 显示地图界面亦或搜索结果界面
    private void showMapOrSearch(int index) {
        if (index == SHOW_SEARCH_RESULT) {
            llMLMain.setVisibility(View.GONE);
            lvSearchPoi.setVisibility(View.VISIBLE);
        } else {
            lvSearchPoi.setVisibility(View.GONE);
            llMLMain.setVisibility(View.VISIBLE);
            if (searchPoiList != null) {
                searchPoiList.clear();
            }
        }
    }

    public void locate() {
        BaiduMapUtilByRacer.locateByBaiduMap(mContext, 2000,
                new BaiduMapUtilByRacer.LocateListener() {

                    @Override
                    public void onLocateSucceed(LocationBean locationBean) {
                        mLocationBean = locationBean;
                        if (mMarker != null) {
                            mMarker.remove();
                        } else {
                            mBaiduMap.clear();
                        }
                        mMarker = BaiduMapUtilByRacer.showMarkerByResource(
                                locationBean.getLatitude(),
                                locationBean.getLongitude(), R.mipmap.point,
                                mBaiduMap, 0, true);
                    }

                    @Override
                    public void onLocateFiled() {

                    }

                    @Override
                    public void onLocating() {
                    }
                });
    }

    public void getPoiByPoiSearch() {
        BaiduMapUtilByRacer.getPoiByPoiSearch(mLocationBean.getCity(),
                etMLCityPoi.getText().toString().trim(), 0,
                new BaiduMapUtilByRacer.PoiSearchListener() {

                    @Override
                    public void onGetSucceed(List<LocationBean> locationList,
                                             PoiResult res) {
                        if (etMLCityPoi.getText().toString().trim().length() > 0) {
                            if (searchPoiList == null) {
                                searchPoiList = new ArrayList<LocationBean>();
                            }
                            searchPoiList.clear();
                            searchPoiList.addAll(locationList);
                            updateCityPoiListAdapter();
                        }
                    }

                    @Override
                    public void onGetFailed() {
                        Toast.makeText(mContext, getString(R.string.search_no_result),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void reverseGeoCode(LatLng ll, final boolean isShowTextView) {
        BaiduMapUtilByRacer.getPoisByGeoCode(ll.latitude, ll.longitude,
                new BaiduMapUtilByRacer.GeoCodePoiListener() {

                    @Override
                    public void onGetSucceed(LocationBean locationBean,
                                             List<PoiInfo> poiList) {
                        mLocationBean = (LocationBean) locationBean.clone();
                        // mBaiduMap.setMapStatus(MapStatusUpdateFactory
                        // .newLatLng(new LatLng(locationBean
                        // .getLatitude(), locationBean
                        // .getLongitude())));
                        if (isShowTextView) {
                            tvShowLocation.setText(locationBean.getLocName());
                        }
                        if (aroundPoiList == null) {
                            aroundPoiList = new ArrayList<PoiInfo>();
                        }
                        aroundPoiList.clear();
                        if (poiList != null) {
                            aroundPoiList.addAll(poiList);
                        } else {
                            Toast.makeText(mContext, getString(R.string.arroud_no_point), Toast.LENGTH_SHORT).show();
                        }
                        updatePoiListAdapter(aroundPoiList, -1);
                    }

                    @Override
                    public void onGetFailed() {
                        Toast.makeText(mContext, getString(R.string.search_no_result),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void iniEvent() {
        titleView.setOnClickListener(new OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             if (null != poiInfo) {
                                                 // 发送位置逻辑
                                                 mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
                                                     @Override
                                                     public void onSnapshotReady(Bitmap bitmap) {
                                                         String savePath = DirectoryManager.getDirectory(DirectoryManager.DIR.IMAGE) +
                                                                 StringUtils.generateGUID()
                                                                 + ".jpg";
                                                         File file = new File(savePath);
                                                         FileOutputStream out;
                                                         try {
                                                             out = new FileOutputStream(file);
                                                             // 获取状态栏高度
                                                             Rect frame = new Rect();
                                                             MapMessageActivity.this.getWindow().getDecorView()
                                                                     .getWindowVisibleDisplayFrame(frame);
                                                             int statusBarHeight = frame.top;
                                                             int x = Constant.deviceWidthHeight[0] / 2 - 152;
                                                             int y = statusBarHeight + topLayout.getHeight() + 101;
                                                             bitmap = Bitmap.createBitmap(bitmap, x, y, 304, 202);
                                                             if (bitmap.compress(Bitmap.CompressFormat.PNG, 100,
                                                                     out)) {
                                                                 out.flush();
                                                                 out.close();
                                                             }
                                                             final long lastId = System.currentTimeMillis();
                                                             SLLocationMessage locationMessage = new SLLocationMessage();
                                                             locationMessage.setMessageId(String.valueOf(lastId));
                                                             locationMessage.setMessageContent(savePath);
                                                             locationMessage.setUserFrom(AppUtils.getInstance().getUserId());
                                                             locationMessage.setUserTo(target);
                                                             locationMessage.setIsRead(SLMessage.msgRead);
                                                             locationMessage.setTimestamp(new Date().getTime());
                                                             locationMessage.setLat(poiInfo.location.latitude);
                                                             locationMessage.setLng(poiInfo.location.longitude);
                                                             locationMessage.setAddress(poiInfo.address);
                                                             locationMessage.setSendStatue(SLMessage.MessagePropertie.MSG_SENDING);
                                                             InsertMessageTask insertMessageTask = new InsertMessageTask();
                                                             insertMessageTask
                                                                     .setOnPostExecuteHandler(new BaseTask.OnPostExecuteHandler<Boolean>() {
                                                                         @Override
                                                                         public void handle(Boolean result) {
                                                                             // 发送融云广播
                                                                             Intent send_Intent = new Intent(Actions.ACTION_SNED_SINGLE_MESSAGE);
                                                                             send_Intent.putExtra("MessageID",
                                                                                     String.valueOf(lastId));
                                                                             send_Intent.putExtra("chatType", "single");
                                                                             getApplicationContext().sendOrderedBroadcast(send_Intent, null);
                                                                             // 本地会话广播
                                                                             Intent intent = new Intent(Actions.SINGLEMESSAGE_ADD_ACTION);
                                                                             intent.putExtra("messageID",
                                                                                     String.valueOf(lastId));
                                                                             getApplicationContext().sendOrderedBroadcast(intent, null);
                                                                         }
                                                                     });
                                                             insertMessageTask.execute(locationMessage);
                                                             SLSession session = new SLSession();
                                                             session.setLastMessageId(String.valueOf(lastId));
                                                             session.setPriority(locationMessage.getTimestamp());
                                                             session.setTargetId(target);
                                                             session.setSessionContent(locationMessage.getMessageContent());
                                                             session.setMessageType(locationMessage.getMessageType());
                                                             session.setSessionType(SessionType.CHAT);
                                                             session.setSessionName(userDao.getUserByUserId(target).getNickName());
                                                             SessionDaoImpl sessionDaoImpl = new SessionDaoImpl();
                                                             sessionDaoImpl.addSession(session);
                                                             Intent session_intent = new Intent(Actions.ACTION_SESSION);
                                                             Bundle bundle = new Bundle();
                                                             bundle.putString("targetId", session.getTargetId());
                                                             session_intent.putExtras(bundle);
                                                             sendOrderedBroadcast(session_intent, null);
                                                             MapMessageActivity.this.finish();
                                                         } catch (FileNotFoundException e) {
                                                             e.printStackTrace();
                                                         } catch (IOException e) {
                                                             e.printStackTrace();
                                                         }
                                                     }
                                                 });
                                             }
                                         }
                                     }

        );
        etMLCityPoi.setOnClickListener(new

                                               OnClickListener() {

                                                   @Override
                                                   public void onClick(View v) {
                                                       if (etMLCityPoi.getText().toString().trim().length() > 0) {
                                                           getPoiByPoiSearch();
                                                       }
                                                   }
                                               }

        );
        etMLCityPoi.addTextChangedListener(new

                                                   TextWatcher() {

                                                       @Override
                                                       public void onTextChanged(CharSequence cs, int start, int before,
                                                                                 int count) {
                                                           if (cs.toString().trim().length() > 0) {
                                                               getPoiByPoiSearch();
                                                           } else {
                                                               if (searchPoiList != null) {
                                                                   searchPoiList.clear();
                                                               }
                                                               showMapOrSearch(SHOW_MAP);
                                                               hideSoftinput(mContext);
                                                           }
                                                       }

                                                       @Override
                                                       public void beforeTextChanged(CharSequence s, int start, int count,
                                                                                     int after) {
                                                       }

                                                       @Override
                                                       public void afterTextChanged(Editable s) {
                                                       }
                                                   }

        );
        ibMLLocate.setOnClickListener(new

                                              OnClickListener() {

                                                  @Override
                                                  public void onClick(View v) {
                                                      locate();
                                                  }
                                              }

        );
        btMapZoomIn.setOnClickListener(new

                                               OnClickListener() {

                                                   @Override
                                                   public void onClick(View v) {
                                                       isCanUpdateMap = false;
                                                       BaiduMapUtilByRacer.zoomInMapView(mMapView);
                                                   }
                                               }

        );
        btMapZoomOut.setOnClickListener(new

                                                OnClickListener() {

                                                    @Override
                                                    public void onClick(View v) {
                                                        isCanUpdateMap = false;
                                                        BaiduMapUtilByRacer.zoomOutMapView(mMapView);
                                                    }
                                                }

        );
        lvAroundPoi.setOnItemClickListener(new

                                                   OnItemClickListener() {
                                                       @Override
                                                       public void onItemClick(AdapterView<?> arg0, View view, int position,
                                                                               long arg3) {
                                                           isCanUpdateMap = false;
                                                           BaiduMapUtilByRacer.moveToTarget(
                                                                   aroundPoiList.get(position).location.latitude,
                                                                   aroundPoiList.get(position).location.longitude, mBaiduMap);
                                                           tvShowLocation.setText(aroundPoiList.get(position).name);
                                                           poiInfo = (PoiInfo) arg0.getAdapter().getItem(position);
                                                           LogUtil.i("address:item" + poiInfo.address);
                                                       }
                                                   }

        );
        lvSearchPoi.setOnItemClickListener(new

                                                   OnItemClickListener() {

                                                       @Override
                                                       public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                                               long arg3) {
                                                           // Geo搜索
                                                           // mGeoCoder.geocode(new GeoCodeOption().city(
                                                           // searchPoiList.get(arg2).getCity()).address(
                                                           // searchPoiList.get(arg2).getLocName()));
                                                           hideSoftinput(mContext);
                                                           isCanUpdateMap = false;
                                                           BaiduMapUtilByRacer.moveToTarget(searchPoiList.get(arg2)
                                                                           .getLatitude(), searchPoiList.get(arg2).getLongitude(),
                                                                   mBaiduMap);
                                                           tvShowLocation.setText(searchPoiList.get(arg2).getLocName());
                                                           // 反Geo搜索
                                                           reverseGeoCode(new LatLng(
                                                                   searchPoiList.get(arg2).getLatitude(), searchPoiList
                                                                   .get(arg2).getLongitude()), false);
                                                           if (ivMLPLoading != null
                                                                   && ivMLPLoading.getVisibility() == View.GONE) {
                                                               loadingHandler.sendEmptyMessageDelayed(1, 0);
                                                           }
                                                           showMapOrSearch(SHOW_MAP);
                                                       }
                                                   }

        );
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (llMLMain.getVisibility() == View.GONE) {
            showMapOrSearch(SHOW_MAP);
        } else {
            this.finish();
        }
    }

    ;

    BaiduMap.OnMapClickListener mapOnClickListener = new BaiduMap.OnMapClickListener() {
        /**
         * 地图单击事件回调函数
         *
         * @param point 点击的地理坐标
         */
        public void onMapClick(LatLng point) {
            hideSoftinput(mContext);
        }

        /**
         * 地图内 Poi 单击事件回调函数
         *
         * @param poi 点击的 poi 信息
         * @return
         */
        public boolean onMapPoiClick(MapPoi poi) {
            return false;
        }
    };
    private boolean isCanUpdateMap = true;
    BaiduMap.OnMapStatusChangeListener mapStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {
        /**
         * 手势操作地图，设置地图状态等操作导致地图状态开始改变。
         * @param status 地图状态改变开始时的地图状态
         */
        public void onMapStatusChangeStart(MapStatus status) {
        }

        /**
         * 地图状态变化中
         * @param status 当前地图状态
         */
        public void onMapStatusChange(MapStatus status) {
        }

        /**
         * 地图状态改变结束
         * @param status 地图状态改变结束后的地图状态
         */
        public void onMapStatusChangeFinish(MapStatus status) {
            if (isCanUpdateMap) {
                LatLng ptCenter = new LatLng(status.target.latitude,
                        status.target.longitude);
                // 反Geo搜索
                reverseGeoCode(ptCenter, true);
                if (ivMLPLoading != null
                        && ivMLPLoading.getVisibility() == View.GONE) {
                    loadingHandler.sendEmptyMessageDelayed(1, 0);
                }
            } else {
                isCanUpdateMap = true;
            }
        }
    };
    private static Animation hyperspaceJumpAnimation = null;
    Handler loadingHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0: {
                    // if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    // mLoadingDialog.dismiss();
                    // // showToast(mActivity.getString(R.string.map_locate_fault),
                    // // DialogType.LOAD_FAILURE);
                    // }
                    if (ivMLPLoading != null) {
                        ivMLPLoading.clearAnimation();
                        ivMLPLoading.setVisibility(View.GONE);
                    }
                    break;
                }
                case 1: {
                    // 加载动画
                    hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                            mContext, R.anim.dialog_loading_animation);
                    lvAroundPoi.setVisibility(View.GONE);
                    ivMLPLoading.setVisibility(View.VISIBLE);
                    // 使用ImageView显示动画
                    ivMLPLoading.startAnimation(hyperspaceJumpAnimation);
                    if (ivMLPLoading != null
                            && ivMLPLoading.getVisibility() == View.VISIBLE) {
                        loadingHandler.sendEmptyMessageDelayed(0, DELAY_DISMISS);
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 隐藏软键盘
     *
     * @param
     */
    private void hideSoftinput(Context mContext) {
        InputMethodManager manager = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager.isActive()) {
            manager.hideSoftInputFromWindow(etMLCityPoi.getWindowToken(), 0);
        }
    }

    // 刷新热门地名列表界面的adapter
    private void updatePoiListAdapter(List<PoiInfo> list, int index) {
        ivMLPLoading.clearAnimation();
        ivMLPLoading.setVisibility(View.GONE);
        lvAroundPoi.setVisibility(View.VISIBLE);
        if (mAroundPoiAdapter == null) {
            mAroundPoiAdapter = new AroundPoiAdapter(mContext, list);
            lvAroundPoi.setAdapter(mAroundPoiAdapter);
        } else {
            mAroundPoiAdapter.setNewList(list, index);
        }
    }

    // 刷新当前城市兴趣地点列表界面的adapter
    private void updateCityPoiListAdapter() {
        if (mSearchPoiAdapter == null) {
            mSearchPoiAdapter = new SearchPoiAdapter(mContext, searchPoiList);
            lvSearchPoi.setAdapter(mSearchPoiAdapter);
        } else {
            mSearchPoiAdapter.notifyDataSetChanged();
        }
        showMapOrSearch(SHOW_SEARCH_RESULT);
    }

    @Override
    protected void onDestroy() {
        mLocationBean = null;
        lvAroundPoi = null;
        lvSearchPoi = null;
        btMapZoomIn.setBackgroundResource(0);
        btMapZoomIn = null;
        btMapZoomOut.setBackgroundResource(0);
        btMapZoomOut = null;
        ibMLLocate.setImageBitmap(null);
        ibMLLocate.setImageResource(0);
        ibMLLocate = null;
        if (aroundPoiList != null) {
            aroundPoiList.clear();
            aroundPoiList = null;
        }
        mAroundPoiAdapter = null;
        if (searchPoiList != null) {
            searchPoiList.clear();
            searchPoiList = null;
        }
        mSearchPoiAdapter = null;
        if (mBaiduMap != null) {
            mBaiduMap.setMyLocationEnabled(false);// 关闭定位图层
            mBaiduMap = null;
        }
        if (mMapView != null) {
            mMapView.destroyDrawingCache();
            mMapView.onDestroy();
            mMapView = null;
        }
        if (etMLCityPoi != null) {
            etMLCityPoi.setBackgroundResource(0);
            etMLCityPoi = null;
        }
        mMarker = null;
        super.onDestroy();
        System.gc();
    }

    public class AroundPoiAdapter extends BaseAdapter {
        private Context mContext;
        private List<PoiInfo> mkPoiInfoList;
        public HashMap<String, Boolean> states = new HashMap<String, Boolean>();//用于记录每个RadioButton的状态，并保证只可选一个

        public AroundPoiAdapter(Context context, List<PoiInfo> list) {
            this.mContext = context;
            this.mkPoiInfoList = list;
            if (list.size() > 0) {
                states.put(String.valueOf(0), true);
                isCanUpdateMap = false;
                BaiduMapUtilByRacer.moveToTarget(
                        aroundPoiList.get(0).location.latitude,
                        aroundPoiList.get(0).location.longitude, mBaiduMap);
                tvShowLocation.setText(aroundPoiList.get(0).name);
                poiInfo = mkPoiInfoList.get(0);
            }
        }

        @Override
        public int getCount() {
            return mkPoiInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            if (mkPoiInfoList != null) {
                return mkPoiInfoList.get(position);
            }
            return null;
        }

        public void setNewList(List<PoiInfo> list, int index) {
            this.mkPoiInfoList = list;
            if (list.size() > 0) {
                // 初始化地图显示,默认选中第一条
                for (int i = 0; i < list.size(); i++) {
                    states.put(String.valueOf(i), false);
                }
                states.put(String.valueOf(0), true);
                isCanUpdateMap = false;
                BaiduMapUtilByRacer.moveToTarget(
                        aroundPoiList.get(0).location.latitude,
                        aroundPoiList.get(0).location.longitude, mBaiduMap);
                tvShowLocation.setText(aroundPoiList.get(0).name);
                poiInfo = mkPoiInfoList.get(0);
            }
            this.notifyDataSetChanged();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class RecordHolder {
            public RelativeLayout rlMLPIItem;
            public RadioButton checkeState;
            public TextView tvMLIPoiName, tvMLIPoiAddress;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            RecordHolder holder = null;
            if (convertView == null) {
                holder = new RecordHolder();
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(
                        R.layout.mapview_location_poi_lv_item, null);
                holder.tvMLIPoiName = (TextView) convertView
                        .findViewById(R.id.tvMLIPoiName);
                holder.tvMLIPoiAddress = (TextView) convertView
                        .findViewById(R.id.tvMLIPoiAddress);
                holder.rlMLPIItem = (RelativeLayout) convertView
                        .findViewById(R.id.rlMLPIItem);
                convertView.setTag(holder);
            } else {
                holder = (RecordHolder) convertView.getTag();
            }
            final RadioButton radioButton = (RadioButton) convertView
                    .findViewById(R.id.check_state);
            holder.checkeState = radioButton;
            holder.tvMLIPoiName.setText(mkPoiInfoList.get(position).name);
            holder.tvMLIPoiAddress.setText(mkPoiInfoList.get(position).address);
            //当RadioButton被选中时，将其状态记录进States中，并更新其他RadioButton的状态使它们不被选中
            holder.checkeState.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    isCanUpdateMap = false;
                    BaiduMapUtilByRacer.moveToTarget(
                            aroundPoiList.get(position).location.latitude,
                            aroundPoiList.get(position).location.longitude, mBaiduMap);
                    tvShowLocation.setText(aroundPoiList.get(position).name);
                    poiInfo = mkPoiInfoList.get(position);
                    LogUtil.i("address:item" + poiInfo.address);
                    //重置，确保最多只有一项被选中
                    for (String key : states.keySet()) {
                        states.put(key, false);
                    }
                    states.put(String.valueOf(position), radioButton.isChecked());
                    AroundPoiAdapter.this.notifyDataSetChanged();
                }
            });
            boolean res = false;
            if (states.get(String.valueOf(position)) == null || states.get(String.valueOf(position)) == false) {
                res = false;
                states.put(String.valueOf(position), false);
            } else
                res = true;
            holder.checkeState.setChecked(res);
            return convertView;
        }
    }
}
