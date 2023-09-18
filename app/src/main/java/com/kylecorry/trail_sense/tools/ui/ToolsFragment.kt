package com.kylecorry.trail_sense.tools.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.kylecorry.andromeda.core.capitalizeWords
import com.kylecorry.andromeda.core.system.Resources
import com.kylecorry.andromeda.core.tryOrNothing
import com.kylecorry.andromeda.core.ui.Colors
import com.kylecorry.andromeda.fragments.BoundFragment
import com.kylecorry.andromeda.list.ListView
import com.kylecorry.trail_sense.R
import com.kylecorry.trail_sense.databinding.FragmentToolsBinding
import com.kylecorry.trail_sense.databinding.ListItemToolBinding
import com.kylecorry.trail_sense.shared.navigation.NavControllerAppNavigation


class ToolsFragment : BoundFragment<FragmentToolsBinding>() {

    private val tools by lazy { Tools.getTools(requireContext()) }
    private val navigation by lazy { NavControllerAppNavigation(findNavController()) }
    private val mapper by lazy { ToolListItemMapper(requireContext(), navigation) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchbox.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                updateToolList()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                updateToolList()
                return true
            }

        })

        updateToolList()
    }

    private fun updateToolList() {
        val toolListItems = mutableListOf<ToolListItem>()
        val search = binding.searchbox.query

        if (search.isNullOrBlank()) {
            for (group in tools) {
                toolListItems.add(ToolListItem(group.name, null, null, null))
                for (tool in group.tools) {
                    toolListItems.add(
                        ToolListItem(
                            tool.name,
                            tool.description,
                            tool.icon,
                            tool.navAction
                        )
                    )
                }
            }
        } else {
            for (group in tools) {
                for (tool in group.tools) {
                    if (tool.name.contains(search, true) || tool.description?.contains(
                            search,
                            true
                        ) == true
                    ) {
                        toolListItems.add(
                            ToolListItem(
                                tool.name,
                                tool.description,
                                tool.icon,
                                tool.navAction
                            )
                        )
                    }
                }
            }
        }

        binding.toolList.setItems(toolListItems, mapper)
    }

    override fun generateBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentToolsBinding {
        return FragmentToolsBinding.inflate(layoutInflater, container, false)
    }

    data class ToolListItem(
        val name: String,
        val description: String?,
        @DrawableRes val icon: Int?,
        @IdRes val action: Int?
    )

}