package com.nodes.sunrise.ui.purchases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.nodes.sunrise.components.adapters.list.PurchaseItemListAdapter
import com.nodes.sunrise.components.decorators.ListMarginDecorator
import com.nodes.sunrise.databinding.FragmentPurchaseBinding
import com.nodes.sunrise.enums.ListOrientation
import com.nodes.sunrise.model.PurchaseItem
import com.nodes.sunrise.ui.BaseFragment

class PurchaseFragment : BaseFragment() {

    private var _binding: FragmentPurchaseBinding? = null
    val binding: FragmentPurchaseBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPurchaseBinding.inflate(inflater)

        setToolbarBinding(binding.fragPurchaseTB)

        setRecyclerView()

        return binding.root
    }

    private fun setRecyclerView() {
        val rv = binding.fragPurchaseRVPurchaseItems
        val adapter = PurchaseItemListAdapter()
        val mockData = ArrayList<PurchaseItem>().apply {
            add(PurchaseItem("광고 제거", "광고를 모두 제거합니다.", "₩2,500"))
            add(PurchaseItem("테마 기능 추가", "어두운 테마, 폰트 종류 및 크기, 배경색 선택 기능을 추가합니다.", "₩1,500"))
            add(PurchaseItem("나와의 약속 기능 추가", "나 자신과 약속을 하고 이를 지켰는지 여부를 확인하는 기능을 추가합니다.", "₩2,000"))
            add(PurchaseItem("도전과제 기능 추가", "나 자신을 이겨내는 도전과제를 선택하고 이를 성공했는지 여부를 확인하는 기능을 추가합니다.", "₩2,000"))
            add(PurchaseItem("모든 프리미엄 기능 추가", "위 모든 프리미엄 기능을 추가합니다.", "₩5,000", "₩8,000"))
        }

        with(rv) {
            this.adapter = adapter
            isNestedScrollingEnabled = false
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(ListMarginDecorator(resources, ListOrientation.VERTICAL))
        }

        adapter.submitList(mockData)
    }
}