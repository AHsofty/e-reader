package com.example.e_reader.Activities.BookTypes

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import java.io.File
import java.io.IOException

class PdfParser(private val context: Context, private val uri: Uri) : BookParser {
    private var pdfRenderer: PdfRenderer? = null
    private var currentPage: PdfRenderer.Page? = null

    init {
        try {
            val fileDescriptor: ParcelFileDescriptor? = context.contentResolver.openFileDescriptor(uri, "r")
            pdfRenderer = fileDescriptor?.let { PdfRenderer(it) }
        }
        catch (e: IOException) {
            e.printStackTrace()
        }
    }


    override fun getCoverImage(): Bitmap {
        // We return the first page available
        currentPage?.close()
        currentPage = pdfRenderer?.openPage(0)
        val bitmap: Bitmap = Bitmap.createBitmap(currentPage!!.width, currentPage!!.height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        currentPage?.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        return bitmap
    }

    override fun getTitle(): String? {
        return Uri.parse(uri.toString()).path?.let { File(it).name };
    }

    override fun getAuthor(): String {
        return "[Unknown Author]"
    }

    override fun getPublisher(): String {
        return "[Unknown Publisher]"
    }

    override fun getPublicationDate(): String {
        return "[Unknown Publication date]"
    }

    override fun getDescription(): String {
        return "[Unknown Description]"
    }

    override fun getIdentifier(): String {
        return "[Unknown Identifier]"
    }

    override fun getType(): String {
        return "[Unknown Type]"
    }

    override fun setUri(uri: Uri?) {
        TODO("Not yet implemented")
    }

    fun getAllPages(): MutableList<Bitmap> {
        val images = mutableListOf<Bitmap>()
        pdfRenderer?.let { renderer ->
            val pageCount = renderer.pageCount
            for (i in 0 until pageCount) {
                currentPage?.close()
                currentPage = renderer.openPage(i)

                val bitmap: Bitmap = Bitmap.createBitmap(currentPage!!.width, currentPage!!.height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                canvas.drawColor(Color.WHITE)

                currentPage?.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

                images.add(bitmap)
            }
        }
        return images
    }



}