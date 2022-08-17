package com.nodes.sunrise.components.adapters.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nodes.sunrise.R
import com.nodes.sunrise.components.comparators.ChallengesWithGroupComparator
import com.nodes.sunrise.components.listeners.OnEntityClickListener
import com.nodes.sunrise.databinding.ListItemChallengeBinding
import com.nodes.sunrise.databinding.ListItemChallengeGroupBinding
import com.nodes.sunrise.db.entity.Challenge
import com.nodes.sunrise.enums.ChallengeViewType
import com.nodes.sunrise.model.ChallengeAndGroup

class ChallengeListAdapter :
    ListAdapter<ChallengeAndGroup, RecyclerView.ViewHolder>(
        ChallengesWithGroupComparator()
    ) {

    lateinit var onClickListener: OnEntityClickListener<Challenge>
    lateinit var selectedChallenge: Challenge
    var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ChallengeViewType.GROUP.ordinal -> {
                val holder = ChallengeGroupViewHolder.create(parent)
                holder
            }
            else -> {
                val holder = ChallengeViewHolder.create(parent)
                holder.binding.root.setOnClickListener {
                    updateSelection(holder.absoluteAdapterPosition)
                }
                holder
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ChallengeViewType.GROUP.ordinal -> {
                with((holder as ChallengeGroupViewHolder).binding) {
                    val currentGroup = getItem(position).challengeGroup!!
                    listItemChallengeCategoryTVName.text = currentGroup.name
                }
            }
            else -> {
                with((holder as ChallengeViewHolder).binding) {
                    val currentChallenge = getItem(position).challenge!!
                    listItemChallengeTVChallengeName.text = currentChallenge.name
                    listItemChallengeTVRecentSuccessDate.text = "*아직 도전하지 않음."

                    listItemChallengeRB.isChecked = (position == selectedPosition)
                }
            }
        }
    }

    override fun onCurrentListChanged(
        previousList: MutableList<ChallengeAndGroup>,
        currentList: MutableList<ChallengeAndGroup>
    ) {
        val pos: Int
        if (::selectedChallenge.isInitialized) {

            for (i in 0 until currentList.count()) {
                if (currentList[i].challenge != null && currentList[i].challenge == selectedChallenge) {
                    pos = i
                    updateSelection(pos)
                    break
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return currentList[position].viewType.ordinal
    }

    private fun updateSelection(newPosition: Int) {
        // 기존에 선택된 값이 있는 경우, 이전 position의 데이터가 변경됨을 notify
        if (selectedPosition >= 0) notifyItemChanged(selectedPosition)

        selectedPosition = newPosition
        selectedChallenge = currentList[newPosition].challenge!!

        notifyItemChanged(selectedPosition)

        onClickListener.onClick()
    }

    class ChallengeGroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = ListItemChallengeGroupBinding.bind(itemView)

        companion object {
            fun create(parent: ViewGroup): ChallengeGroupViewHolder {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_challenge_group, parent, false)
                return ChallengeGroupViewHolder(itemView)
            }
        }
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