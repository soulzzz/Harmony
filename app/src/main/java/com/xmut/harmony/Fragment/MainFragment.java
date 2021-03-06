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
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.xmut.harmony.Adapter.GoodsAdapter;
import com.xmut.harmony.Adapter.StoreAdapter;
import com.xmut.harmony.R;
import com.xmut.harmony.Video.utils.StringUtil;
import com.xmut.harmony.entity.Product;
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
    public static List<Product> products;
    private EditText editText; //?????????
    private String keyWord;
    private Button searchbt; //??????bt
    private RecyclerView recyclerView,goodsrecyclerView; //??????
    private GoodsAdapter goodsAdapter;
    private StoreAdapter storeAdapter;
    private LinearLayout linearLayout;
    boolean location_bt=false;
    private RelativeLayout map_visible_bt,search_visible_bt,store_visible_bt,location_visible_bt,goods_visible_bt;
    private ImageView map_img,search_img,store_img,location_img,goods_img;
    private LatLonPoint TargetMarker;
    AMap aMap;
    View view;

    //??????AMapLocationClient?????????
    public AMapLocationClient mapLocationClient;
    //??????AMapLocationClientOption??????
    public AMapLocationClientOption mapLocationClientOption;
    public AMapLocationListener mLocationListener;

    private ProgressDialog progDialog = null;// ??????????????????
    private PoiSearch.Query query;// Poi???????????????
    private PoiSearch poiSearch;//??????
    private int currentPage;// ??????????????????0????????????
    private PoiResult poiResults; // poi???????????????
    private String city = "";//????????????
    private LatLonPoint latLonPoint; //????????????
    private int zoomtype =15;

    private RouteSearch routeSearch;
    private WalkRouteResult mWalkRouteResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context =getActivity() ;
        view = inflater.inflate(R.layout.activity_main_fragment,container,false);
        permission();
        init();
        //??????map
        mapView.onCreate(savedInstanceState);// ?????????????????????

        return  view;

    }
    private void init()
    {
        //??????map ???????????????
        mapView = (MapView)view.findViewById(R.id.map_visible);
        if (aMap == null) {
            aMap = mapView.getMap();
        }

        myLocationStyle = new MyLocationStyle();//??????????????????????????????myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????1???1???????????????????????????myLocationType????????????????????????????????????
        myLocationStyle.interval(1000); //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????1???1???????????????????????????????????????
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????1???1???????????????????????????????????????
        aMap.setMyLocationStyle(myLocationStyle);//?????????????????????Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);?????????????????????????????????????????????????????????
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
//                                    //????????????????????????
//                                    .setUseTexture(true)
//                                    //??????????????????
//                                    .geodesic(false)
//                                    //??????????????????
//                                    .setCustomTexture(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.custtexture)))
//                                    //?????????????????????
//                                    .color(Color.argb(200, 0, 0, 0)));

                        } else if (result != null && result.getPaths() == null) {
                            Toast.makeText(context,"????????????", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context,"????????????", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

            }
        });

        //???????????????????????????
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
                Toast.makeText(context, "????????????????????????", Toast.LENGTH_SHORT).show();
            }

        }
    });

        //???????????????????????????????????? ????????????mapLocationClient ?????????????????????
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation!=null){
                    if (aMapLocation.getErrorCode()==0){

                        StringBuilder stringBuilder = new StringBuilder();
                        //?????????????????????????????????????????????
                        int type = aMapLocation.getLocationType();
                        String address = aMapLocation.getAddress();
                        stringBuilder.append(type+address);
                        city = aMapLocation.getCity();
                        //????????????
                        if (latLonPoint==null){
                            latLonPoint = new LatLonPoint(aMapLocation.getLatitude(),aMapLocation.getLongitude());
                        }else {
                            latLonPoint.setLatitude(aMapLocation.getLatitude());
                            latLonPoint.setLongitude(aMapLocation.getLongitude());
                        }
                        //Toast.makeText(this,stringBuilder.toString(),Toast.LENGTH_SHORT).show();
                    }else {
                        //??????????????????ErrCode???????????????errInfo?????????????????????????????????????????????
                        Log.i("erro info???",aMapLocation.getErrorCode()+"---"+aMapLocation.getErrorInfo());
                    }
                }

            }
        };
        mapLocationClient = new AMapLocationClient(getActivity().getApplicationContext());

        //?????????AMapLocationClientOption??????
        mapLocationClientOption = new AMapLocationClientOption();
        mapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //????????????????????????
        mapLocationClientOption.setNeedAddress(true);
        //?????????????????????
        mapLocationClientOption.setOnceLocation(false);
        //????????????????????????WIFI????????????????????????
        mapLocationClientOption.setWifiActiveScan(true);
        //????????????????????????
        mapLocationClientOption.setMockEnable(false);
        //??????????????????
        mapLocationClientOption.setInterval(2000);
        //??????????????????????????????????????????
        mapLocationClient.setLocationOption(mapLocationClientOption);
        //????????????
        mapLocationClient.setLocationListener(mLocationListener);
        //????????????
        mapLocationClient.startLocation();


        linearLayout = view.findViewById(R.id.search_visible);
        editText =view.findViewById(R.id.search_text);
//        searchbt  =view.findViewById(R.id.search_bt);
        recyclerView = view.findViewById(R.id.store_visible);
        goodsrecyclerView =view.findViewById(R.id.goods_visible);
        map_visible_bt = view.findViewById(R.id.map_visible_bt);
        search_visible_bt = view.findViewById(R.id.search_visible_bt);
        store_visible_bt = view.findViewById(R.id.store_visible_bt);
        goods_visible_bt = view.findViewById(R.id.goods_visible_bt);
        location_visible_bt = view.findViewById(R.id.location_visible_bt);

        map_img = view.findViewById(R.id.map_img);
        search_img = view.findViewById(R.id.search_img);
        store_img = view.findViewById(R.id.store_img);
        location_img = view.findViewById(R.id.location_img);
        goods_img = view.findViewById(R.id.goods_img);
        map_visible_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mapView.getVisibility()==View.GONE)
                {
                    mapView.setVisibility(View.VISIBLE);
                    map_img.setImageResource(R.drawable.ic_map_on);
                    myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);//??????????????????????????????????????????????????????????????????????????????????????????
                   aMap.setMyLocationStyle(myLocationStyle);
                }else{
                    mapView.setVisibility(View.GONE);
                    map_img.setImageResource(R.drawable.ic_map_off);
                    //????????????
                    linearLayout.setVisibility(View.GONE);
                    search_img.setImageResource(R.drawable.ic_search_off);
                    //????????????
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

                    goodsrecyclerView.setVisibility(View.GONE);
                    goods_img.setImageResource(R.drawable.goods_img_off);
                }else {
                    recyclerView.setVisibility(View.GONE);
                    store_img.setImageResource(R.drawable.ic_store_off);
                }
            }
        });
        goods_visible_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(goodsrecyclerView.getVisibility()==View.GONE)
                {

                    goodsrecyclerView.setVisibility(View.VISIBLE);
                    goods_img.setImageResource(R.drawable.goods_img);

                    store_img.setImageResource(R.drawable.ic_store_off);
                    recyclerView.setVisibility(View.GONE);
                }else {
                    goodsrecyclerView.setVisibility(View.GONE);
                    goods_img.setImageResource(R.drawable.goods_img_off);
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
                    myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);//??????????????????????????????????????????????????????????????????????????????????????????
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
                    Toast.makeText(context,"????????????????????????",Toast.LENGTH_SHORT).show();
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
        getAllProducts();
        storeAdapter = new StoreAdapter(context);
        goodsAdapter = new GoodsAdapter(context);
        storeAdapter.setStoreListList(stores);
        goodsAdapter.setProductList(products);
        goodsrecyclerView.setAdapter(goodsAdapter);
        goodsrecyclerView.setLayoutManager(new GridLayoutManager(context,2));

        recyclerView.setAdapter(storeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }
    private void getAllStore() {
        stores = new ArrayList<>();
        Result result = DatabaseUtil.selectList(HttpAddress.get(HttpAddress.store(), "list"));
        if (result.getCode() != 200) {
            Toast.makeText(context, "??????????????????", Toast.LENGTH_SHORT).show();
        } else {
                stores = DatabaseUtil.getObjectList(result,Store.class);
            System.out.println("TEst"+stores);
            Toast.makeText(context, "??????????????????", Toast.LENGTH_SHORT).show();
        }
    }
    private void getAllProducts(){
        products = new ArrayList<>();
        Result result = DatabaseUtil.selectList(HttpAddress.get(HttpAddress.product(), "lists",1));
        if (result.getCode() != 200) {
            Toast.makeText(context, "??????????????????", Toast.LENGTH_SHORT).show();
        } else {
            products = DatabaseUtil.getObjectList(result,Product.class);
            Toast.makeText(context, "??????????????????", Toast.LENGTH_SHORT).show();
        }

    }

    //????????????
    private void doSearchQuery(String keyword) {
        showProgressDialog();
        currentPage = 0;
        //????????????????????????????????????????????????????????????poi????????????????????????????????????poi??????????????????????????????????????????
        query = new PoiSearch.Query(keyword,"",city);
        query.setPageSize(30);// ?????????????????????????????????poiitem
        query.setPageNum(currentPage);// ??????????????????
        poiSearch = new PoiSearch(context,query);
        poiSearch.setOnPoiSearchListener(this);//??????????????????????????????
        //???????????????????????????
        if (latLonPoint!=null){
            PoiSearch.SearchBound searchBound = new PoiSearch.SearchBound(latLonPoint,20000);
            poiSearch.setBound(searchBound);
        }
        poiSearch.searchPOIAsyn();//????????????
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
            // Android SDK>28 ???????????????????????????????????????android.permission.ACCESS_BACKGROUND_LOCATION?????????
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
        //???activity??????onDestroy?????????mMapView.onDestroy()???????????????
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
        //???activity??????onResume?????????mMapView.onResume ()???????????????????????????
        mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //???activity??????onPause?????????mMapView.onPause ()????????????????????????
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
        progDialog.setMessage("????????????:\n" + keyWord);
        progDialog.show();
    }

    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "????????????\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "????????????:" + cities.get(i).getCityName() + "????????????:"
                    + cities.get(i).getCityCode() + "????????????:"
                    + cities.get(i).getAdCode() + "\n";
        }
        Toast.makeText(context, infomation,Toast.LENGTH_SHORT).show();

    }
    public LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        dissmissProgressDialog();// ???????????????
        if (i == 1000) {
            Log.i("---","????????????:"+i);
            if (poiResult != null && poiResult.getQuery() != null) {// ??????poi?????????
                if (poiResult.getQuery().equals(query)) {// ??????????????????
                    poiResults = poiResult;
                    // ??????????????????poiitems????????????
                    List<PoiItem> poiItems = poiResult.getPois();// ??????????????????poiitem????????????????????????0??????
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// ???????????????poiitem?????????????????????????????????????????????????????????

                    if (poiItems != null && poiItems.size() > 0) {
                        aMap.clear();// ?????????????????????
                        for(int j=0;j<poiItems.size();j++){
//
                            System.out.println(poiItems.get(j).getTitle() );
                            aMap.addMarker(new MarkerOptions().position(convertToLatLng(poiItems.get(j).getLatLonPoint()) ).title(poiItems.get(j).getTitle()).snippet(poiItems.get(j).getSnippet()));
                        }
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
                        Toast.makeText(context, "???????????????",Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(context, "??????????????????????????????",Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.i("---","????????????:"+i);
            Toast.makeText(context, "????????????---"+i,Toast.LENGTH_SHORT).show();
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