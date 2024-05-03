package com.example.cliente.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.apirest.Capture
import com.example.apirest.DeleteItem
import com.example.apirest.Update
import com.example.cliente.R
import com.example.cliente.Videogame
import com.squareup.picasso.Picasso

class AdapterVideogame (
    var videogameList: ArrayList<Videogame>,
    private val mOnClickListener: Capture,
    private val onClickDelete : DeleteItem,
    private val onClickUpdate : Update,
): RecyclerView.Adapter<AdapterVideogame.VideogameHolder>() {

    inner class VideogameHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {

        var videogameImage : ImageView
        var videogameID : TextView
        var videogameTitle : TextView
        var videogameDeveloper : TextView
        var videogamePublisher : TextView
        var deleteButton : Button
        var updateButton : Button

        init {
            videogameImage = view.findViewById(R.id.holderImage)
            videogameID = view.findViewById(R.id.holderID)
            videogameTitle = view.findViewById(R.id.holderTitle)
            videogameDeveloper = view.findViewById(R.id.holderDeveloper)
            videogamePublisher = view.findViewById(R.id.holderPublisher)
            deleteButton = view.findViewById(R.id.holderDelete)
            updateButton  =view.findViewById(R.id.holderUpdate)

            videogameImage.setOnClickListener(this)
            deleteButton.setOnClickListener {
                val position = this.adapterPosition
                onClickDelete.deleteItem(position)
            }
            updateButton.setOnClickListener {
                val position = this.adapterPosition
                onClickUpdate.updateItem(position)
            }
        }

        override fun onClick(view : View) {
            val position = this.adapterPosition
            mOnClickListener.clickEnElemento(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideogameHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.layout_videogameholder, parent, false)
        return VideogameHolder(item)
    }

    override fun onBindViewHolder(holder: VideogameHolder, position: Int) {
        val selected = videogameList[position]
        holder.videogameID.text = selected.id.toString()
        holder.videogameTitle.text = selected.title
        holder.videogameDeveloper.text = selected.developer
        holder.videogamePublisher.text = selected.publisher
        var urlImage = selected.urlImage
        holder.videogameImage.setBackgroundResource(0)
        Picasso.get().load(urlImage)
            .placeholder(R.drawable.videogames)
            .error(R.drawable.videogames)
            .into(holder.videogameImage)
    }

    override fun getItemCount(): Int = videogameList.size

    fun videogameUpdate(videogameList: ArrayList<Videogame>) {
        this.videogameList = videogameList
        notifyDataSetChanged()
    }

    fun filterVideogames(videogameList: ArrayList<Videogame>) {
        this.videogameList = videogameList
        notifyDataSetChanged()
    }
}