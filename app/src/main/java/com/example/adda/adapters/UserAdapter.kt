package com.example.adda.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adda.R
import com.example.adda.models.UserInfo
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(
    var userInfos: ArrayList<UserInfo>,
    var context: Context,
    var itemUserClickListener: ItemUserClickListener,
    var b: Boolean
) : RecyclerView.Adapter<UserAdapter.ViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userInfo = userInfos[position]
        holder.tvName.text = userInfo.name
        holder.deptTv.text = userInfo.department

        if (b){
            holder.infoIv.visibility = View.VISIBLE
        }else{
            holder.infoIv.visibility = View.GONE
        }
        Glide.with(context)
            .load(userInfo.imageUri)
            .placeholder(R.drawable.round_perm_identity_24)
            .fitCenter()
            .error(R.drawable.round_perm_identity_24)
            .into(holder.profileImg)
        if (userInfo.isActiveStatus) {
            holder.activeBeige.visibility = View.VISIBLE
        } else {
            holder.activeBeige.visibility = View.GONE
        }
        holder.itemView.setOnClickListener { view: View? ->
            itemUserClickListener.onUserClick(
                userInfo
            )
        }
        holder.infoIv.setOnClickListener {
            itemUserClickListener.onUserInfoClick(userInfo)
        }
    }

    override fun getItemCount(): Int {
        return userInfos.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView
        var deptTv: TextView
        var profileImg: CircleImageView
        var activeBeige: ImageView
        var infoIv: ImageView

        init {
            tvName = itemView.findViewById(R.id.nameTv)
            deptTv = itemView.findViewById(R.id.deptTv)
            profileImg = itemView.findViewById(R.id.profileImg)
            activeBeige = itemView.findViewById(R.id.activeBeige)
            infoIv = itemView.findViewById(R.id.infoIv)
        }
    }

    interface ItemUserClickListener {
        fun onUserClick(userInfo: UserInfo?)
        fun onUserInfoClick(userInfo: UserInfo?)
    }
}