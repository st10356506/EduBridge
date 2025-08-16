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

    // Store the selected file URI
    private var selectedFileUri: Uri? = null

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            // Store the URI
            selectedFileUri = it
            // Show the image preview and upload button
            showImagePreview(it)
        }
    }

    private val takePhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as? android.graphics.Bitmap
            imageBitmap?.let {
                // If you want to preview a photo, you'll need to save the bitmap to a file and get its URI
                // For this example, we'll stick to processing it directly.
                val image = InputImage.fromBitmap(it, 0)
                recognizeText(image)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProblemScannerBinding.inflate(inflater, container, false)

        binding.scanCameraButton.setOnClickListener { openCamera() }
        binding.uploadGalleryButton.setOnClickListener { pickImageFromGallery() }

        // Set click listener for the new upload button
        binding.btnUpload.setOnClickListener {
            selectedFileUri?.let { uri ->
                uploadFileToFirebase(uri)
            }
        }

        binding.scanNewButton.setOnClickListener {
            // Hide the result container and the upload preview
            binding.scannedResultContainer.visibility = View.GONE
            binding.uploadPreviewContainer.visibility = View.GONE
            binding.scannerInterfaceCard.visibility = View.VISIBLE
            selectedFileUri = null
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
        // Hide the scanned result container before opening the gallery
        binding.scannedResultContainer.visibility = View.GONE
        pickImageLauncher.launch("image/*")
    }

    // New function to display the selected image and show the upload section
    private fun showImagePreview(uri: Uri) {
        binding.scannerInterfaceCard.visibility = View.GONE // Hide the initial scanner interface
        binding.uploadPreviewContainer.visibility = View.VISIBLE // Show the preview container
        binding.imgPreview.setImageURI(uri) // Set the image to the ImageView
    }

    // Placeholder for your Firebase upload logic
    private fun uploadFileToFirebase(uri: Uri) {
        // Implement your Firebase Storage upload logic here.
        // For example:
        // val storageRef = FirebaseStorage.getInstance().reference.child("uploads/${System.currentTimeMillis()}.jpg")
        // storageRef.putFile(uri).addOnSuccessListener { ... }.addOnFailureListener { ... }

        Toast.makeText(requireContext(), "Uploading file...", Toast.LENGTH_SHORT).show()

        // After upload is complete, you might want to hide the preview and show the result
        // or a success message. For now, let's just toast a message.
        // For demonstration, let's assume the upload is successful and we want to process the image.
        processImage(uri)
    }

    private fun processImage(uri: Uri) {
        try {
            val image = InputImage.fromFilePath(requireContext(), uri)
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
                binding.uploadPreviewContainer.visibility = View.GONE // Hide the preview once text is recognized
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