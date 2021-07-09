package com.pekyurek.emircan.presentation.ui.message.adapter

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.pekyurek.emircan.domain.model.response.chatdata.Message
import com.pekyurek.emircan.presentation.core.extensions.loadImageFromUrl
import com.pekyurek.emircan.presentation.core.extensions.setVisibleOrGone
import com.pekyurek.emircan.presentation.core.extensions.setVisibleOrInvisible
import com.pekyurek.emircan.presentation.core.extensions.toMessageTimeFormat

abstract class BaseMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract val tvUserNickname: AppCompatTextView
    abstract val tvMessage: AppCompatTextView
    abstract val ivUserAvatar: AppCompatImageView
    abstract val tvMessageTime: AppCompatTextView

    fun initBinding(sameWithPreviousUser: Boolean, message: Message) {
        ivUserAvatar.setVisibleOrInvisible(sameWithPreviousUser.not()) {
            ivUserAvatar.loadImageFromUrl(message.user.avatarURL, android.R.drawable.ic_menu_view)
        }

        tvUserNickname.setVisibleOrGone(sameWithPreviousUser.not()) {
            tvUserNickname.text = message.user.nickname
        }

        tvMessage.text = message.text

        tvMessageTime.text = message.timestamp.toMessageTimeFormat()
    }

}