package com.interlinkj.android.remotecamera;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class ConnectThread extends Thread {
	private static final UUID SERIAL_PORT_PROFILE = UUID
		.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private final BluetoothSocket mmSocket;
	private final BluetoothDevice mmDevice;

	public ConnectThread(BluetoothDevice device) {
		// Use a temporary object that is later assigned to mmSocket,
		// because mmSocket is final
		BluetoothSocket tmp = null;
		mmDevice = device;

		// BluetoothSocket�̍쐬
		try {
			// UUID���w�肵��rfcomm�̃\�P�b�g���쐬
			tmp = device
					.createRfcommSocketToServiceRecord(SERIAL_PORT_PROFILE);
		} catch(IOException e) {
		}
		mmSocket = tmp;
	}

	public void run() {
		// �ʐM�J�n�O�Ƀf�o�C�X�̒T���𒆎~������
//		mAdapter.cancelDiscovery();

		try {
			// �\�P�b�g�𗘗p���ĒʐM���J�n����
			mmSocket.connect();
		} catch(IOException connectException) {
			// ��O�����������ꍇ�̓\�P�b�g��������𔲂���
			try {
				mmSocket.close();
			} catch(IOException closeException) {
			}
			return;
		}

		// Do work to manage the connection (in a separate thread)
		manageConnectedSocket(mmSocket);
	}

	/** Will cancel an in-progress connection, and close the socket */
	public void cancel() {
		try {
			mmSocket.close();
		} catch(IOException e) {
		}
	}
	
	public void manageConnectedSocket(BluetoothSocket mmSocket) {
		ConnectedThread mConnectedThread = new ConnectedThread(mmSocket);
		mConnectedThread.start();
	}
}
