package com.example.constitutionmaker.utils

import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.webkit.WebView
import java.io.File
import java.io.FileOutputStream

object PDFGenerator {
    private const val TAG = "PDFGenerator"

    /**
     * Generates a PDF from a WebView that has already loaded content.
     * This method captures the entire scrollable content of the WebView and handles multi-page pagination.
     */
    fun generateFromWebView(webView: WebView, filePath: String, callback: (Boolean) -> Unit) {
        val handler = Handler(Looper.getMainLooper())
        
        handler.post {
            try {
                // Ensure WebView has dimensions
                val webViewWidth = webView.width
                val contentHeight = webView.contentHeight
                
                if (webViewWidth <= 0 || contentHeight <= 0) {
                    Log.w(TAG, "WebView not ready (dimensions <= 0), retrying in 500ms...")
                    handler.postDelayed({
                        generateFromWebView(webView, filePath, callback)
                    }, 500)
                    return@post
                }

                // A4 dimensions in points (72 DPI: 595x842)
                val pageWidth = 595
                val pageHeight = 842
                
                // Calculate scale to fit WebView width into A4 page width
                val scale = pageWidth.toFloat() / webViewWidth.toFloat()
                
                // Convert CSS pixels from contentHeight to device pixels
                val density = webView.resources.displayMetrics.density
                val totalHeightPx = (contentHeight * density).toInt()

                val pdfDocument = PdfDocument()
                
                // capturePicture() is deprecated but very reliable for capturing full WebView content
                // as it captures the entire scrollable area.
                @Suppress("DEPRECATION")
                val picture = webView.capturePicture()
                
                if (picture.width <= 0 || picture.height <= 0) {
                    // Fallback to drawing the view directly if picture is empty
                    Log.w(TAG, "Picture empty, falling back to drawViewToPdf")
                    drawViewToPdf(webView, pdfDocument, totalHeightPx, pageWidth, pageHeight, scale, filePath, callback)
                    return@post
                }

                val pageHeightInWebViewPx = pageHeight / scale
                var currentY = 0f
                var pageCount = 0
                
                // Draw content onto PDF pages using the captured picture
                while (currentY < totalHeightPx && pageCount < 100) {
                    val remainingHeight = totalHeightPx - currentY
                    val drawHeightPx = Math.min(pageHeightInWebViewPx, remainingHeight)
                    
                    val pdfPageHeight = (drawHeightPx * scale).toInt()
                    if (pdfPageHeight <= 0) break

                    val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pdfPageHeight, pageCount + 1).create()
                    val page = pdfDocument.startPage(pageInfo)
                    
                    val canvas = page.canvas
                    canvas.save()
                    canvas.scale(scale, scale)
                    canvas.translate(0f, -currentY)
                    
                    picture.draw(canvas)
                    
                    canvas.restore()
                    pdfDocument.finishPage(page)
                    
                    currentY += drawHeightPx
                    pageCount++
                }

                savePdfAndNotify(pdfDocument, filePath, callback)
            } catch (e: Exception) {
                Log.e(TAG, "Error during PDF generation", e)
                callback(false)
            }
        }
    }

    private fun drawViewToPdf(webView: WebView, pdfDocument: PdfDocument, totalHeightPx: Int, pageWidth: Int, pageHeight: Int, scale: Float, filePath: String, callback: (Boolean) -> Unit) {
        // Use software layer to ensure the entire content is rendered for drawing
        val originalLayerType = webView.layerType
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        
        val pageHeightInWebViewPx = pageHeight / scale
        var currentY = 0f
        var pageCount = 0
        
        while (currentY < totalHeightPx && pageCount < 100) {
            val remainingHeight = totalHeightPx - currentY
            val drawHeightPx = Math.min(pageHeightInWebViewPx, remainingHeight)
            
            val pdfPageHeight = (drawHeightPx * scale).toInt()
            if (pdfPageHeight <= 0) break

            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pdfPageHeight, pageCount + 1).create()
            val page = pdfDocument.startPage(pageInfo)
            
            val canvas = page.canvas
            canvas.save()
            canvas.scale(scale, scale)
            canvas.translate(0f, -currentY)
            
            webView.draw(canvas)
            
            canvas.restore()
            pdfDocument.finishPage(page)
            
            currentY += drawHeightPx
            pageCount++
        }
        
        webView.setLayerType(originalLayerType, null)
        savePdfAndNotify(pdfDocument, filePath, callback)
    }

    private fun savePdfAndNotify(pdfDocument: PdfDocument, filePath: String, callback: (Boolean) -> Unit) {
        try {
            val file = File(filePath)
            file.parentFile?.mkdirs()
            
            FileOutputStream(file).use { out ->
                pdfDocument.writeTo(out)
            }
            pdfDocument.close()
            callback(true)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save PDF file", e)
            pdfDocument.close()
            callback(false)
        }
    }
}
