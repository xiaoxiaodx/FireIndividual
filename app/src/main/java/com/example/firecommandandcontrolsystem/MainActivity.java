package com.example.firecommandandcontrolsystem;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;

import com.example.firecommandandcontrolsystem.Adapter.ControlMenuAdapter;
import com.example.firecommandandcontrolsystem.Adapter.MasterMenuAdapter;
import com.example.firecommandandcontrolsystem.adhocNetwork.AdhocClientThread;
import com.example.firecommandandcontrolsystem.adhocNetwork.AdhocProtocol;
import com.example.firecommandandcontrolsystem.adhocNetwork.ProtcocolClass.FloorConfig;
import com.example.firecommandandcontrolsystem.adhocNetwork.ProtcocolClass.GpsInfo;
import com.example.firecommandandcontrolsystem.adhocNetwork.ProtcocolClass.Rescue;
import com.example.firecommandandcontrolsystem.adhocNetwork.ProtcocolClass.Retreat;
import com.example.firecommandandcontrolsystem.fragment.ControlDeviceManager;
import com.example.firecommandandcontrolsystem.fragment.ControlFiremenInfo;
import com.example.firecommandandcontrolsystem.fragment.ControlMap;
import com.example.firecommandandcontrolsystem.fragment.ControlSystemSet;
import com.example.firecommandandcontrolsystem.fragment.ShowCmdResuce;
import com.example.firecommandandcontrolsystem.fragment.ShowCmdRetreat;
import com.example.firecommandandcontrolsystem.fragment.ShowCommunicationSet;
import com.example.firecommandandcontrolsystem.fragment.ShowDeviceManager;
import com.example.firecommandandcontrolsystem.fragment.ShowFiremenInfo;
import com.example.firecommandandcontrolsystem.fragment.ShowFloorSet;
import com.example.firecommandandcontrolsystem.fragment.ShowGlobalMap;
import com.example.firecommandandcontrolsystem.fragment.ShowIndoorMap;
import com.example.firecommandandcontrolsystem.fragment.ShowNetSet;
import com.example.firecommandandcontrolsystem.fragment.ShowOfflineMap;
import com.example.firecommandandcontrolsystem.fragment.ShowRescueLocation;
import com.example.firecommandandcontrolsystem.myClass.DataApplication;
import com.example.firecommandandcontrolsystem.myClass.Firemen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private String TAG = "MainActivity*";
    MasterMenuAdapter masterMenuAdapter;
    RecyclerView mastermenulistview;
    List<String> mastermeArrData;

    static public Handler MainActivityHandler;

    AdhocClientThread adhocThread;


    static final public int CMD_FloorConifg = 1;
    static final public int CMD_Retreat = 2;
    static final public int CMD_Rescue = 3;

    static final public int CMD_RecSos = 4;
    static final public int CMD_RecGpsInfo = 5;

    GridView controlMenuGirdview;
    ControlMenuAdapter controlMenuAdapter;
    List<ControlMenuAdapter.ControlMenuInfo> listcontrolmenu = new ArrayList<>();

//    private ShowCmdRetreat showCmdRetreat;
//    private ShowCmdResuce showCmdResuce;
//    private ShowDeviceManager showDeviceManager;
//    private ShowFiremenInfo showFiremenInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
//
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        initWindows();
        initMasterMenu();
        initControlMenu();

        replaceLeftControlMenu(0);
        replaceMainContentFragment(new ShowGlobalMap(this));

        handmessage();

        autoConnect();
    }


    private void autoConnect() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (adhocThread == null) {
                    adhocThread = new AdhocClientThread("39.108.105.245", 7090, 0);

                }
            }
        }, 2000);
    }


    private void initWindows() {
        Window window = getWindow();
        int color = getResources().getColor(R.color.colorStates);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(color);
            //设置导航栏颜色
            window.setNavigationBarColor(color);
            ViewGroup contentView = ((ViewGroup) findViewById(android.R.id.content));
            View childAt = contentView.getChildAt(0);
            if (childAt != null) {
                childAt.setFitsSystemWindows(true);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //设置contentview为fitsSystemWindows
            ViewGroup contentView = (ViewGroup) findViewById(android.R.id.content);
            View childAt = contentView.getChildAt(0);
            if (childAt != null) {
                childAt.setFitsSystemWindows(true);
            }
            //给statusbar着色
            View view = new View(this);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(this)));
            view.setBackgroundColor(color);
            contentView.addView(view);
        }
    }

    private static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    private void initControlMenu() {

        listcontrolmenu.clear();

        controlMenuAdapter = new ControlMenuAdapter(this, listcontrolmenu, new ControlMenuAdapter.onItemTouch() {
            @Override
            public void onItemTouch(String string) {
                if (string == "全局概览") {


                } else if (string == "队员显示") {


                } else if (string == "轨迹隐藏") {


                } else if (string == "轨迹回放") {


                } else if (string == "三维建模") {
                    if(interfaceGaodeMap != null)
                        interfaceGaodeMap.setup3Dmodel();

                } else if (string == "新增") {


                } else if (string == "删除选中") {


                } else if (string == "编辑选中") {


                } else if (string == "消息日志") {


                } else if (string == "撤离指令") {
                    replaceMainContentFragment(new ShowCmdRetreat(new ShowCmdRetreat.interfaceRetreat() {
                        @Override
                        public void interfaceRetreat(int cmdlevel, List<Integer> listretreat) {

                            Retreat retreat = new Retreat();
                            retreat.cmdLevel = cmdlevel;
                            if (listretreat.size() == 1 && listretreat.get(0) == 255) {
                                retreat.isALLRetreat = true;
                            } else {

                                retreat.isALLRetreat = false;
                                for (int i = 0; i < listretreat.size(); i++) {

                                    retreat.listRetreat.add(listretreat.get(i));
                                }
                            }

                            send_cmd(CMD_Retreat, retreat);
                        }
                    }));

                } else if (string == "搜救指令") {
                    replaceMainContentFragment(new ShowCmdResuce(new ShowCmdResuce.interfaceRescue() {
                        @Override
                        public void interfaceRescue(int firemenid, List<Integer> listrescue) {

                            DataApplication dataApplication = (DataApplication)getApplication();
                            Firemen firemen = dataApplication.findFiremenFromID(firemenid);

                            Rescue rescue = new Rescue(Integer.valueOf(firemen.getBindDeviceId()),firemen.getCurLocation().latitude,firemen.getCurLocation().longitude
                            ,firemen.getLatState(),firemen.getLngState(),firemen.getHeight(),firemen.getLocalState(),firemen.getFloor());

                            for (int i=0;i<listrescue.size();i++)
                                rescue.listRescue.add(listrescue.get(i));

                            send_cmd(AdhocProtocol.rescue,rescue);

                        }
                    }));

                } else if (string == "全员撤退") {

                    Retreat retreat = new Retreat();
                    retreat.cmdLevel = 2;
                    retreat.isALLRetreat = true;
                    send_cmd(CMD_Retreat, retreat);

                } else if (string == "楼层设置") {
                    replaceMainContentFragment(new ShowFloorSet(new ShowFloorSet.interfaceFloorConifg() {
                        @Override
                        public void interfaceFloorConifg(float topfirstHeight, float topheight, float lowfirstheight, float lowheight) {

                            FloorConfig floorConfig = new FloorConfig(topfirstHeight, topheight, lowfirstheight, lowheight);

                            send_cmd(CMD_FloorConifg, floorConfig);
                        }
                    }));

                } else if (string == "网络设置") {
                    replaceMainContentFragment(new ShowNetSet(new ShowNetSet.interfaceNetConnect() {
                        @Override
                        public void interfaceAdhocConnect(String ip, int port) {
                            //adhocThread = new AdhocClientThread(ip,port,0);
                        }

                        @Override
                        public void interfaceCloudConnect(String ip, int port) {

                        }
                    }));

                } else if (string == "通信设置") {


                } else if (string == "离线地图") {


                }
            }
        });
        controlMenuGirdview = findViewById(R.id.contrlo_menu);
        controlMenuGirdview.setAdapter(controlMenuAdapter);
        controlMenuGirdview.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    private void initMasterMenu() {

        mastermenulistview = (RecyclerView) findViewById(R.id.listview_mastermenu);


        mastermeArrData = new ArrayList<>();
        mastermeArrData.add("全局定位");
        mastermeArrData.add("搜救定位");
        mastermeArrData.add("室内定位");
        mastermeArrData.add("设备管理");
        mastermeArrData.add("人员信息");
        mastermeArrData.add("系统设置");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mastermenulistview.setLayoutManager(layoutManager);
        masterMenuAdapter = new MasterMenuAdapter(this, mastermeArrData);
        mastermenulistview.setAdapter(masterMenuAdapter);
    }

    public void onClick(View view) {

        int resId = view.getId();

        DataApplication dataApplication = (DataApplication) getApplicationContext();

        switch (resId) {


//            case R.id.menu_quanyuanchetui:
//                break;
//            //系统设置
//            case R.id.menu_loucengshezhi:
//                replaceMainContentFragment(new ShowFloorSet(new ShowFloorSet.interfaceFloorConifg() {
//                    @Override
//                    public void interfaceFloorConifg(float topfirstHeight, float topheight, float lowfirstheight, float lowheight) {
//                        Log.e(TAG, "楼层配置");
//
//
//                    }
//                }));
//                break;
//            case R.id.menu_wangluoshezhi:
//                replaceMainContentFragment(new ShowNetSet(new ShowNetSet.interfaceNetConnect() {
//                    @Override
//                    public void interfaceAdhocConnect(String ip, int port) {
//
//                    }
//
//                    @Override
//                    public void interfaceCloudConnect(String ip, int port) {
//
//                    }
//                }));
//                break;
//            case R.id.menu_tongxinsehzhi:
//                replaceMainContentFragment(new ShowCommunicationSet());
//                break;
//            case R.id.menu_lixianditu:
//                replaceMainContentFragment(new ShowOfflineMap());
//                break;
        }

    }


    public void masterMenuClick(View view) {
        // 获取itemView的位置
        int position = mastermenulistview.getChildAdapterPosition(view);

        if (((DataApplication) getApplication()).mastermenuCurSelectIndex == position)
            return;
        ((DataApplication) getApplication()).mastermenuCurSelectIndex = position;

        maincontentSwitch(position);

        masterMenuAdapter.notifyDataSetChanged();

    }


    public void maincontentSwitch(int position) {

        replaceLeftControlMenu(position);
        switch (position) {

            case 0:
                replaceMainContentFragment(new ShowGlobalMap(this));
                break;
            case 1:
                replaceMainContentFragment(new ShowRescueLocation());
                break;
            case 2:
                replaceMainContentFragment(new ShowIndoorMap());
                break;
            case 3:
                replaceMainContentFragment(new ShowDeviceManager(this));
                break;
            case 4:
                replaceMainContentFragment(new ShowFiremenInfo());
                break;
            case 5:
                replaceMainContentFragment(new ShowFloorSet(new ShowFloorSet.interfaceFloorConifg() {
                    @Override
                    public void interfaceFloorConifg(float topfirstHeight, float topheight, float lowfirstheight, float lowheight) {
                        Log.e(TAG, "楼层配置");
                    }
                }));

                break;
        }
    }

    public interface interfaceGaodeMap {
        void setup3Dmodel();

        void updateLngLat(GpsInfo info);
    }

    private interfaceGaodeMap interfaceGaodeMap;

    public void setListener(interfaceGaodeMap interfacepar) {
        this.interfaceGaodeMap = interfacepar;
    }


    public void replaceLeftControlMenu(int mastermenuIndex) {


        switch (mastermenuIndex) {
            case 0:
                listcontrolmenu.clear();
                listcontrolmenu.add(new ControlMenuAdapter.ControlMenuInfo("全局概览", "#97b2df", "#23ecf7", R.mipmap.quanjudingwei_quanjugailan, R.mipmap.quanjudingwei_quanjugailan_s, "#011a43", R.mipmap.menu_bg_blue));
                listcontrolmenu.add(new ControlMenuAdapter.ControlMenuInfo("队员显示", "#97b2df", "#00ff00", R.mipmap.quanjudingwei_duiyuanxianshi, R.mipmap.quanjudingwei_duiyuanxianshi_s, "#011a43", R.mipmap.menu_bg_green));
                listcontrolmenu.add(new ControlMenuAdapter.ControlMenuInfo("轨迹隐藏", "#97b2df", "#00ff00", R.mipmap.quanjudingwei_qingchuguiji, R.mipmap.quanjudingwei_qingchuguiji_s, "#011a43", R.mipmap.menu_bg_green));
                listcontrolmenu.add(new ControlMenuAdapter.ControlMenuInfo("轨迹回放", "#97b2df", "#ff8f00", R.mipmap.quanjudingwei_lishihuifang, R.mipmap.quanjudingwei_lishihuifang_s, "#011a43", R.mipmap.menu_bg_orange));
                listcontrolmenu.add(new ControlMenuAdapter.ControlMenuInfo("三维建模", "#97b2df", "#23ecf7", R.mipmap.quanjudingwei_3djianmo, R.mipmap.quanjudingwei_3djianmo_s, "#011a43", R.mipmap.menu_bg_blue));
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                listcontrolmenu.clear();
                listcontrolmenu.add(new ControlMenuAdapter.ControlMenuInfo("新增", "#97b2df", "#23ecf7", R.mipmap.quanjudingwei_quanjugailan, R.mipmap.quanjudingwei_quanjugailan_s, "#011a43", R.mipmap.menu_bg_blue));
                listcontrolmenu.add(new ControlMenuAdapter.ControlMenuInfo("删除选中", "#97b2df", "#00ff00", R.mipmap.quanjudingwei_duiyuanxianshi, R.mipmap.quanjudingwei_duiyuanxianshi_s, "#011a43", R.mipmap.menu_bg_green));
                listcontrolmenu.add(new ControlMenuAdapter.ControlMenuInfo("编辑选中", "#97b2df", "#00ff00", R.mipmap.quanjudingwei_qingchuguiji, R.mipmap.quanjudingwei_qingchuguiji_s, "#011a43", R.mipmap.menu_bg_green));

                break;
            case 4:
                listcontrolmenu.clear();
                listcontrolmenu.add(new ControlMenuAdapter.ControlMenuInfo("报警信息", "#97b2df", "#23ecf7", R.mipmap.quanjudingwei_quanjugailan, R.mipmap.quanjudingwei_quanjugailan_s, "#011a43", R.mipmap.menu_bg_blue));
                listcontrolmenu.add(new ControlMenuAdapter.ControlMenuInfo("撤离指令", "#97b2df", "#00ff00", R.mipmap.quanjudingwei_duiyuanxianshi, R.mipmap.quanjudingwei_duiyuanxianshi_s, "#011a43", R.mipmap.menu_bg_green));
                listcontrolmenu.add(new ControlMenuAdapter.ControlMenuInfo("搜救指令", "#97b2df", "#00ff00", R.mipmap.quanjudingwei_qingchuguiji, R.mipmap.quanjudingwei_qingchuguiji_s, "#011a43", R.mipmap.menu_bg_green));
                listcontrolmenu.add(new ControlMenuAdapter.ControlMenuInfo("全员撤退", "#97b2df", "#ff8f00", R.mipmap.quanjudingwei_lishihuifang, R.mipmap.quanjudingwei_lishihuifang_s, "#011a43", R.mipmap.menu_bg_orange));
                break;
            case 5:
                listcontrolmenu.clear();
                listcontrolmenu.add(new ControlMenuAdapter.ControlMenuInfo("楼层设置", "#97b2df", "#23ecf7", R.mipmap.quanjudingwei_quanjugailan, R.mipmap.quanjudingwei_quanjugailan_s, "#011a43", R.mipmap.menu_bg_blue));
                listcontrolmenu.add(new ControlMenuAdapter.ControlMenuInfo("网络设置", "#97b2df", "#00ff00", R.mipmap.quanjudingwei_duiyuanxianshi, R.mipmap.quanjudingwei_duiyuanxianshi_s, "#011a43", R.mipmap.menu_bg_green));
                listcontrolmenu.add(new ControlMenuAdapter.ControlMenuInfo("通信设置", "#97b2df", "#00ff00", R.mipmap.quanjudingwei_qingchuguiji, R.mipmap.quanjudingwei_qingchuguiji_s, "#011a43", R.mipmap.menu_bg_green));
                listcontrolmenu.add(new ControlMenuAdapter.ControlMenuInfo("离线地图", "#97b2df", "#ff8f00", R.mipmap.quanjudingwei_lishihuifang, R.mipmap.quanjudingwei_lishihuifang_s, "#011a43", R.mipmap.menu_bg_orange));
                break;
        }

        controlMenuAdapter.notifyDataSetChanged();
    }


    public void replaceMainContentFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();//注意！每次要执行commit()方法的时候都需要重新获取一次FragmentTransaction，否则用已经commit过的FragmentTransaction再次commit会报错！
        transaction.replace(R.id.maincontent_framelayout, fragment);
        transaction.commit();
    }

    public static void sendMsgToHandle(int what, Object obj, int par1, int par2) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = obj;
        msg.arg1 = par1;
        msg.arg2 = par2;
        MainActivityHandler.sendMessage(msg);
    }

    private void send_cmd(int cmd, Object obj) {

        Message msg = new Message();
        msg.obj = obj;
        switch (cmd) {

            case CMD_FloorConifg:
                msg.what = AdhocProtocol.configHeight;
                break;
            case CMD_Rescue:
                msg.what = AdhocProtocol.rescue;
                break;
            case CMD_Retreat:
                msg.what = AdhocProtocol.retreat;
                break;
        }

        AdhocClientThread.receiHandler.sendMessage(msg);

    }


    private String handmsgTag = "MainActivityHandler";

    private void handmessage() {

        MainActivityHandler = new Handler() {
            @Override
            public void handleMessage(final Message msg) {
                super.handleMessage(msg);

                Log.e(handmsgTag, "what:" + msg.what + " obj:" + msg.obj.toString() + " arg1:" + msg.arg1 + " arg2:" + msg.arg2);

                Message sendmsg = new Message();
                switch (msg.what) {

                    case CMD_RecGpsInfo:

                        GpsInfo info = (GpsInfo)msg.obj;

                        if(interfaceGaodeMap != null){
                            interfaceGaodeMap.updateLngLat(info);
                        }


                        break;
                    case CMD_RecSos:
                        break;
                }
            }

            ;
        };
    }
}
