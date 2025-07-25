import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.YuvImage
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.attendanceappoffline.presentaion.viewModels.FaceRecognitionViewModel
import com.example.attendanceappoffline.presentaion.viewModels.GlobalStateViewModel
import com.example.attendanceappoffline.presentaion.viewModels.AttendanceViewModel
import com.example.attendanceappoffline.presentaion.viewModels.AuthViewModel
import com.example.attendanceappoffline.presentaion.viewModels.StudentViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

class FaceAnalyzer(
    private val tflite: Interpreter,
    private val globalStateViewModel: GlobalStateViewModel,
    private  val attendanceViewModel: AttendanceViewModel,
    private val studentViewModel: StudentViewModel,
    private val faceRecognitionViewModel: FaceRecognitionViewModel,
    private val authViewModel: AuthViewModel
) : ImageAnalysis.Analyzer {
//    private val faceDetector = FaceDetection.getClient(
//        com.google.mlkit.vision.face.FaceDetectorOptions.Builder()
//            .setPerformanceMode(com.google.mlkit.vision.face.FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
//            .setLandmarkMode(com.google.mlkit.vision.face.FaceDetectorOptions.LANDMARK_MODE_ALL)
//            .setClassificationMode(com.google.mlkit.vision.face.FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
//            .enableTracking()
//            .build()
//    )

    private val faceDetector = FaceDetection.getClient()



    @OptIn(ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {
        Log.d("FaceRecognition", "analyze() is running...") // Add this log

        val mediaImage = image.image
        if (mediaImage != null) {
            val inputImage = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)

            faceDetector.process(inputImage)

                .addOnSuccessListener {faces ->
                    Log.d("FaceRecognition", "Faces detected: ${faces.size}")

                    val bitmap = imageProxyToBitmap(image)
                    val faceRects = faces.map { it.boundingBox }
//                    globalStateViewModel.updateFaceBounds(faceRects)

                    bitmap?.let { nonNullBitmap ->
                        for (face in faces) {
                            if (!isFaceInCenter(face, nonNullBitmap.width, nonNullBitmap.height)) {
                                Log.d("FaceRecognition", "Face skipped (not centered)")
                                continue
                            }

                            Log.d("FaceRecognition", "Face in center detected")
                            val leftEyeOpen = face.leftEyeOpenProbability ?: -1f
                            val rightEyeOpen = face.rightEyeOpenProbability ?: -1f
                            val smiling = face.smilingProbability ?: -1f

                            val isBlinking = (leftEyeOpen < 0.3f && rightEyeOpen < 0.3f)
                            val isSmiling = (smiling > 0.6f)

                            val faceBitmap = cropFaceFromImage(nonNullBitmap, face)
                            faceBitmap?.let { validFaceBitmap ->
                                val embedding = getFaceEmbedding(validFaceBitmap)
                                Log.d("FaceRecognition", "Got face embedding")
//                                if (faceRecognitionViewModel.isRegistering) {
                                if (faceRecognitionViewModel.isRegistering.value) {
                                    faceRecognitionViewModel.registerFace(
                                        studentId = studentViewModel.studentId,
                                        fullName = studentViewModel.fullName,
                                        rollNumber = studentViewModel.rollNumber,
                                        embedding=embedding,
                                        faceBitmap = validFaceBitmap,
                                        className = studentViewModel.className,
                                        section = studentViewModel.section,
                                        schoolId = studentViewModel.schoolId,
                                        date = globalStateViewModel.dropdownDate
                                    )
//                                    faceRecognitionViewModel.updateIsRegistering(false)
                                    faceRecognitionViewModel.setRegistering(false)
                                    attendanceViewModel.loadStudentsWithAttendance(className = studentViewModel.className+"-"+studentViewModel.section, date = globalStateViewModel.dropdownDate)
//                                    attendanceViewModel.loadStudentsWithAttendance(className = studentViewModel.selectedClassNameWithSection, date = globalStateViewModel.dropdownDate)

                                    Log.d("FaceRecognition", "Face registered successfully!")
                                } else {
                                        CoroutineScope(Dispatchers.IO).launch {
                                        faceRecognitionViewModel.recognizeFace(currentEmbedding = embedding, facesFromDB = globalStateViewModel.faceEmbeddings.value, className = studentViewModel.className, section = studentViewModel.section, dropDown = globalStateViewModel.dropdownDate, schoolId = authViewModel.schoolId.value)
                                    }
                                }


                            }
                        }
                    }

                }
                .addOnFailureListener { exception ->
                    // Handle error
                    Log.e("FaceRecognition", "Face detection failed", exception)
                }
                .addOnCompleteListener {
                    image.close()
                }
        }
    }

    private fun isFaceInCenter(face: Face, imageWidth: Int, imageHeight: Int): Boolean {
        val centerX = imageWidth / 2
        val centerY = imageHeight / 2
        val centerRegionSize = 200 // you can tune this (in pixels)

        val centerRect = android.graphics.Rect(
            centerX - centerRegionSize / 2,
            centerY - centerRegionSize / 2,
            centerX + centerRegionSize / 2,
            centerY + centerRegionSize / 2
        )

        return centerRect.contains(face.boundingBox.centerX(), face.boundingBox.centerY())
    }


    private fun cropFaceFromImage(bitmap: Bitmap?, face: Face): Bitmap? {
        Log.d("FaceRecognition", "Processing face...")

        // If the bitmap is null, return null
        if (bitmap == null) return null

        val boundingBox = face.boundingBox
        val left = boundingBox.left.coerceAtLeast(0)
        val top = boundingBox.top.coerceAtLeast(0)
        val right = boundingBox.right.coerceAtMost(bitmap.width)
        val bottom = boundingBox.bottom.coerceAtMost(bitmap.height)

        return Bitmap.createBitmap(bitmap, left, top, right - left, bottom - top)
    }

    @OptIn(ExperimentalGetImage::class)
    fun imageProxyToBitmap(imageProxy: ImageProxy): Bitmap? {
        val image = imageProxy.image ?: return null
        val planes = image.planes

        val yBuffer = planes[0].buffer
        val uBuffer = planes[1].buffer
        val vBuffer = planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        // Copy Y buffer
        yBuffer.get(nv21, 0, ySize)
        // Copy U buffer (VU format, so we need to swap U and V)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val outputStream = ByteArrayOutputStream()
        yuvImage.compressToJpeg(android.graphics.Rect(0, 0, image.width, image.height), 100, outputStream)
        val jpegArray = outputStream.toByteArray()

        return BitmapFactory.decodeByteArray(jpegArray, 0, jpegArray.size)
    }

    private fun getFaceEmbedding(faceBitmap: Bitmap): FloatArray {
        val resizedBitmap = Bitmap.createScaledBitmap(faceBitmap, 160, 160, true)
        val byteBuffer = convertBitmapToByteBuffer(resizedBitmap)

        val embedding = Array(1) { FloatArray(128) }
        tflite.run(byteBuffer, embedding)

        return embedding[0]
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(1 * 160 * 160 * 3 * 4)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(160 * 160)
        bitmap.getPixels(intValues, 0, 160, 0, 0, 160, 160)

        for (pixel in intValues) {
            val r = (pixel shr 16 and 0xFF) / 255.0f
            val g = (pixel shr 8 and 0xFF) / 255.0f
            val b = (pixel and 0xFF) / 255.0f

            byteBuffer.putFloat(r)
            byteBuffer.putFloat(g)
            byteBuffer.putFloat(b)
        }

        return byteBuffer
    }
}
