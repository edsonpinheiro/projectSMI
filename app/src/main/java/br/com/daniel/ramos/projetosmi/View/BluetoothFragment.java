package br.com.daniel.ramos.projetosmi.View;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import br.com.daniel.ramos.projetosmi.Presenter.BluetoothPresenter;
import br.com.daniel.ramos.projetosmi.Presenter.PresenterMVP;
import br.com.daniel.ramos.projetosmi.R;

public class BluetoothFragment extends Fragment implements ViewMVP.BluetoothView, View.OnClickListener {

    private static final String TAG = "BluetoothFragment";

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;

    private PresenterMVP.BluetoothPresenter mPresenter;
    private BluetoothAdapter mBluetoothAdapter;

    private TextView statusBlueTv, pairedTv, informeMsgTv;
    private ImageView blueIv;
    private Button btnOnOff, discoverBtn, pairedBtn;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        initViews(view);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mPresenter = new BluetoothPresenter(this, mBluetoothAdapter);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver1);
    }

    // BroadcastReceiver para ACTION_FOUND
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mPresenter.broadcastReceiver1(context, intent);
        }
    };

    private void initViews(View view) {
        pairedBtn = view.findViewById(R.id.pairedBluetoothBtn);
        btnOnOff = view.findViewById(R.id.bluetoothONOFFBtn);
        discoverBtn = view.findViewById(R.id.discoverBluetoothBtn);
        statusBlueTv = view.findViewById(R.id.statusBluetoothTv);
        pairedTv = view.findViewById(R.id.pairedTv);
        blueIv = view.findViewById(R.id.bluetoothIv);
        informeMsgTv = view.findViewById(R.id.informeMessageTv);

        pairedBtn.setOnClickListener(this);
        btnOnOff.setOnClickListener(this);
        discoverBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pairedBluetoothBtn:
                break;
            case R.id.bluetoothONOFFBtn:
                mPresenter.enableDisableBT();
                break;
            case R.id.discoverBluetoothBtn:
                break;
        }
    }

    @Override
    public void enableBT() {
        Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivity(enableBTIntent);
    }

    @Override
    public void filterStatusBT(){
        // Filtro que intercepta mudança no status bluetooth
        IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        getActivity().registerReceiver(mBroadcastReceiver1, BTIntent);
    }


}
