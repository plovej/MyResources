package com.myresources.baidu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.myresources.R;
import com.myresources.activity.MyBaseActivity;
import com.myresources.baidu.utils.BikingRouteOverlay;
import com.myresources.baidu.utils.DrivingRouteOverlay;
import com.myresources.baidu.utils.OverlayManager;
import com.myresources.baidu.utils.TransitRouteOverlay;
import com.myresources.baidu.utils.WalkingRouteOverlay;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 作者：范展鹏
 * 邮箱：fanzhanpeng@hskbj.com
 *
 * 此demo用来展示如何进行驾车、步行、公交路线搜索并在地图使用RouteOverlay、TransitOverlay绘制
 * 同时展示如何进行节点浏览并弹出泡泡
 */
public class PathplanningActivity extends MyBaseActivity implements BaiduMap.OnMapClickListener, OnGetRoutePlanResultListener, OnGetGeoCoderResultListener {
//    int nodeIndex = -1; // 节点索引,供浏览节点时使用
    RouteLine route = null;
    OverlayManager routeOverlay = null;
    boolean useDefaultIcon = false;

    // 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
    // 如果不处理touch事件，则无需继承，直接使用MapView即可
    MapView mMapView = null;    // 地图View
    BaiduMap mBaidumap = null;
    // 搜索相关
    RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    GeoCoder mGeoCoder = null; // 搜索模块，也可去掉地图模块独立使用
    private int planning;//选择使用什么路线
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(this);
        setContentView(R.layout.pathplanning_layout);
        this.setUpView();
        mMapView = (MapView) findViewById(R.id.map);

        mBaidumap = mMapView.getMap();
        // 地图点击事件处理
        mBaidumap.setOnMapClickListener(this);
        // 初始化搜索模块，注册事件监听
        // 获取RoutePlan检索实例
        mSearch = RoutePlanSearch.newInstance();
        //设置路线检索监听者
        mSearch.setOnGetRoutePlanResultListener(this);
        // 初始化搜索模块，注册事件监听
        mGeoCoder = GeoCoder.newInstance();

        mGeoCoder.setOnGetGeoCodeResultListener(this);
    }
     private EditText editSt;
     private EditText editstText;
     private EditText editEn;
     private EditText editentext;
    PlanNode stNode;
    PlanNode enNode;
    private void setUpView(){
        // 处理搜索按钮响应
         editSt = (EditText) findViewById(R.id.startchengshi);
         editstText = (EditText) findViewById(R.id.start);
         editEn = (EditText) findViewById(R.id.endchengshi);
         editentext = (EditText) findViewById(R.id.end);
        //EditView的焦点事件
        editSt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (!"".equals(editSt.getText().toString()) && !"".equals(editstText.getText().toString())){
                        mGeoCoder.geocode(new GeoCodeOption().city(editSt.getText().toString()).address(editstText.getText().toString()));
                    }
                }
            }
        });
        editstText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (!"".equals(editSt.getText().toString()) &&!"".equals(editstText.getText().toString())){
                        mGeoCoder.geocode(new GeoCodeOption().city(editSt.getText().toString()).address(editstText.getText().toString()));
                    }
                }
            }
        });
        editEn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (!"".equals(editEn.getText().toString()) && !"".equals(editentext.getText().toString())
                            && editEn.getText().toString() != null && editentext.getText().toString()!= null){
                        mGeoCoder.geocode(new GeoCodeOption().city(editEn.getText().toString()).address(editentext.getText().toString()));
                    }
                }
            }
        });
        editentext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (!"".equals(editEn.getText().toString()) && !"".equals(editentext.getText().toString())
                            && editEn.getText().toString() != null && editentext.getText().toString()!= null){
                        mGeoCoder.geocode(new GeoCodeOption().city(editEn.getText().toString()).address(editentext.getText().toString()));
                    }
                }
            }
        });
    }
    public void searchButtonProcess(View v) {
        // 重置浏览节点的路线数据
        route = null;
        mBaidumap.clear();
        // 设置起终点信息，对于tranist search 来说，城市名无意义
        stNode = PlanNode.withCityNameAndPlaceName(editSt.getText().toString(), editstText.getText().toString());
        enNode = PlanNode.withCityNameAndPlaceName(editEn.getText().toString(), editentext.getText().toString());
        // 实际使用中请对起点终点城市进行正确的设定
        if (v.getId() == R.id.drive) {
            //ROUTE_PATH_AND_TRAFFIC 驾车路线含路况 ROUTE_PATH 驾车路线不含路况
            /**
             * ECAR_AVOID_JAM
               驾车策略： 躲避拥堵
               ECAR_DIS_FIRST
               驾乘检索策略常量：最短距离
               ECAR_FEE_FIRST
               驾乘检索策略常量：较少费用
               ECAR_TIME_FIRST
               驾乘检索策略常量：时间优先

             */
            new AlertDialog.Builder(this).setTitle("选择路线")
                    .setPositiveButton("最短距离", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mSearch.drivingSearch((new DrivingRoutePlanOption())
                                    .from(stNode).to(enNode).trafficPolicy(DrivingRoutePlanOption.
                                            DrivingTrafficPolicy.ROUTE_PATH_AND_TRAFFIC).
                                            policy(DrivingRoutePlanOption.DrivingPolicy.ECAR_DIS_FIRST));
                        }
                    })
                    .setNegativeButton("时间较短", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mSearch.drivingSearch((new DrivingRoutePlanOption())
                                    .from(stNode).to(enNode).trafficPolicy(DrivingRoutePlanOption
                                            .DrivingTrafficPolicy.ROUTE_PATH_AND_TRAFFIC)
                                    .policy(DrivingRoutePlanOption.DrivingPolicy.ECAR_TIME_FIRST));
                        }
                    }).setNeutralButton("躲避拥堵", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mSearch.drivingSearch((new DrivingRoutePlanOption())
                            .from(stNode).to(enNode).trafficPolicy(DrivingRoutePlanOption.
                                    DrivingTrafficPolicy.ROUTE_PATH_AND_TRAFFIC).
                                    policy(DrivingRoutePlanOption.DrivingPolicy.ECAR_AVOID_JAM));
                }
            }).create().show();

        } else if (v.getId() == R.id.transit) {
            mSearch.transitSearch((new TransitRoutePlanOption())
                    .from(stNode).city(editSt.getText().toString()).to(enNode).city(editEn.getText().toString()));
        } else if (v.getId() == R.id.walk) {
            mSearch.walkingSearch((new WalkingRoutePlanOption())
                    .from(stNode).to(enNode));
        } else if (v.getId() == R.id.bike) {
            mSearch.bikingSearch((new BikingRoutePlanOption())
                    .from(stNode).to(enNode));
        }
    }



    /**-------------------搜索模块，也可去掉地图模块独立使用--------------------**/


        /**
     * 步行搜索返回结果
     * @param result
     */
    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(PathplanningActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
//            nodeIndex = -1;
            route = result.getRouteLines().get(0);
            /**
             * 用于显示步行路线的overlay，
             */
            WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaidumap);
            mBaidumap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }

    }


    /**
     * 用于显示步行路线的overlay，自3.4.0版本起可实例化多个添加在地图中显示
     */
    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    /**
     * 换乘路线搜索返回结果
     */
    @Override
    public void onGetTransitRouteResult(TransitRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(PathplanningActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }

        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
//            nodeIndex = -1;
            route = result.getRouteLines().get(0);
            TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
            mBaidumap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }
    /**
     * 用于显示换乘路线的Overlay，自3.4.0版本起可实例化多个添加在地图中显示
     */
    private class MyTransitRouteOverlay extends TransitRouteOverlay {

        public MyTransitRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }
    /**
     * 用于显示一条驾车路线的搜索返回结果
     */
    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(PathplanningActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
             result.getSuggestAddrInfo();
            return;
        }

        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
//            nodeIndex = -1;
            route = result.getRouteLines().get(0);
            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
            routeOverlay = overlay;
            mBaidumap.setOnMarkerClickListener(overlay);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
//       if (result.error == SearchResult.ERRORNO.NO_ERROR) {
//            nodeIndex = -1;
//            route = result.getRouteLines().get(0);
//            for (int i=0; i<result.getRouteLines().size();i++){
//                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
//                mBaidumap.setOnMarkerClickListener(overlay);
//                routeOverlay = overlay;
//                overlay.setData(result.getRouteLines().get(i));
//                overlay.addToMap();
//                overlay.zoomToSpan();
//            }
//
//        }

    }

    /**
     * 用于显示一条驾车路线的overlay，自3.4.0版本起可实例化多个添加在地图中显示，当数据中包含路况数据时，则默认使用路况纹理分段绘制
     */
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }
    /**
     * 用于显示骑行路线返回的数据
     */
    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
        if (bikingRouteResult == null || bikingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(PathplanningActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (bikingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (bikingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
//            nodeIndex = -1;
            route = bikingRouteResult.getRouteLines().get(0);
            BikingRouteOverlay overlay = new MyBikingRouteOverlay(mBaidumap);
            routeOverlay = overlay;
            mBaidumap.setOnMarkerClickListener(overlay);
            overlay.setData(bikingRouteResult.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }
    /**
     * 用于显示骑行路线的Overlay
     */
     private class MyBikingRouteOverlay extends BikingRouteOverlay {
        public  MyBikingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }


    }

    /**
     *地图点击事件处理
     * @param point
     */
    @Override
    public void onMapClick(LatLng point) {
        mBaidumap.hideInfoWindow();
    }
    /**s
     * 地图点击事件处理
     * @param poi
     * @return
     */
    @Override
    public boolean onMapPoiClick(MapPoi poi) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mSearch.destroy();
        mMapView.onDestroy();
        super.onDestroy();
    }

    //    算路设置起、终点，算路偏好，是否模拟导航等参数，然后在回调函数中设置跳转至诱导。

    private void routeplanToNavi(CoordinateType coType) {
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;
        switch (coType) {
            case GCJ02: {
                sNode = new BNRoutePlanNode(116.30142, 40.05087, "百度大厦", null, coType);
                eNode = new BNRoutePlanNode(116.39750, 39.90882, "北京天安门", null, coType);
                break;
            }
            case WGS84: {
                sNode = new BNRoutePlanNode(116.300821, 40.050969, "百度大厦", null, coType);
                eNode = new BNRoutePlanNode(116.397491, 39.908749, "北京天安门", null, coType);
                break;
            }
            case BD09_MC: {
                sNode = new BNRoutePlanNode(12947471, 4846474, "百度大厦", null, coType);
                eNode = new BNRoutePlanNode(12958160, 4825947, "北京天安门", null, coType);
                break;
            }
            case BD09LL: {
                sNode = new BNRoutePlanNode(116.30784537597782, 40.057009624099436, "百度大厦", null, coType);
                eNode = new BNRoutePlanNode(116.40386525193937, 39.915160800132085, "北京天安门", null, coType);
                break;
            }
            default:
                ;
        }
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode));
        }
    }

    public static List<Activity> activityList = new LinkedList<Activity>();
    public static final String ROUTE_PLAN_NODE = "routePlanNode";

    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
        /*
         * 设置途径点以及resetEndNode会回调该接口
         */

            for (Activity ac : activityList) {

                if (ac.getClass().getName().endsWith("BNDemoGuideActivity")) {

                    return;
                }
            }
            Intent intent = new Intent(PathplanningActivity.this, BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            Toast.makeText(PathplanningActivity.this, "算路失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 地理编码查询结果回调函数
     * @param result
     */
    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(getApplicationContext(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        String strInfo = String.format("纬度：%f 经度：%f",
                result.getLocation().latitude, result.getLocation().longitude);
        Toast.makeText(getApplicationContext(), strInfo, Toast.LENGTH_LONG)
                .show();
    }

    /**
     * 反地理编码查询结果回调函数
     * @param reverseGeoCodeResult
     */
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

    }

}
