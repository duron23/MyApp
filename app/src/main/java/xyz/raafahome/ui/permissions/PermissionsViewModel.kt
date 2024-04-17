package xyz.raafahome.ui.permissions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PermissionsViewModel : ViewModel() {

    //Bluetooth Permissions
    val isBluetoothPermissionGranted = MutableLiveData<Boolean>()
    val isBluetoothConnectPermissionGranted = MutableLiveData<Boolean>()
    val isBluetoothAdminPermissionGranted = MutableLiveData<Boolean>()

    //Storage Permissions
    val isStorageReadPermissionGranted = MutableLiveData<Boolean>()
    val isStorageWritePermissionGranted = MutableLiveData<Boolean>()
    val isStorageManagePermissionGranted = MutableLiveData<Boolean>()

    //Network Permissions
    private val isInternetPermissionGranted = MutableLiveData<Boolean>()

    fun updateBluetoothPermissionState(isGranted: Boolean) {
        isBluetoothPermissionGranted.value = isGranted
    }

    fun updateBluetoothConnectPermissionState(isGranted: Boolean) {
        isBluetoothConnectPermissionGranted.value = isGranted
    }

    fun updateBluetoothAdminPermissionState(isGranted: Boolean) {
        isBluetoothAdminPermissionGranted.value = isGranted
    }

    fun updateStorageWritePermissionState(isGranted: Boolean) {
        isStorageWritePermissionGranted.value = isGranted
    }

    fun updateStorageManagePermissionState(isGranted: Boolean) {
        isStorageManagePermissionGranted.value = isGranted
    }

    fun updateStorageReadPermissionState(isGranted: Boolean) {
        isStorageReadPermissionGranted.value = isGranted
    }

    fun updateInternetPermissionState(isGranted: Boolean) {
        isInternetPermissionGranted.value = isGranted
    }


}