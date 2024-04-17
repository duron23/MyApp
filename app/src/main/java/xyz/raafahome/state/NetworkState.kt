package xyz.raafahome.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object NetworkState {
    private val _networkStatus = MutableLiveData<NetworkStatus>()
    val networkStatus: LiveData<NetworkStatus> = _networkStatus

    enum class NetworkStatus {
        CONNECTED, DISCONNECTED, UNDEFINED
    }

    init {
        _networkStatus.postValue(NetworkStatus.UNDEFINED)
    }

    fun updateNetworkStatus(status: NetworkStatus) {
        _networkStatus.postValue(status)
    }
}