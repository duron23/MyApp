package xyz.raafahome.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object BTPrinterState {
    private val _printerStatus = MutableLiveData(PrinterStatus.DISCONNECTED)
    val printerStatus: LiveData<PrinterStatus> = _printerStatus

    enum class PrinterStatus {
        CONNECTED, DISCONNECTED, PRINTING, ERROR, HEAD_OPEN
    }

    fun updatePrinterStatus(status: PrinterStatus) {
        _printerStatus.postValue(status)
    }
}