package xyz.raafahome.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import xyz.raafahome.R
import xyz.raafahome.state.BTPrinterState
import xyz.raafahome.state.BluetoothState
import xyz.raafahome.state.NetworkState
import xyz.raafahome.state.ServiceState

class HomeViewModel : ViewModel() {

    private val TAG = "HomeViewModel"

    private val _btStatus = MediatorLiveData<BluetoothState.BluetoothStatus>()
    val btStatus: LiveData<BluetoothState.BluetoothStatus> = _btStatus

    private val _isPrinterServiceRunning = MediatorLiveData<Boolean>()
    val isPrinterServiceRunning: LiveData<Boolean> = _isPrinterServiceRunning

    private val _printerStatus = MediatorLiveData<BTPrinterState.PrinterStatus>()
    val printerStatus: LiveData<BTPrinterState.PrinterStatus> = _printerStatus

    private val _hostAddress = MediatorLiveData<String>()
    val hostAddress: LiveData<String> = _hostAddress

    private val _connectionType = MediatorLiveData<ServiceState.ConnectionType>()
    val connectionType: LiveData<ServiceState.ConnectionType> = _connectionType

    private val _businessType = MediatorLiveData<ServiceState.BusinessType>()
    val businessType: LiveData<ServiceState.BusinessType> = _businessType

    private val _lastPrint = MediatorLiveData<String>()
    val lastPrint: LiveData<String> = _lastPrint

    private val _networkStatus = MediatorLiveData<NetworkState.NetworkStatus>()
    val networkStatus : LiveData<NetworkState.NetworkStatus> = _networkStatus

    init {

        _connectionType.addSource(ServiceState.connectionType) { connectionType ->
            _connectionType.value = connectionType
        }

        _isPrinterServiceRunning.addSource(ServiceState.isPrinterServiceRunning) { isPrinterServiceRunning ->
            _isPrinterServiceRunning.value = isPrinterServiceRunning
        }

        _hostAddress.addSource(ServiceState.hostAddress) { hostAddress ->
            _hostAddress.value = hostAddress
        }

        _printerStatus.addSource(BTPrinterState.printerStatus) {
            _printerStatus.value = it
        }

        _businessType.addSource(ServiceState.businessType) {
            _businessType.value = it
        }

        _btStatus.addSource(BluetoothState.bluetoothStatus) {
            _btStatus.value = it
        }

        _lastPrint.addSource(ServiceState.lastPrint) {
            _lastPrint.value = it
        }

        _networkStatus.addSource(NetworkState.networkStatus){
            _networkStatus.value = it
        }
    }

    private fun setConnectionType(type: ServiceState.ConnectionType) {
        ServiceState.setConnectionType(type)
    }

    private fun setBusinessType(type: ServiceState.BusinessType) {
        ServiceState.setBusinessType(type)
    }

    fun onConnectionTypeSelected(checkedId: Int) {
        val newConnectionType = when (checkedId) {
            R.id.btOnlyRadioButton -> ServiceState.ConnectionType.BLUETOOTH
            R.id.networkOnlyRadioButton -> ServiceState.ConnectionType.NETWORK
            R.id.btAndNetworkRadioButton -> ServiceState.ConnectionType.BLUETOOTH_NETWORK
            else -> ServiceState.ConnectionType.UNDEFINED
        }
        setConnectionType(newConnectionType)
    }

    fun onBusinessTypeSelected(checkedId: Int) {
        val newBusinessType = when (checkedId) {
            R.id.fcOptionRadioButton -> ServiceState.BusinessType.FC
            R.id.mhOptionRadioButton -> ServiceState.BusinessType.MH
            R.id.largeOptionRadioButton -> ServiceState.BusinessType.LDC
            R.id.groceryOptionRadioButton -> ServiceState.BusinessType.GROCERY
            else -> ServiceState.BusinessType.UNDEFINED
        }
        setBusinessType(newBusinessType)
    }
}