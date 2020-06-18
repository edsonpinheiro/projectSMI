package br.com.daniel.ramos.projetosmi.View;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.daniel.ramos.projetosmi.Adapters.DeviceListAdapter;
import br.com.daniel.ramos.projetosmi.Presenter.BluetoothPresenter;
import br.com.daniel.ramos.projetosmi.Presenter.PresenterMVP;
import br.com.daniel.ramos.projetosmi.R;
import br.com.daniel.ramos.projetosmi.Services.BluetoothConnectionService;

//TODO: Criar duas lista, uma para dispositivos pareados e outra para dispositivos não pareados.
public class BluetoothFragment extends Fragment implements ViewMVP.BluetoothView, View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = "BluetoothFragment";

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;

    private PresenterMVP.BluetoothPresenter mPresenter;
    private BluetoothAdapter mBluetoothAdapter;

    private TextView statusBlueTv, pairedTv, informeMsgTv;
    private EditText messageEt;
    private ImageView blueIv;
    private Button btnOnOff, btnDiscoverOnOff, btnUnpairedDevices, btnStartConn, btnSend;
    private ListView newDevicesLv;
    private Context mContext;

    private ArrayList<BluetoothDevice> mBTDevices;
    private DeviceListAdapter mDeviceListAdapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        initViews(view);
        mContext = getContext();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mPresenter = new BluetoothPresenter(this, mBluetoothAdapter);
        mBTDevices = new ArrayList<>();
        filterBondStateChange();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiverStatus);
        getActivity().unregisterReceiver(mBroadcastReceiverDiscovery);
        getActivity().unregisterReceiver(mBroadcastReceiverDiscoverUnpaired);
        getActivity().unregisterReceiver(mBroadcastReceiver4);
    }

    //============ BROADCAST RECEIVERS ============//

    private final BroadcastReceiver mBroadcastReceiverStatus = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mPresenter.mBroadcastReceiverStatus(context, intent);
        }
    };

    private final BroadcastReceiver mBroadcastReceiverDiscovery = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mPresenter.mBroadcastReceiverDiscovery(context, intent);
        }
    };

    private final BroadcastReceiver mBroadcastReceiverDiscoverUnpaired = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mPresenter.mBroadcastReceiverDiscoverUnpaired(context, intent);
        }
    };

    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mPresenter.mBroadcastReceiver4(context, intent);
        }
    };

    //============ Inicializa as Views do Fragment ============//
    private void initViews(View view) {
        // === Buttons === //
        btnOnOff = view.findViewById(R.id.bluetoothONOFFBtn);
        btnDiscoverOnOff = view.findViewById(R.id.enableDisableDiscover);
        btnUnpairedDevices = view.findViewById(R.id.findUnpairedDevices);
        btnStartConn = view.findViewById(R.id.btnStartConnection);
        btnSend = view.findViewById(R.id.btnSend);

        //TODO: Enviar o número de celular do aparelho, inves de um texto
        messageEt = view.findViewById(R.id.editText);

        blueIv = view.findViewById(R.id.bluetoothIv);
        informeMsgTv = view.findViewById(R.id.informeMessageTv);

        newDevicesLv = (ListView) view.findViewById(R.id.newDevicesLv);
        newDevicesLv.setOnItemClickListener(this);

        btnOnOff.setOnClickListener(this);
        btnDiscoverOnOff.setOnClickListener(this);
        btnUnpairedDevices.setOnClickListener(this);
        btnStartConn.setOnClickListener(this);
        btnSend.setOnClickListener(this);

    }


    // ========== onClick Method ========== //
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bluetoothONOFFBtn:
                mPresenter.enableDisableBT();
                break;
            case R.id.enableDisableDiscover:
                mPresenter.enableDisableDiscoverableBT();
                break;
            case R.id.findUnpairedDevices:
                newDevicesLv.setAdapter(null); // Limpamos a lista para nova busca
                // Passamos a lista de dispositivos bluetooth para o presenter
                mPresenter.findUnpairedDevices(mBTDevices);
                break;
            case R.id.btnStartConnection:
                mPresenter.startConnection();
                break;
            case R.id.btnSend:
                mPresenter.sendWriteBytes(messageEt);
        }
    }

    // ========== Intent Methods ========== //
    @Override
    public void enableBT() {
        Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivity(enableBTIntent);
    }

    @Override
    public void enableDisableDiscoverableBT() {
        Log.d(TAG, "enableDisableDiscoverableBT: Making device discoverable for 300 seconds");
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
    }



    //============ Bluetooth Filters ============//
    @Override
    public void filterStatusBT(){
        // Filtro que intercepta mudança no status bluetooth
        IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        getActivity().registerReceiver(mBroadcastReceiverStatus, BTIntent);
    }

    @Override
    public void filterDiscoverableBT() {
        IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        getActivity().registerReceiver(mBroadcastReceiverDiscovery, BTIntent);
    }

    @Override
    public void filterDiscoverUnpairedBT() {
        IntentFilter BTIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mBroadcastReceiverDiscoverUnpaired, BTIntent);
    }

    @Override
    public void filterBondStateChange() {
        IntentFilter BTIntent = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        getActivity().registerReceiver(mBroadcastReceiver4, BTIntent);
    }

    @Override
    public void refreshListViewDevices(Context context, ArrayList<BluetoothDevice> devices) {
        mDeviceListAdapter = new DeviceListAdapter(context, R.layout.view_device_adapter, devices);
        newDevicesLv.setAdapter((ListAdapter) mDeviceListAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Cancela o discovery (uso intensivo de memória)
        mBluetoothAdapter.cancelDiscovery();

        Log.d(TAG, "onItemClick: You clicked on a device");
        String deviceName = mBTDevices.get(position).getName();
        String deviceAdress = mBTDevices.get(position).getAddress();
        Log.d(TAG, "onItemClick: deviceName: " +  deviceName );
        Log.d(TAG, "onItemClick: deviceAdress " + deviceAdress);

        // Create a bond
        // API 17 +
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Log.d(TAG, "Pareando com " + deviceName);
            mPresenter.initBluetoothAndAssignItemClicked(mContext, position, mBTDevices);
        }
    }

    @Override
    public void showToastMessageShort(String message) {
        Toast.makeText(mContext, "message", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToastMessageLong(String message) {
        Toast.makeText(mContext, "message", Toast.LENGTH_LONG).show();
    }
}
