package io.digitalbinary.camera.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.digitalbinary.camera.R
import java.io.File

class ImageAdapter(private val imageUrls: List<String>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val text: TextView = itemView.findViewById(R.id.textview_second)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        val file = File(imageUrl)  // Create a File object
        val uri = Uri.fromFile(file)  // Convert File to Uri

        Picasso.get()
            .load(file)
            .placeholder(R.mipmap.ic_launcher_round) // Placeholder image
            .error(android.R.drawable.ic_menu_report_image) // Error image
            .into(holder.imageView, object : Callback {
                override fun onSuccess() {
                    // Handle success if needed
                    Log.d("ImageAdapter", "Image loaded successfully: $uri")
                }
                override fun onError(e: Exception?) {
                    // Handle error if needed
                    Log.e("ImageAdapter", "Error loading image: $uri", e)
                }
            })

        holder.text.setText(file.name)
    }

    override fun getItemCount(): Int = imageUrls.size
}
