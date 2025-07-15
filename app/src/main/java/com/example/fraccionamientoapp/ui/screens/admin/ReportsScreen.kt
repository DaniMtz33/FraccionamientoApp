package com.example.fraccionamientoapp.ui.screens.admin

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.fraccionamientoapp.R
import java.io.File
import java.io.IOException

data class ReporteAcceso(
    val nombre: String,
    val fecha: String,
    val tipo: String, // Entrada / Salida
    val estado: String // Autorizado / Rechazado
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(onBack: () -> Unit) {
    val reportesSimulados = listOf(
        ReporteAcceso("Carlos Ramírez", "21/05/2025 14:30", "Entrada", "Autorizado"),
        ReporteAcceso("Visita Juan Pérez", "21/05/2025 15:10", "Entrada", "Pendiente"),
        ReporteAcceso("María López", "21/05/2025 16:45", "Salida", "Autorizado")
    )

    val context = LocalContext.current
    var showExportDialog by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.ver_reportes)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { showExportDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.exportar_pdf))
            }

            Button(
                onClick = { showShareDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.compartir_pdf))
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(reportesSimulados) { reporte ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("${stringResource(R.string.nombre)}: ${reporte.nombre}")
                            Text("${stringResource(R.string.fecha)}: ${reporte.fecha}")
                            Text("${stringResource(R.string.tipo)}: ${reporte.tipo}")
                            Text("${stringResource(R.string.estado)}: ${reporte.estado}")
                        }
                    }
                }
            }

            if (showExportDialog) {
                AlertDialog(
                    onDismissRequest = { showExportDialog = false },
                    confirmButton = {
                        TextButton(onClick = {
                            generarPDF(reportesSimulados, context)
                            showExportDialog = false
                        }) {
                            Text(stringResource(R.string.aceptar))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showExportDialog = false }) {
                            Text(stringResource(R.string.cancelar))
                        }
                    },
                    title = { Text(stringResource(R.string.confirmar_exportacion)) },
                    text = { Text(stringResource(R.string.texto_confirmar_exportacion)) }
                )
            }

            if (showShareDialog) {
                AlertDialog(
                    onDismissRequest = { showShareDialog = false },
                    confirmButton = {
                        TextButton(onClick = {
                            compartirPDF(context)
                            showShareDialog = false
                        }) {
                            Text(stringResource(R.string.aceptar))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showShareDialog = false }) {
                            Text(stringResource(R.string.cancelar))
                        }
                    },
                    title = { Text(stringResource(R.string.confirmar_compartir)) },
                    text = { Text(stringResource(R.string.texto_confirmar_compartir)) }
                )
            }
        }
    }
}

fun generarPDF(reportes: List<ReporteAcceso>, context: Context) {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas
    val paint = Paint()

    var y = 50f
    paint.textSize = 16f
    paint.isFakeBoldText = true
    canvas.drawText("Reporte de Accesos", 200f, y, paint)

    paint.textSize = 12f
    paint.isFakeBoldText = false
    y += 30f

    reportes.forEachIndexed { index, r ->
        val linea = "${index + 1}. ${r.nombre} | ${r.fecha} | ${r.tipo} | ${r.estado}"
        canvas.drawText(linea, 40f, y, paint)
        y += 20f
    }

    pdfDocument.finishPage(page)

    try {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Files.FileColumns.DISPLAY_NAME, "ReporteAccesos.pdf")
            put(MediaStore.Files.FileColumns.MIME_TYPE, "application/pdf")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Files.FileColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/Fraccionamiento")
            }
        }

        val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

        if (uri != null) {
            resolver.openOutputStream(uri)?.use { outputStream ->
                pdfDocument.writeTo(outputStream)
                Toast.makeText(context, "PDF exportado en Documentos/Fraccionamiento", Toast.LENGTH_LONG).show()
            } ?: throw IOException("No se pudo abrir el stream de salida")
        } else {
            throw IOException("No se pudo crear el archivo")
        }
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, "Error al exportar PDF", Toast.LENGTH_LONG).show()
    } finally {
        pdfDocument.close()
    }
}

fun compartirPDF(context: Context) {
    val archivo = File(
        context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
        "Fraccionamiento/ReporteAccesos.pdf"
    )

    if (!archivo.exists()) {
        Toast.makeText(context, "Primero exporta el PDF", Toast.LENGTH_SHORT).show()
        return
    }

    val uri: Uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        archivo
    )

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(Intent.createChooser(intent, "Compartir PDF con..."))
}
