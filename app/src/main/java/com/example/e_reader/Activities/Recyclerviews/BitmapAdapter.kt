import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.e_reader.R
import com.github.chrisbanes.photoview.PhotoView

class BitmapAdapter(private val bitmaps: List<Bitmap>) : RecyclerView.Adapter<BitmapAdapter.BitmapViewHolder>() {

    inner class BitmapViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val photoView: PhotoView = view.findViewById(R.id.bitmapPhotoView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BitmapViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bitmap_item, parent, false)
        return BitmapViewHolder(view)
    }

    override fun onBindViewHolder(holder: BitmapViewHolder, position: Int) {
        holder.photoView.setImageBitmap(bitmaps[position])
    }

    override fun getItemCount(): Int {
        return bitmaps.size
    }
}