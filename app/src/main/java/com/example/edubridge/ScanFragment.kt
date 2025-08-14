package com.example.edubridge

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.edubridge.databinding.FragmentProblemScannerBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class ScanFragment : Fragment() {

    private var _binding: FragmentProblemScannerBinding? = null
    private val binding get() = _binding!!

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { processImage(it) }
    }

    private val takePhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as? android.graphics.Bitmap
            imageBitmap?.let {
                val image = InputImage.fromBitmap(it, 0)
                recognizeText(image)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProblemScannerBinding.inflate(inflater, container, false)

        binding.scanCameraButton.setOnClickListener { openCamera() }
        binding.uploadGalleryButton.setOnClickListener { pickImageFromGallery() }
        binding.scanNewButton.setOnClickListener {
            binding.scannedResultContainer.visibility = View.GONE
        }

        return binding.root
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), 101)
        } else {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePhotoLauncher.launch(cameraIntent)
        }
    }

    private fun pickImageFromGallery() {
        pickImageLauncher.launch("image/*")
    }

    private fun processImage(uri: Uri) {
        try {
            val imageStream = requireContext().contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(imageStream)
            val image = InputImage.fromBitmap(bitmap, 0)
            recognizeText(image)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error loading image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun recognizeText(image: InputImage) {
        showLoading(true)
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                binding.scannedProblemText.text = visionText.text.ifEmpty { "No text detected" }
                binding.scannedResultContainer.visibility = View.VISIBLE
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Scan failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            .addOnCompleteListener {
                showLoading(false)
            }
    }

    private fun showLoading(show: Boolean) {
        binding.scanningProgress.visibility = if (show) View.VISIBLE else View.GONE
        binding.scannerIcon.visibility = if (show) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
