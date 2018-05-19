package es.upsa.mimo.mimo18_androidarch.list.model

import android.databinding.BindingAdapter
import android.widget.ImageView
import es.upsa.mimo.mimo18_androidarch.R
import es.upsa.mimo.mimo18_androidarch.util.ImageLoader


data class CharacterListBindingModel(
        val id: String,
        val name: String,
        val imageUrl: String? = null,
        val imageLoader: ImageLoader
) {

    companion object {

        @BindingAdapter(value = ["imageUrl", "imageLoader"])
        @JvmStatic
        fun loadImage(
                imageView: ImageView,
                imageUrl: String?,
                imageLoader: ImageLoader?
        ) {
            if (imageUrl != null && imageLoader != null) {
                imageLoader.loadImageToImageView(
                        imageUrl = imageUrl,
                        imageView = imageView
                )
            } else {
                imageView.setImageResource(R.mipmap.avengers_logo)
            }
        }

    }

}