package es.upsa.mimo.mimo18_androidarch.detail

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import es.upsa.mimo.mimo18_androidarch.R
import es.upsa.mimo.mimo18_androidarch.databinding.CharacterDetailListHeaderSectionBinding
import es.upsa.mimo.mimo18_androidarch.databinding.CharacterDetailListSectionItemBinding
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection


class CharacterDetailSection(
        var title: String,
        private var items: List<String>,
        private var itemClick: (value: String) -> Unit
) : StatelessSection(
        SectionParameters.builder()
                .itemViewWillBeProvided()
                .headerViewWillBeProvided()
                .build()) {

    override fun getContentItemsTotal(): Int {
        return items.size
    }

    override fun getItemView(parent: ViewGroup): View {
        return LayoutInflater
                .from(parent.context)
                .inflate(R.layout.character_detail_list_section_item, parent, false)
    }

    override fun getItemViewHolder(view: View): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.bind<CharacterDetailListSectionItemBinding>(view)!!
        return ItemViewHolder(view, binding)
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val name = items[position]
            holder.bind(ItemDataBindingModelRow(itemName = name, itemClick = itemClick))
        }
    }

    override fun getHeaderView(parent: ViewGroup): View {
        return LayoutInflater
                .from(parent.context)
                .inflate(R.layout.character_detail_list_header_section, parent, false)
    }

    override fun getHeaderViewHolder(view: View): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.bind<CharacterDetailListHeaderSectionBinding>(view)!!
        return HeaderViewHolder(view, binding)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?) {
        if (holder is HeaderViewHolder) {
            holder.tvTitle.text = title
            holder.bind(title)
        }
    }

    private inner class HeaderViewHolder constructor(
            view: View,
            private val binding: CharacterDetailListHeaderSectionBinding
    ) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById<View>(R.id.tvTitle) as TextView

        fun bind(item: String) {
            binding.item = item
            binding.executePendingBindings()
        }

    }

    private inner class ItemViewHolder constructor(
            rootView: View,
            private val binding: CharacterDetailListSectionItemBinding
    ) : RecyclerView.ViewHolder(rootView) {

        fun bind(item: ItemDataBindingModelRow) {
            binding.itemDataRow = item
            binding.executePendingBindings()
        }

    }

    data class ItemDataBindingModelRow(
            var itemName: String,
            var itemClick: (value: String) -> Unit
    )

}