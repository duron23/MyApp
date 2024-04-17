package xyz.raafahome.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object BluetoothState {
    private val _bluetoothStatus = MutableLiveData<BluetoothStatus>()
    val bluetoothStatus: LiveData<BluetoothStatus> = _bluetoothStatus

    enum class BluetoothStatus {
        ENABLED, DISABLED, NO_PAIRED_DEVICES, MORE_THAN_ONE_PAIRED_DEVICE
    }

    init {
        _bluetoothStatus.value = BluetoothStatus.DISABLED
    }

    fun updateBluetoothStatus(status: BluetoothStatus) {
        _bluetoothStatus.postValue(status)
    }

}