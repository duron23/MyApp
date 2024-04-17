package xyz.raafahome.ui.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import xyz.raafahome.R
import xyz.raafahome.databinding.FragmentHomeBinding
import xyz.raafahome.service.PrinterConnectionService
import xyz.raafahome.state.ServiceState

class HomeFragment : Fragment() {

    private val TAG = "HomeFragment"

    /*companion object {
        fun newInstance() = HomeFragment()
    }*/

    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.homeViewModel = viewModel

        viewModel.isPrinterServiceRunning.observe(viewLifecycleOwner) { isRunning ->
            if (isRunning) {
                "Disconnect".also { binding.connectButton.text = it }
                disableRadioGroups()
            } else if (!isRunning) {
                "Connect".also { binding.connectButton.text = it }
                enableRadioGroups()
            }
        }

        viewModel.connectionType.observe(viewLifecycleOwner) {
            val selectedRadioButtonId = when (it) {
                ServiceState.ConnectionType.BLUETOOTH -> R.id.btOnlyRadioButton
                ServiceState.ConnectionType.NETWORK -> R.id.networkOnlyRadioButton
                ServiceState.ConnectionType.USB -> null
                ServiceState.ConnectionType.BLUETOOTH_NETWORK -> R.id.btAndNetworkRadioButton
                ServiceState.ConnectionType.UNDEFINED -> null
            }

            selectedRadioButtonId?.let { id ->
                binding.deviceTypeRadioGroup.check(id)
            } ?: run {
                binding.deviceTypeRadioGroup.clearCheck() // Clear selection if undefined
            }
        }

        viewModel.businessType.observe(viewLifecycleOwner) {
            val selectedRadioButtonId = when (it) {
                ServiceState.BusinessType.FC -> R.id.fcOptionRadioButton
                ServiceState.BusinessType.MH -> R.id.mhOptionRadioButton
                ServiceState.BusinessType.LDC -> R.id.largeOptionRadioButton
                ServiceState.BusinessType.GROCERY -> R.id.groceryOptionRadioButton
                ServiceState.BusinessType.UNDEFINED -> null
            }

            selectedRadioButtonId?.let { id ->
                binding.buTypeRadioGroup.check(id)
            } ?: run {
                binding.buTypeRadioGroup.clearCheck() // Clear selection if undefined
            }
        }

        binding.connectButton.setOnClickListener {
            if (ServiceState.isPrinterServiceRunning.value == true) {
                "Disconnect".also { binding.connectButton.text = it }
                stopService()
            } else {
                "Connect".also { binding.connectButton.text = it }
                startService()
            }


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startService() {
        val serviceIntent = Intent(context, PrinterConnectionService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context?.startForegroundService(serviceIntent)
        } else {
            context?.startService(serviceIntent)
        }
    }

    private fun stopService() {
        val serviceIntent = Intent(requireContext(), PrinterConnectionService::class.java)
        requireContext().stopService(serviceIntent)
    }

    private fun disableRadioGroups(){
        for (i in 0 until binding.deviceTypeRadioGroup.childCount) {
            val child = binding.deviceTypeRadioGroup.getChildAt(i)
            child.isEnabled = false // This re-enables each RadioButton within the group
        }

        for (i in 0 until binding.buTypeRadioGroup.childCount) {
            val child = binding.buTypeRadioGroup.getChildAt(i)
            child.isEnabled = false // This re-enables each RadioButton within the group
        }
    }
    private fun enableRadioGroups(){
        for (i in 0 until binding.deviceTypeRadioGroup.childCount) {
            val child = binding.deviceTypeRadioGroup.getChildAt(i)
            child.isEnabled = true // This re-enables each RadioButton within the group
        }

        for (i in 0 until binding.buTypeRadioGroup.childCount) {
            val child = binding.buTypeRadioGroup.getChildAt(i)
            child.isEnabled = true // This re-enables each RadioButton within the group
        }
    }
}