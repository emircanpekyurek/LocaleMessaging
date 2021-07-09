package com.pekyurek.emircan.presentation.ui.message.adapter

import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.pekyurek.emircan.R
import com.pekyurek.emircan.databinding.ItemReceiveMessageBinding
import com.pekyurek.emircan.databinding.ItemSentMessageBinding
import com.pekyurek.emircan.domain.model.response.chatdata.Message
import com.pekyurek.emircan.presentation.core.extensions.inflateDataBinding

class MessageAdapter(private val userId: String) : RecyclerView.Adapter<BaseMessageViewHolder>() {

    private val list = mutableListOf<Message>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseMessageViewHolder {
        return when (viewType) {
            MessageType.RECEIVE.ordinal -> ReceivedMessageViewHolder(parent.inflateDataBinding(R.layout.item_receive_message))
            MessageType.SEND.ordinal -> SentMessageViewHolder(parent.inflateDataBinding(R.layout.item_sent_message))
            else -> throw IllegalStateException("wrong view type")
        }
    }

    override fun onBindViewHolder(holder: BaseMessageViewHolder, position: Int) {
        val message = list[position]
        val previousUserId = list.getOrNull(position - 1)?.user?.id
        val thisUserId = list[position].user.id
        holder.initBinding(previousUserId == thisUserId, message)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (userId == list[position].user.id) MessageType.SEND.ordinal else MessageType.RECEIVE.ordinal
    }

    fun setAllMessage(messageList: List<Message>) {
        list.clear()
        list.addAll(messageList)
        notifyDataSetChanged()
    }

    fun addMessage(message: Message) {
        list.add(message)
        notifyItemInserted(list.lastIndex)
    }

    class SentMessageViewHolder(private val binding: ItemSentMessageBinding) : BaseMessageViewHolder(binding.root) {
        override val tvUserNickname: AppCompatTextView get() = binding.tvNickname
        override val tvMessage: AppCompatTextView get() = binding.tvMessage
        override val ivUserAvatar: AppCompatImageView get() = binding.ivUserAvatar
        override val tvMessageTime: AppCompatTextView get() = binding.tvMessageTime

    }

    class ReceivedMessageViewHolder(private val binding: ItemReceiveMessageBinding) : BaseMessageViewHolder(binding.root) {

        override val tvUserNickname: AppCompatTextView get() = binding.tvNickname
        override val tvMessage: AppCompatTextView get() = binding.tvMessage
        override val ivUserAvatar: AppCompatImageView get() = binding.ivUserAvatar
        override val tvMessageTime: AppCompatTextView get() = binding.tvMessageTime

    }

    enum class MessageType {
        SEND, RECEIVE
    }

}