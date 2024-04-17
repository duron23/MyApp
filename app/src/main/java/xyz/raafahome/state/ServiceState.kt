package xyz.raafahome.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object ServiceState {

    enum class ConnectionType {
        BLUETOOTH, NETWORK, USB, BLUETOOTH_NETWORK, UNDEFINED
    }

    enum class BusinessType {
        FC, MH, LDC, GROCERY, UNDEFINED
    }

    private val _isPrinterServiceRunning = MutableLiveData<Boolean>()
    val isPrinterServiceRunning: LiveData<Boolean> = _isPrinterServiceRunning

    private val _hostAddress = MutableLiveData<String>()
    val hostAddress: LiveData<String> = _hostAddress

    private val _connectionType = MutableLiveData<ConnectionType>()
    val connectionType: LiveData<ConnectionType> = _connectionType

    private val _businessType = MutableLiveData<BusinessType>()
    val businessType: LiveData<BusinessType> = _businessType

    private val _lastPrint = MutableLiveData<String>()
    val lastPrint: LiveData<String> = _lastPrint

    init {
        _isPrinterServiceRunning.value = false
        _hostAddress.value = ""
        _lastPrint.value = ""
        _connectionType.value = ConnectionType.UNDEFINED
        _businessType.value = BusinessType.UNDEFINED
    }

    fun setPrinterServiceRunning(isRunning: Boolean) {
        _isPrinterServiceRunning.postValue(isRunning)
    }

    fun setHostAddress(hostAddress: String) {
        _hostAddress.postValue(hostAddress)
    }

    fun setConnectionType(connectionType: ConnectionType) {
        _connectionType.postValue(connectionType)
    }

    fun setBusinessType(businessType: BusinessType) {
        _businessType.postValue(businessType)
    }

    fun updateLastPrint(lastPrint: String) {
        _lastPrint.postValue(lastPrint)
    }
}