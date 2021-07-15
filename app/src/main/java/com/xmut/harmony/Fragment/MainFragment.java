package com.xmut.harmony.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;

import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.RouteOverlay;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.xmut.harmony.Adapter.StoreAdapter;
import com.xmut.harmony.R;
import com.xmut.harmony.Video.utils.StringUtil;
import com.xmut.harmony.entity.Result;
import com.xmut.harmony.entity.Store;
import com.xmut.harmony.map.overlay.WalkRouteOverlay;
import com.xmut.harmony.util.httputil.DatabaseUtil;
import com.xmut.harmony.util.httputil.http.HttpAddress;


import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment  implements  PoiSearch.OnPoiSearchListener , AMap.OnMarkerClickListener {
    private static final String TAG = "MainFragment Map";
    RouteOverlay routeOverlay;
    MapView mapView;
    Context context;
    CameraUpdate mCameraUpdate;
    MyLocationStyle myLocationStyle;
    public static List<Store> stores;
    private EditText editText; //搜索栏
    private String keyWord;
    private Button searchbt; //搜索bt
    private RecyclerView recyclerView; //商店
    private StoreAdapter storeAdapter;
    private LinearLayout linearLayout;
    boolean location_bt=false;
    private RelativeLayout map_visible_bt,search_visible_bt,store_visible_bt,location_visible_bt;
    private ImageView map_img,search_img,store_img,location_img;
    private LatLonPoint TargetMarker;
    AMap aMap;
    View view;

    //声明AMapLocationClient类对象
    public AMapLocationClient mapLocationClient;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mapLocationClientOption;
    public AMapLocationListener mLocationListener;

    private ProgressDialog progDialog = null;// 搜索时进度条
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;//搜索
    private int currentPage;// 当前页面，从0开始计数
    private PoiResult poiResults; // poi返回的结果
    private String city = "";//搜索城市
    private LatLonPoint latLonPoint; //当前位置
    private int zoomtype =15;

    private RouteSearch routeSearch;
    private WalkRouteResult mWalkRouteResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context =getActivity() ;
        view = inflater.inflate(R.layout.activity_main_fragment,container,false);
        permission();
        init();
        //高德map
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        return  view;

    }
    private void init()
    {
        //高德map 与道路规划
        mapView = (MapView)view.findViewById(R.id.map_visible);
        if (aMap == null) {
            aMap = mapView.getMap();
        }

        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(1000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);//
        aMap.showIndoorMap(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(zoomtype));
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                TargetMarker = new LatLonPoint(marker.getPosition().latitude,marker.getPosition().longitude);
                return false;
            }
        });
        routeSearch = new RouteSearch(context);
        routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {


            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
                dissmissProgressDialog();
                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (result != null && result.getPaths() != null) {
                        if (result.getPaths().size() > 0) {
                            aMap.clear();
                            mWalkRouteResult = result;
                            final WalkPath walkPath = mWalkRouteResult.getPaths()
                                    .get(0);
                            WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                                    context, aMap, walkPath,
                                    mWalkRouteResult.getStartPos(),
                                    mWalkRouteResult.getTargetPos());
                            walkRouteOverlay.removeFromMap();
                            walkRouteOverlay.addToMap();
                            walkRouteOverlay.zoomToSpan();

//                            List<WalkPath> pathList = result.getPaths();
//                            List<LatLng> walkPaths = new ArrayList<>();
//
//                            for (WalkPath dp : pathList) {
//
//                                List<WalkStep> stepList = dp.getSteps();
//                                for (WalkStep ds : stepList) {
//
//
//                                    List<LatLonPoint> points = ds.getPolyline();
//                                    for (LatLonPoint llp : points) {
//                                        walkPaths.add(new LatLng(llp.getLatitude(), llp.getLongitude()));
//                                    }
//                                }
//                            }
//
//                            aMap.clear();
//                            aMap.addPolyline(new PolylineOptions()
//                                    .addAll(walkPaths)
//                                    .width(40)
//                                    //是否开启纹理贴图
//                                    .setUseTexture(true)
//                                    //绘制成大地线
//                                    .geodesic(false)
//                                    //设置纹理样式
//                                    .setCustomTexture(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.custtexture)))
//                                    //设置画线的颜色
//                                    .color(Color.argb(200, 0, 0, 0)));

                        } else if (result != null && result.getPaths() == null) {
                            Toast.makeText(context,"导航错误", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context,"导航错误", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

            }
        });

        //点击后进行道路规划
        aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker arg0) {
            if(TargetMarker !=null && latLonPoint!=null)
            {
                RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                        latLonPoint,
                        TargetMarker);

                RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo,RouteSearch.WALK_DEFAULT);
                routeSearch.calculateWalkRouteAsyn(query);
                showProgressDialog();
            }
            else{
                Toast.makeText(context, "步行定位数据无效", Toast.LENGTH_SHORT).show();
            }

        }
    });

        //实时获取自身位置的监听器 要绑定到mapLocationClient 去获取实时定位
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation!=null){
                    if (aMapLocation.getErrorCode()==0){

                        StringBuilder stringBuilder = new StringBuilder();
                        //定位成功回调信息，设置相关消息
                        int type = aMapLocation.getLocationType();
                        String address = aMapLocation.getAddress();
                        stringBuilder.append(type+address);
                        city = aMapLocation.getCity();
                        //获得小点
                        if (latLonPoint==null){
                            latLonPoint = new LatLonPoint(aMapLocation.getLatitude(),aMapLocation.getLongitude());
                        }else {
                            latLonPoint.setLatitude(aMapLocation.getLatitude());
                            latLonPoint.setLongitude(aMapLocation.getLongitude());
                        }
                        //Toast.makeText(this,stringBuilder.toString(),Toast.LENGTH_SHORT).show();
                    }else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见下方错误码表。
                        Log.i("erro info：",aMapLocation.getErrorCode()+"---"+aMapLocation.getErrorInfo());
                    }
                }

            }
        };
        mapLocationClient = new AMapLocationClient(getActivity().getApplicationContext());

        //初始化AMapLocationClientOption对象
        mapLocationClientOption = new AMapLocationClientOption();
        mapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //是否返回地址信息
        mapLocationClientOption.setNeedAddress(true);
        //是否只定位一次
        mapLocationClientOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mapLocationClientOption.setWifiActiveScan(true);
        //是否允许模拟位置
        mapLocationClientOption.setMockEnable(false);
        //定位时间间隔
        mapLocationClientOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mapLocationClient.setLocationOption(mapLocationClientOption);
        //绑定监听
        mapLocationClient.setLocationListener(mLocationListener);
        //开启定位
        mapLocationClient.startLocation();


        linearLayout = view.findViewById(R.id.search_visible);
        editText =view.findViewById(R.id.search_text);
//        searchbt  =view.findViewById(R.id.search_bt);
        recyclerView = view.findViewById(R.id.store_visible);

        map_visible_bt = view.findViewById(R.id.map_visible_bt);
        search_visible_bt = view.findViewById(R.id.search_visible_bt);
        store_visible_bt = view.findViewById(R.id.store_visible_bt);
        location_visible_bt = view.findViewById(R.id.location_visible_bt);

        map_img = view.findViewById(R.id.map_img);
        search_img = view.findViewById(R.id.search_img);
        store_img = view.findViewById(R.id.store_img);
        location_img = view.findViewById(R.id.location_img);

        map_visible_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mapView.getVisibility()==View.GONE)
                {
                    mapView.setVisibility(View.VISIBLE);
                    map_img.setImageResource(R.drawable.ic_map_on);
                    myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
                   aMap.setMyLocationStyle(myLocationStyle);
                }else{
                    mapView.setVisibility(View.GONE);
                    map_img.setImageResource(R.drawable.ic_map_off);
                    //关闭搜索
                    linearLayout.setVisibility(View.GONE);
                    search_img.setImageResource(R.drawable.ic_search_off);
                    //关闭定位
                    myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
                    location_img.setImageResource(R.drawable.ic_location_off);
                    location_bt =false;
                    aMap.setMyLocationStyle(myLocationStyle);
                }
            }
        });
        search_visible_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearLayout.getVisibility()==View.GONE)
                {
                    linearLayout.setVisibility(View.VISIBLE);
                    search_img.setImageResource(R.drawable.ic_search_on);
                }else {
                    linearLayout.setVisibility(View.GONE);
                    search_img.setImageResource(R.drawable.ic_search_off);


                }
            }
        });
        store_visible_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recyclerView.getVisibility()==View.GONE)
                {
                    recyclerView.setVisibility(View.VISIBLE);
                    store_img.setImageResource(R.drawable.ic_store_on);
                }else {
                    recyclerView.setVisibility(View.GONE);
                    store_img.setImageResource(R.drawable.ic_store_off);
                }
            }
        });
        location_visible_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!location_bt){
                    location_img.setImageResource(R.drawable.ic_location_on);
                    location_bt =true;
                    myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);


                }else {
                    location_img.setImageResource(R.drawable.ic_location_off);
                    location_bt =false;
                    myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
                }

                aMap.setMyLocationStyle(myLocationStyle);
            }

        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                 keyWord = String.valueOf(charSequence);
                if (StringUtil.isEmpty(keyWord)) {
                    Toast.makeText(context,"请输入搜索关键字",Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    doSearchQuery(keyWord);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        getAllStore();
        storeAdapter = new StoreAdapter(context);
        storeAdapter.setStoreListList(stores);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(storeAdapter);

    }
    private void getAllStore() {
        stores = new ArrayList<>();
        Result result = DatabaseUtil.selectList(HttpAddress.get(HttpAddress.store(), "list"));
        if (result.getCode() != 200) {
            Toast.makeText(context, "获取商店失败", Toast.LENGTH_SHORT).show();
        } else {
                stores = DatabaseUtil.getObjectList(result,Store.class);
            System.out.println(stores);
            Toast.makeText(context, "获取商店成功", Toast.LENGTH_SHORT).show();
        }
    }

    //周边搜索
    private void doSearchQuery(String keyword) {
        showProgressDialog();
        currentPage = 0;
        //第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query = new PoiSearch.Query(keyword,"",city);
        query.setPageSize(30);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        poiSearch = new PoiSearch(context,query);
        poiSearch.setOnPoiSearchListener(this);//设置回调数据的监听器
        //点附近内的搜索结果
        if (latLonPoint!=null){
            PoiSearch.SearchBound searchBound = new PoiSearch.SearchBound(latLonPoint,20000);
            poiSearch.setBound(searchBound);
        }
        poiSearch.searchPOIAsyn();//开始搜索
    }


    private void permission() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            Log.i(TAG, "android sdk <= 28 Q");
            if (ActivityCompat.checkSelfPermission(context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[] strings =
                        {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};
                ActivityCompat.requestPermissions(getActivity(), strings, 1);
            }
        } else {
            // Android SDK>28 所需权限动态申请，需添加“android.permission.ACCESS_BACKGROUND_LOCATION”权限
            if (ActivityCompat.checkSelfPermission(context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context,
                    "android.permission.ACCESS_BACKGROUND_LOCATION") != PackageManager.PERMISSION_GRANTED) {
                String[] strings = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        "android.permission.ACCESS_BACKGROUND_LOCATION"};
                ActivityCompat.requestPermissions(getActivity(), strings, 2);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "onRequestPermissionsResult: apply LOCATION PERMISSION successful");
            } else {
                Log.i(TAG, "onRequestPermissionsResult: apply LOCATION PERMISSSION  failed");
            }
        }

        if (requestCode == 2) {
            if (grantResults.length > 2 && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "onRequestPermissionsResult: apply ACCESS_BACKGROUND_LOCATION successful");
            } else {
                Log.i(TAG, "onRequestPermissionsResult: apply ACCESS_BACKGROUND_LOCATION  failed");
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
        if (mapLocationClient!=null){
            mapLocationClient.stopLocation();
            mapLocationClient.onDestroy();
        }
        mapLocationClient = null;
    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);

    }
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(context);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("正在搜索:\n" + keyWord);
        progDialog.show();
    }

    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        Toast.makeText(context, infomation,Toast.LENGTH_SHORT).show();

    }
    public LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        dissmissProgressDialog();// 隐藏对话框
        if (i == 1000) {
            Log.i("---","查询结果:"+i);
            if (poiResult != null && poiResult.getQuery() != null) {// 搜索poi的结果
                if (poiResult.getQuery().equals(query)) {// 是否是同一条
                    poiResults = poiResult;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                    if (poiItems != null && poiItems.size() > 0) {
                        aMap.clear();// 清理之前的图标
                        for(int j=0;j<poiItems.size();j++){
//
                            System.out.println(poiItems.get(j).getTitle() );
                            aMap.addMarker(new MarkerOptions().position(convertToLatLng(poiItems.get(j).getLatLonPoint()) ).title(poiItems.get(j).getTitle()).snippet(poiItems.get(j).getSnippet()));
                        }
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
                        Toast.makeText(context, "未找到结果",Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(context, "该距离内没有找到结果",Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.i("---","查询结果:"+i);
            Toast.makeText(context, "异常代码---"+i,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


}