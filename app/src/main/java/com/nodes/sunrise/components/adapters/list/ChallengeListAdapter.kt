package com.nodes.sunrise.components.adapters.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nodes.sunrise.R
import com.nodes.sunrise.components.comparators.ChallengeComparator
import com.nodes.sunrise.components.listeners.OnEntityClickListener
import com.nodes.sunrise.databinding.ListItemChallengeBinding
import com.nodes.sunrise.db.entity.Challenge

class ChallengeListAdapter :
    ListAdapter<Challenge, ChallengeListAdapter.ChallengeViewHolder>(ChallengeComparator()) {

    lateinit var onClickListener: OnEntityClickListener<Challenge>
    var selectedPosition = -1
    lateinit var selectedChallenge: Challenge

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val holder = ChallengeViewHolder.create(parent)

        holder.binding.root.setOnClickListener {
            updateSelection(holder.absoluteAdapterPosition)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        val currentChallenge = getItem(position)
        with(holder.binding) {
            listItemChallengeTVChallengeName.text = currentChallenge.name
            listItemChallengeTVRecentSuccessDate.text = "*아직 도전하지 않음."

            listItemChallengeRB.isChecked = (position == selectedPosition)
        }
    }

    override fun onCurrentListChanged(
        previousList: MutableList<Challenge>,
        currentList: MutableList<Challenge>
    ) {
        if (::selectedChallenge.isInitialized) {
            val pos = currentList.indexOf(selectedChallenge)
            updateSelection(pos)
        }
    }

    private fun updateSelection(newPosition: Int) {
        // 기존에 선택된 값이 있는 경우, 이전 position의 데이터가 변경됨을 notify
        if (selectedPosition >= 0) notifyItemChanged(selectedPosition)

        selectedPosition = newPosition
        selectedChallenge = currentList[newPosition]

        notifyItemChanged(selectedPosition)

        onClickListener.onClick()
    }

    class ChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = ListItemChallengeBinding.bind(itemView)

        companion object {
            fun create(parent: ViewGroup): ChallengeViewHolder {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_challenge, parent, false)
                return ChallengeViewHolder(itemView)
            }
        }
    }
}