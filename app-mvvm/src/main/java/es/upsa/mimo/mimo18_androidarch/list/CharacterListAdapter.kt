package es.upsa.mimo.mimo18_androidarch.list

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.upsa.mimo.mimo18_androidarch.R
import es.upsa.mimo.mimo18_androidarch.databinding.CharacterListListItemBinding
import es.upsa.mimo.mimo18_androidarch.marvel.bindingModel.CharacterBindingModel

class CharacterListAdapter(
        private val itemListener: OnItemClickListener?
) : RecyclerView.Adapter<CharacterListAdapter.CharacterVH>() {

    private var characters: List<CharacterBindingModel> = emptyList()

    interface OnItemClickListener {
        fun onItemClicked(charId: String)
    }

    class CharacterVH(
            itemView: View,
            private val binding: CharacterListListItemBinding,
            private val itemListener: OnItemClickListener?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: CharacterBindingModel) {

            with(binding) {
                character = item
                imageLoader = item.imageLoader
                itemListener?.let {
                    itemClickListener = it
                }
                executePendingBindings()
            }

        }

    }

    fun characters(characters: List<CharacterBindingModel>) {
        this.characters = characters
        notifyDataSetChanged() // This is bad. Used for simplicity.
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterVH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.character_list_list_item, parent, false)
        val binding = DataBindingUtil.bind<CharacterListListItemBinding>(view)!!
        return CharacterVH(
                itemView = view,
                binding = binding,
                itemListener = itemListener
        )
    }

    override fun onBindViewHolder(holder: CharacterVH, position: Int) {

        holder.bind(characters[position])
    }

    override fun getItemCount(): Int = characters.size

    companion object {
        private val TAG = CharacterListAdapter::class.java.canonicalName
    }


}
