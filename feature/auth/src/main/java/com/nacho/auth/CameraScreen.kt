package com.nacho.auth

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.nacho.auth.components.StoriButton
import com.nacho.common.utils.rotateBitmap

@Composable
fun CameraScreen(
    viewModel: CameraViewModel = hiltViewModel(),
    onUsePicture: () -> Unit = {},
    onPreviousSection: () -> Unit = {},
    onPhotoCaptured: (Bitmap) -> Unit,
) {

    val cameraState: CameraState by viewModel.state.collectAsState()

    CameraContent(
        onPhotoCaptured = {
            viewModel.onPhotoCaptured(it)
            onPhotoCaptured(it)
        },
        onPreviousSection = onPreviousSection,
    )

    cameraState.capturedImage?.let { capturedImage: Bitmap ->
        CapturedImageBitmapDialog(
            capturedImage = capturedImage,
            onDismissRequest = viewModel::onCapturedPhotoConsumed,
            onUsePicture = {
                viewModel.onCapturedPhotoConsumed()
                onUsePicture.invoke()
            }
        )
    }
}

@Composable
private fun CapturedImageBitmapDialog(
    capturedImage: Bitmap,
    onDismissRequest: () -> Unit,
    onUsePicture: () -> Unit = {},
) {

    val capturedImageBitmap: ImageBitmap = remember { capturedImage.asImageBitmap() }

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
//        (LocalView.current.parent as DialogWindowProvider).window.setDimAmount(1f)
        Box {
            Image(
                bitmap = capturedImageBitmap,
                contentDescription = "Captured photo"
            )
            StoriButton(
                text = "Use picture", onClick = onUsePicture,
                modifier = Modifier
                    .align(alignment = Alignment.BottomEnd)
                    .padding(end = 8.dp)
            )
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun CameraContent(
    onPhotoCaptured: (Bitmap) -> Unit,
    onPreviousSection: () -> Unit = {}
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }

    Box {
        AndroidView(
            factory = { context ->
                PreviewView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    setBackgroundColor(Color.BLACK)
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    scaleType = PreviewView.ScaleType.FILL_START
                }.also { previewView ->
                    previewView.controller = cameraController
                    cameraController.bindToLifecycle(lifecycleOwner)
                }
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.BottomEnd),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            StoriButton(text = "Go back", onClick = onPreviousSection)
            StoriButton(text = "Take picture",
                onClick = {
                    val mainExecutor = ContextCompat.getMainExecutor(context)
                    cameraController.takePicture(
                        mainExecutor,
                        object : ImageCapture.OnImageCapturedCallback() {
                            override fun onCaptureSuccess(image: ImageProxy) {
                                val correctedBitmap: Bitmap = image
                                    .toBitmap()
                                    .rotateBitmap(image.imageInfo.rotationDegrees)

                                onPhotoCaptured(correctedBitmap)

                                image.close()
                            }

                            override fun onError(exception: ImageCaptureException) {
                                Log.e("CameraContent", "Error capturing image", exception)
                            }
                        })
                })
        }
    }
}

@Preview
@Composable
private fun Preview_CameraContent() {
    CameraContent(
        onPhotoCaptured = {}
    )
}