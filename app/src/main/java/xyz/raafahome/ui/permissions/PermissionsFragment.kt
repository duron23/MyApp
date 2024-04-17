package xyz.raafahome.ui.permissions

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import xyz.raafahome.databinding.FragmentPermissionsBinding

class PermissionsFragment : Fragment() {

    private var _binding: FragmentPermissionsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PermissionsViewModel by viewModels()

    private lateinit var requestMultiplePermissionsLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var settingsResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupPermissionLaunchers()
    }

    private fun setupPermissionLaunchers() {
        requestMultiplePermissionsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->



            val anyPermissionDenied = permissions.any { !it.value }


            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R  ){
                binding.requestPermissionButton.visibility = if (anyPermissionDenied && Environment.isExternalStorageManager() ) View.VISIBLE else View.INVISIBLE
            }else {
                binding.requestPermissionButton.visibility = if (anyPermissionDenied  ) View.VISIBLE else View.INVISIBLE
            }


            handlePermissionsResult(permissions)
            updatePermissionStatus(permissions)

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            settingsResultLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == Activity.RESULT_OK && Environment.isExternalStorageManager()) {
                        Toast.makeText(
                            requireContext(),
                            "Storage Manager granted",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Storage Manager denied",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPermissionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.permissionsViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.requestPermissionButton.setOnClickListener {
            checkPermissionsAndShowRationale()
        }
    }

    private fun handlePermissionsResult(permissions: Map<String, Boolean>) {
        permissions.entries.forEach { entry ->
            val permissionResult = if (entry.value) "granted" else "denied"
            Toast.makeText(requireContext(), "${entry.key} $permissionResult", Toast.LENGTH_SHORT)
                .show()
        }
    }


    private fun checkAndRequestManageExternalStoragePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            settingsResultLauncher.launch(intent)
        }
    }

    private fun checkPermissionsAndShowRationale() {
        val permissionsToRequest = mutableListOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        checkAndRequestManageExternalStoragePermission()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionsToRequest.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        val rationaleNeeded = permissionsToRequest.any { shouldShowRequestPermissionRationale(it) }

        if (rationaleNeeded) {
            showRationaleDialog {
                requestMultiplePermissionsLauncher.launch(permissionsToRequest.toTypedArray())
            }
        } else {
            requestMultiplePermissionsLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

    private fun showRationaleDialog(onPositive: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission Required")
            .setMessage("This app needs access to your storage and Bluetooth to function properly. Please grant these permissions.")
            .setPositiveButton("OK") { _, _ -> onPositive() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updatePermissionStatus(permissions: Map<String, Boolean>) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            binding.btConnectPermissionTextView.visibility = View.VISIBLE
        }else {
            binding.btConnectPermissionTextView.visibility = View.INVISIBLE
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            binding.extrenalStorageManagerPermission.visibility = View.VISIBLE
            if (Environment.isExternalStorageManager()) {
                binding.extrenalStorageManagerPermission.setTextColor(Color.BLUE)
            } else {
                binding.extrenalStorageManagerPermission.setTextColor(Color.RED)
            }
        }else {
            binding.extrenalStorageManagerPermission.visibility = View.INVISIBLE
        }

        permissions.entries.forEach { entry ->
            val color = if (entry.value) {
                Color.BLUE
            } else {
                Color.RED
            }

            when (entry.key) {
                Manifest.permission.READ_EXTERNAL_STORAGE -> {
                    binding.readPermissionTextView.setTextColor(color)
                }

                Manifest.permission.WRITE_EXTERNAL_STORAGE -> {

                    binding.writePermissionTextView.setTextColor(color)
                }

                Manifest.permission.BLUETOOTH_CONNECT -> {

                    binding.btConnectPermissionTextView.setTextColor(color)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
