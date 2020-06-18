package br.com.daniel.ramos.projetosmi.Services;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class BluetoothConnectionService {
    private static final String TAG = "BluetoothConnectionService";
    private static final String appName = "ProjetoSMI";
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private BluetoothAdapter mBluetoothAdapter;
    private Context context;

    private AcceptThread mInsecureAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;

    private BluetoothDevice mmDevice;
    private UUID deviceUUID;
    ProgressBar progressBar;

    public BluetoothConnectionService(Context context) {
        this.context = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        start();
    }

    /**
     * Essa Thread roda enquanto escuta por possíveis conexões. Atua como
     * um server-side client. Roda até uma conexão ser aceita ( ou cancelada)
     */
    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {
                // Cria um listening para o server socket
                tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(appName, MY_UUID_INSECURE);
                Log.d(TAG, "AcceptThread: Configurando Server usando: " + MY_UUID_INSECURE);
            } catch (IOException e) {
                Log.d(TAG, "AcceptThread: IOException: " + e.getMessage());
            }
            mmServerSocket = tmp;
        }

        public void run() {
            Log.d(TAG, "run: AcceptThread Running");
            BluetoothSocket socket = null;

            try {
                /**
                 * Esse bloco só retornara a chamada quando houver
                 * uma conexão com sucesso ou uma Exception
                 */
                Log.d(TAG, "run: RFCOM server socket iniciado......");
                socket = mmServerSocket.accept();
                Log.d(TAG, "run: RFCOM server socket Conexão Aceita");
            } catch (IOException e) {
                Log.d(TAG, "AcceptThread: IOException: " + e.getMessage());
            }

            if (socket != null) {
                connected(socket, mmDevice);
            }
        }

        public void cancel() {
            Log.d(TAG, "END mACceptThread");
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: Close of AcceptThread SErverSocket failed. " + e.getMessage());
            }
        }
    }

    /**
     * Essa Thread roda enquanto tenta fazer uma conexão com um dispositivo.
     * Roda inteiramente se a conexão for um sucesso ou falhar
     */
    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid) {
            mmDevice = device;
            deviceUUID = uuid;
        }

        public void run() {
            BluetoothSocket tmp = null;
            Log.i(TAG, "RUN mConnnectThread");

            try {
                // Pega um BluetoothSocket para uma conexão com dado BluetoothDevice
                Log.d(TAG, "ConnectThread: Tentando criar InsecureRfcommSocket usando UUID: "
                        + MY_UUID_INSECURE);
                tmp = mmDevice.createInsecureRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                Log.e(TAG, "ConnectThread: Could not create InsecureRfcommSocket " + e.getMessage());
            }

            mmSocket = tmp;

            // Sempre cancele discovery, pois vai deixa a conexão lenta
            mBluetoothAdapter.cancelDiscovery();

            // Faz a conexão com o BluetoothSocket

            try {
                //Bloco vai retornar uma conexão com sucesso ou exception
                mmSocket.connect();
                Log.d(TAG, "run: ConnectThread connected.");
            } catch (IOException e) {
                // Close socket
                try {
                    mmSocket.close();
                    Log.d(TAG, "run: Closed Socket.");
                } catch (IOException e1) {
                    Log.e(TAG, "mConnectThread: run: Unable to close connection in socket " + e1.getMessage());
                }
                Log.d(TAG, "run: ConnectThread: Could not connect to UUID: " + MY_UUID_INSECURE);
            }
            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                Log.d(TAG, "cancel: Closing Client Socket.");
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: close() of mmSocket in ConnectThread failed. " + e.getMessage());
            }
        }

        /**
         * Inicia o serviço de chat. Especificamente inicia AcceptThread e começa a
         * sessão escutando (server) mode. Chamado pela Activity onResume
         */
        public synchronized void start() {
            Log.d(TAG, "start");

            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
            if (mInsecureAcceptThread == null) {
                mInsecureAcceptThread = new AcceptThread();
                mInsecureAcceptThread.start();
            }
        }
    }

    /**
     * Responsavel por manter a conexão bluetooth. Enviando e recebendo dados
     * vindos através de input/output streams
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "ConnectedThread: Starting.");

            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            Log.i(TAG, "ProgressBar: Dismiss()");

            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "write: Error reading to inputStream " + e.getMessage());

            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            // Byte que pega o input de inputstream
            byte[] buffer = new byte[1024]; // Armazena a stream

            int bytes; // retorna os bytes lidos

            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    String incomingMessage = new String(buffer, 0, bytes); // Converte o Buffer e o bytes em uma mensagem
                    Log.d(TAG, "InputStream: " + incomingMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }

            }
        }

        /**
         * Chamada pela activity para enviar dados para o dispositivo remoto
         *
         * @param bytes
         */
        public void write(byte[] bytes) {
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG, "write: Writing to outputStream" + text);
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.e(TAG, "write: Error writing to outputstream " + e.getMessage());
            }
        }

        /**
         * Chamada pela activity para cancelar a conexão
         */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice) {
        Log.d(TAG, "connected: Starting");

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Synchronize a copy of the ConnectedThread
        Log.d(TAG, "write: Write Called");
        //perform the write
        mConnectedThread.write(out);
    }

    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    public synchronized void start() {
        Log.d(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mInsecureAcceptThread == null) {
            mInsecureAcceptThread = new AcceptThread();
            mInsecureAcceptThread.start();
        }
    }

    /**
     * AcceptThread inicia e aguarda por uma conexão.
     * Então ConnectThread inicia e tenta fazer uma conexão com os outros dispositivos AcceptThread
     */
    public void startClient(BluetoothDevice device, UUID uuid) {
        //TODO: Adaptar um progressBar
        Log.i(TAG, "ProgressBar: Connecting Bluetooth. Please wait...");
        mConnectThread = new ConnectThread(device, uuid);
        mConnectThread.start();
    }
}
