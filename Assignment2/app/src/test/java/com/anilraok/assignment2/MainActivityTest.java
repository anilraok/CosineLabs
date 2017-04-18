package com.anilraok.assignment2;

import com.anilraok.assignment2.models.DeviceDisplay;
import com.anilraok.assignment2.screens.interfaces.MainActivityModelInterface;
import com.anilraok.assignment2.screens.interfaces.MainActivityViewInterface;
import com.anilraok.assignment2.screens.mvp.MainActivityPresenter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Anil on 14-04-2017.
 */

public class MainActivityTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    public MainActivityViewInterface mainActivityView;

    @Mock
    public MainActivityModelInterface mainActivityModel;

    private MainActivityPresenter mainActivityPresenter;

    private List<DeviceDisplay> MULTIPLE_DEVICES =  Arrays.asList(new DeviceDisplay(), new DeviceDisplay(), new DeviceDisplay());
    private List<DeviceDisplay> EMPTY_DEVICE_LIST = Collections.emptyList();

    @Before
    public void setUp(){
        mainActivityPresenter = new MainActivityPresenter(mainActivityView,mainActivityModel);
    }

    @Test
    public void shouldShowViewIfWifiConnected(){
        String testWifiName = "Test wifi";
        String testIpAddress = "192.168.1.2";

        when(mainActivityModel.isWifiDetailsPresent()).thenReturn(true);
        when(mainActivityModel.getCurrentWifiName()).thenReturn(testWifiName);
        when(mainActivityModel.getCurrentWifiIpAddress()).thenReturn(testIpAddress);

        mainActivityPresenter.loadCurrentWifiDetails();

        verify(mainActivityView).showCurrentWifiName(testWifiName);
        verify(mainActivityView).showCurrentIpAddress(testIpAddress);
        verify(mainActivityView).showWifiView();
    }

    @Test
    public void shouldHideViewIfWifiDisconnected(){
        when(mainActivityModel.isWifiDetailsPresent()).thenReturn(false);

        mainActivityPresenter.loadCurrentWifiDetails();

        verify(mainActivityView).hideWifiView();
    }

    @Test
    public void shouldShowDevicesView(){
        when(mainActivityModel.getOtherDevicesInCurrentNetwork()).thenReturn(MULTIPLE_DEVICES);

        mainActivityPresenter.loadOtherDevicesInNetwork();

        verify(mainActivityView).showOtherDevices(MULTIPLE_DEVICES);
    }

    @Test
    public void shouldShowNoDevicesView(){
        when(mainActivityModel.getOtherDevicesInCurrentNetwork()).thenReturn(EMPTY_DEVICE_LIST);

        mainActivityPresenter.loadOtherDevicesInNetwork();

        verify(mainActivityView).showNoDevicesView();
    }

}
