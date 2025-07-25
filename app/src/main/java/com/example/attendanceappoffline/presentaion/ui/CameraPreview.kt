import android.Manifest
import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.attendanceappoffline.presentaion.viewModels.FaceRecognitionViewModel
import com.example.attendanceappoffline.presentaion.viewModels.GlobalStateViewModel
import com.example.attendanceappoffline.presentaion.viewModels.AttendanceViewModel
import com.example.attendanceappoffline.presentaion.viewModels.AuthViewModel
import com.example.attendanceappoffline.presentaion.viewModels.StudentViewModel
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.concurrent.Executors


@Composable
fun CameraPreview(globalStateViewModel: GlobalStateViewModel,attendanceViewModel: AttendanceViewModel,studentViewModel: StudentViewModel,faceRecognitionViewModel: FaceRecognitionViewModel,authViewModel: AuthViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val preview = remember { Preview.Builder().build() }
//    val cameraSelector = remember {
//        CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()
//    }
    val cameraProvider = cameraProviderFuture.get()

    val cameraSelector = remember {
        try {
            val cameraProvider = cameraProviderFuture.get()
            val cameraCount = cameraProvider.availableCameraInfos.size
            if (cameraCount > 0) {
                // Select the first available camera (which could be external webcam)
                CameraSelector.Builder().build()
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    val tfliteModel = loadModelFile(context, "facenet.tflite")

//    val imageAnalyzer = remember {
//        ImageAnalysis.Builder().build().apply {
//            setAnalyzer(Executors.newSingleThreadExecutor(), FaceAnalyzer(tfliteModel,globalStateViewModel,attendanceViewModel, studentViewModel,faceRecognitionViewModel))
//        }
//    }

    val previewView = remember { PreviewView(context) }  // Move to top

    LaunchedEffect(Unit) {
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            if (cameraProvider.availableCameraInfos.isEmpty()) {
                Toast.makeText(context, "No camera found", Toast.LENGTH_LONG).show()
                globalStateViewModel.updateIsCameraAvailable()
                return@addListener
            }

            try {
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider) // ✅ Move here
                }

                val imageAnalyzer = ImageAnalysis.Builder().build().apply {
                    setAnalyzer(
                        Executors.newSingleThreadExecutor(),
                        FaceAnalyzer(loadModelFile(context, "facenet.tflite"), globalStateViewModel, attendanceViewModel, studentViewModel, faceRecognitionViewModel, authViewModel = authViewModel)
                    )
                }

                val cameraSelector = CameraSelector.Builder().build()

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalyzer
                )

                globalStateViewModel.updateIsCameraAvailable()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Failed to bind camera: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                globalStateViewModel.updateIsCameraAvailable()
            }
        }, ContextCompat.getMainExecutor(context))
    }

//    LaunchedEffect(Unit) {
//        cameraProviderFuture.addListener({
//            val cameraProvider = cameraProviderFuture.get()
//
//            val availableCameras = cameraProvider.availableCameraInfos
//            if (availableCameras.isEmpty()) {
//                Toast.makeText(context, "No camera found. Try connecting USB camera.", Toast.LENGTH_LONG).show()
//                globalStateViewModel.updateIsCameraAvailable()
//                return@addListener
//            }
//
//            try {
//                val cameraSelector = CameraSelector.Builder().build() // fallback generic selector
//                val imageAnalyzer = ImageAnalysis.Builder().build().apply {
//                    setAnalyzer(
//                        Executors.newSingleThreadExecutor(),
//                        FaceAnalyzer(loadModelFile(context, "facenet.tflite"), globalStateViewModel, attendanceViewModel, studentViewModel, faceRecognitionViewModel)
//                    )
//                }
//
//                val preview = Preview.Builder().build()
//
//                cameraProvider.unbindAll()
//                cameraProvider.bindToLifecycle(
//                    lifecycleOwner,
//                    cameraSelector,
//                    preview,
//                    imageAnalyzer
//                )
//                globalStateViewModel.updateIsCameraAvailable()
//            } catch (e: Exception) {
//                e.printStackTrace()
//                Toast.makeText(context, "Failed to bind camera: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
//                globalStateViewModel.updateIsCameraAvailable()
//            }
//        }, ContextCompat.getMainExecutor(context))
//    }


    androidx.compose.runtime.DisposableEffect(Unit) {
        onDispose {
            cameraProvider.unbindAll()
        }
    }

    Box(
        modifier = Modifier
            .width(444.dp)  // Set a fixed width for the container
            .height(254.dp) // Set a fixed height for the container (matching 16:9)
            .background(Color.Transparent)
//            .padding(16.dp) // Optional padding
    ) {
        AndroidView(
//            factory = { context ->
//                val previewView = PreviewView(context).apply {
//                    layoutParams = ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,  // Take full width of parent Box
//                        ViewGroup.LayoutParams.MATCH_PARENT   // Take full height of parent Box
//                    )
//                }
//                preview.setSurfaceProvider(previewView.surfaceProvider)
//                previewView
//            },
            factory = { previewView }, // ✅ Use the remembered one
            modifier = Modifier
                .fillMaxSize() // Make PreviewView match its parent (Box)
                .clip(RoundedCornerShape(12.dp)) // Optional rounded corners
        )
    }

}

fun loadModelFile(context: Context, modelName: String): Interpreter {
    val assetFileDescriptor = context.assets.openFd(modelName)
    val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
    val fileChannel = fileInputStream.channel
    val startOffset = assetFileDescriptor.startOffset
    val declaredLength = assetFileDescriptor.declaredLength
    val mappedByteBuffer: MappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    return Interpreter(mappedByteBuffer)
}
