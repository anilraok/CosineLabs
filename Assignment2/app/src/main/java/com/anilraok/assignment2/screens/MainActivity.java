package com.anilraok.assignment2.screens;

import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pUpnpServiceRequest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.anilraok.assignment2.R;
import com.anilraok.assignment2.screens.mvp.MainActivityModel;
import com.anilraok.assignment2.screens.mvp.MainActivityPresenter;
import com.anilraok.assignment2.screens.mvp.MainActivityView;

public class MainActivity extends AppCompatActivity {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private WifiP2pUpnpServiceRequest mRequester;

    private MainActivityView mainActivityView;
    private MainActivityModel mainActivityModel;
    private MainActivityPresenter mainActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityView = new MainActivityView(this);
        mainActivityModel = new MainActivityModel(this);
        mainActivityPresenter = new MainActivityPresenter(mainActivityView,mainActivityModel);
        mainActivityPresenter.loadCurrentWifiDetails();
        mainActivityPresenter.loadOtherDevicesInNetwork();

        mainActivityModel.setPresenter(mainActivityPresenter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mainActivityPresenter != null)
            mainActivityPresenter.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mainActivityPresenter != null)
            mainActivityPresenter.onStop();
    }

    public void showToast(final String msg, final boolean longLength) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(
                        MainActivity.this,
                        msg,
                        longLength ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    /*mManager = (WifiP2pManager)getSystemService(Context.WIFI_P2P_SERVICE);

        mChannel = mManager.initialize(this, getMainLooper(), new WifiP2pManager.ChannelListener() {

            public void onChannelDisconnected() {
                Log.i("CI", "Channel detected!");
            }

        });

        mManager.setUpnpServiceResponseListener(mChannel, new WifiP2pManager.UpnpServiceResponseListener() {

            public void onUpnpServiceAvailable(List<String> arg0, WifiP2pDevice arg1) {
                Log.i("foundDevice", "Found device!!");
            }

        });
        mRequester = WifiP2pUpnpServiceRequest.newInstance();

        mManager.addServiceRequest(mChannel, mRequester, new WifiP2pManager.ActionListener() {

            public void onSuccess() {

                Log.i("d", "AddServiceRequest success!");

                mManager.discoverServices(mChannel, new WifiP2pManager.ActionListener() {

                    public void onSuccess() {
                        Log.i("d", "DiscoverServices success!");
                    }

                    public void onFailure(int reason) {
                    }
                });

                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Log.e("discoverPeers","success");
                        mManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {
                            @Override
                            public void onPeersAvailable(WifiP2pDeviceList peers) {
                                Log.e("onPeersAvailable","available"+peers.getDeviceList().size());
                            }
                        });
                    }

                    @Override
                    public void onFailure(int reason) {
                        Log.e("discoverPeers","failure"+reason);
                    }
                });
            }

            public void onFailure(int reason) {
                Log.i("addService failure", "reason"+reason);
            }
        });*/
}
