package com.kylecorry.trail_sense.tools.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import com.kylecorry.andromeda.fragments.BoundFragment
import com.kylecorry.trail_sense.R
import com.kylecorry.trail_sense.databinding.FragmentToolsBinding
import com.kylecorry.trail_sense.shared.UserPreferences
import com.kylecorry.trail_sense.tools.ui.sort.AlphabeticalToolSort
import com.kylecorry.trail_sense.tools.ui.sort.ToolSortFactory

class ToolsFragment : BoundFragment<FragmentToolsBinding>() {

    private val tools by lazy { Tools.getTools(requireContext()) }
    private val prefs by lazy { UserPreferences(requireContext()) }

    private val pinnedToolManager by lazy { PinnedToolManager(prefs) }

    private val toolSortFactory by lazy { ToolSortFactory(requireContext()) }

    private val pinnedSorter = AlphabeticalToolSort()

    override fun generateBinding(
        layoutInflater: LayoutInflater, container: ViewGroup?
    ): FragmentToolsBinding {
        return FragmentToolsBinding.inflate(layoutInflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.composeView.setContent {
            var currentTools by remember {
                mutableStateOf(tools)
            }
            var pinnedTools by remember {
                mutableStateOf(tools.filter { pinnedToolManager.isPinned(it.id) })
            }
            MaterialTheme (colorScheme = darkColorScheme()) {
                Column {

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        // Pinned header
                        item(span = { GridItemSpan(2) }) {
                            Text(
                                text = stringResource(id = R.string.pinned),
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(8.dp)
                            )
                        }

                        items(pinnedTools) { tool ->
                            ToolItem(tool = tool, onPinStateChanged = {
                                if (it) {
                                    pinnedToolManager.pin(tool.id)
                                } else {
                                    pinnedToolManager.unpin(tool.id)
                                }
                                pinnedTools = tools.filter { pinnedToolManager.isPinned(it.id) }
                            })
                        }
                        item(span = { GridItemSpan(2) }) {
                            Text(
                                text = stringResource(id = R.string.tools),
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(
                                    top = 16.dp,
                                    start = 8.dp,
                                    end = 8.dp,
                                    bottom = 8.dp
                                )
                            )
                        }
                        items(currentTools) { tool ->
                            ToolItem(tool = tool, onPinStateChanged = {
                                if (it) {
                                    pinnedToolManager.pin(tool.id)
                                } else {
                                    pinnedToolManager.unpin(tool.id)
                                }
                                pinnedTools = tools.filter { pinnedToolManager.isPinned(it.id) }
                            })
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ToolItem(tool: Tool, onPinStateChanged: (isPinned: Boolean) -> Unit = {}) {
        Surface(
            modifier = Modifier
                .padding(8.dp)
                .combinedClickable(
                    onClick = {
                        findNavController().navigate(tool.navAction)
                    },
                    onLongClick = {
                        // Show a menu
                        onPinStateChanged(!pinnedToolManager.isPinned(tool.id))
                    }
                ),
            tonalElevation = 2.dp,
            shape = RoundedCornerShape(4.dp)) {
            Row(
                modifier = Modifier
                    .height(64.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = tool.icon),
                    contentDescription = tool.name,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = tool.name,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }

    @Preview
    @Composable
    fun ToolItemPreview() {
        val tool = Tool(
            1,
            "Test2",
            R.drawable.maps,
            R.id.action_settings,
            ToolCategory.Location
        )
        ToolItem(tool = tool)
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        updatePinnedTools()
//        updateTools()
//
//        updateQuickActions()
//
//        binding.settingsBtn.setOnClickListener {
//            findNavController().navigate(R.id.action_settings)
//        }
//
//        binding.searchbox.setOnQueryTextListener { _, _ ->
//            updateTools()
//            true
//        }
//
//        binding.pinnedEditBtn.setOnClickListener {
//            // Sort alphabetically, but if the tool is already pinned, put it first
//            val sorted = tools.sortedBy { tool ->
//                if (pinnedToolManager.isPinned(tool.id)) {
//                    "0${tool.name}"
//                } else {
//                    tool.name
//                }
//            }
//            val toolNames = sorted.map { it.name }
//            val defaultSelected = sorted.mapIndexedNotNull { index, tool ->
//                if (pinnedToolManager.isPinned(tool.id)) {
//                    index
//                } else {
//                    null
//                }
//            }
//
//            Pickers.items(
//                requireContext(), getString(R.string.pinned), toolNames, defaultSelected
//            ) { selected ->
//                if (selected != null) {
//                    pinnedToolManager.setPinnedToolIds(selected.map { sorted[it].id })
//                }
//
//                updatePinnedTools()
//            }
//        }
//
//        binding.sortBtn.setOnClickListener {
//            changeToolSort()
//        }
//
//        CustomUiUtils.oneTimeToast(
//            requireContext(),
//            getString(R.string.tool_long_press_hint_toast),
//            "tools_long_press_notice_shown",
//            short = false
//        )
//
//    }
//
//    // TODO: Add a way to customize this
//    private fun updateQuickActions() {
//        ToolsQuickActionBinder(this, binding).bind()
//    }
//
//    private fun changeToolSort() {
//        val sortTypes = ToolSortType.values()
//        val sortTypeNames = mapOf(
//            ToolSortType.Name to getString(R.string.name),
//            ToolSortType.Category to getString(R.string.category)
//        )
//
//        Pickers.item(
//            requireContext(),
//            getString(R.string.sort),
//            sortTypes.map { sortTypeNames[it] ?: "" },
//            sortTypes.indexOf(prefs.toolSort)
//        ) { selectedIdx ->
//            if (selectedIdx != null) {
//                prefs.toolSort = sortTypes[selectedIdx]
//                updateTools()
//            }
//        }
//    }
//
//    private fun updateTools() {
//        val filter = binding.searchbox.query
//
//        // Hide pinned when searching
//        if (filter.isNullOrBlank()) {
//            binding.pinned.isVisible = true
//            binding.pinnedTitle.isVisible = true
//            binding.pinnedEditBtn.isVisible = true
//        } else {
//            binding.pinned.isVisible = false
//            binding.pinnedTitle.isVisible = false
//            binding.pinnedEditBtn.isVisible = false
//        }
//
//        val tools = if (filter.isNullOrBlank()) {
//            this.tools
//        } else {
//            this.tools.filter {
//                it.name.contains(filter, true) || it.description?.contains(filter, true) == true
//            }
//        }
//
//        val sorter = toolSortFactory.getToolSort(prefs.toolSort)
//        populateTools(sorter.sort(tools), binding.tools)
//    }
//
//    private fun updatePinnedTools() {
//        val pinned = tools.filter {
//            pinnedToolManager.isPinned(it.id)
//        }
//
//        binding.pinned.isVisible = pinned.isNotEmpty()
//
//        populateTools(pinnedSorter.sort(pinned), binding.pinned)
//    }
//
//    private fun populateTools(categories: List<CategorizedTools>, grid: GridLayout) {
//        inBackground {
//            val viewsToAdd = mutableListOf<View>()
//
//            onDefault {
//                if (categories.size == 1) {
//                    categories.first().tools.forEach {
//                        viewsToAdd.add(createToolButton(it))
//                    }
//                } else {
//                    categories.forEach {
//                        viewsToAdd.add(createToolCategoryHeader(it.categoryName))
//                        it.tools.forEach {
//                            viewsToAdd.add(createToolButton(it))
//                        }
//                    }
//                }
//            }
//
//            onMain {
//                grid.removeAllViews()
//                viewsToAdd.forEach {
//                    grid.addView(it)
//                }
//            }
//
//        }
//    }
//
//    private fun createToolCategoryHeader(name: String?): View {
//        // TODO: Move this to the class level
//        val headerMargins = Resources.dp(requireContext(), 8f).toInt()
//
//        val gridColumnSpec = GridLayout.spec(GridLayout.UNDEFINED, 2, 1f)
//        val gridRowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
//
//        val header = TextView(requireContext())
//        header.text = name?.capitalizeWords()
//        header.textSize = 14f
//        header.setTextColor(
//            Resources.getAndroidColorAttr(
//                requireContext(), android.R.attr.colorPrimary
//            )
//        )
//        // Bold
//        header.paint.isFakeBoldText = true
//        header.layoutParams = GridLayout.LayoutParams().apply {
//            width = 0
//            height = GridLayout.LayoutParams.WRAP_CONTENT
//            columnSpec = gridColumnSpec
//            rowSpec = gridRowSpec
//            setMargins(headerMargins, headerMargins * 2, headerMargins, headerMargins)
//        }
//        header.gravity = Gravity.CENTER_VERTICAL
//
//        return header
//    }
//
//    private fun createToolButton(tool: Tool): View {
//        // TODO: Move this to the class level
//        val iconSize = Resources.dp(requireContext(), 24f).toInt()
//        val iconPadding = Resources.dp(requireContext(), 12f).toInt()
//        val iconColor = Resources.androidTextColorPrimary(requireContext())
//        val buttonHeight = Resources.dp(requireContext(), 64f).toInt()
//        val buttonMargins = Resources.dp(requireContext(), 8f).toInt()
//        val buttonPadding = Resources.dp(requireContext(), 16f).toInt()
//        val buttonBackgroundColor = Resources.getAndroidColorAttr(
//            requireContext(), android.R.attr.colorBackgroundFloating
//        )
//
//        val gridColumnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
//        val gridRowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
//
//        val button = TextView(requireContext())
//        button.text = tool.name.capitalizeWords()
//        button.setCompoundDrawables(iconSize, left = tool.icon)
//        button.compoundDrawablePadding = iconPadding
//        button.elevation = 2f
//        CustomUiUtils.setImageColor(button, iconColor)
//        button.layoutParams = GridLayout.LayoutParams().apply {
//            width = 0
//            height = buttonHeight
//            columnSpec = gridColumnSpec
//            rowSpec = gridRowSpec
//            setMargins(buttonMargins)
//        }
//        button.gravity = Gravity.CENTER_VERTICAL
//        button.setPadding(buttonPadding, 0, buttonPadding, 0)
//
//        button.setBackgroundResource(R.drawable.rounded_rectangle)
//        button.backgroundTintList = ColorStateList.valueOf(buttonBackgroundColor)
//        button.setOnClickListener { _ ->
//            findNavController().navigate(tool.navAction)
//        }
//
//        button.setOnLongClickListener { view ->
//            Pickers.menu(
//                view, listOf(
//                    if (tool.isExperimental) getString(R.string.experimental) else null,
//                    if (tool.description != null) getString(R.string.pref_category_about) else null,
//                    if (pinnedToolManager.isPinned(tool.id)) {
//                        getString(R.string.unpin)
//                    } else {
//                        getString(R.string.pin)
//                    },
//                    if (tool.guideId != null) getString(R.string.tool_user_guide_title) else null,
//                )
//            ) { selectedIdx ->
//                when (selectedIdx) {
//                    1 -> dialog(tool.name, tool.description, cancelText = null)
//                    2 -> {
//                        if (pinnedToolManager.isPinned(tool.id)) {
//                            pinnedToolManager.unpin(tool.id)
//                        } else {
//                            pinnedToolManager.pin(tool.id)
//                        }
//                        updatePinnedTools()
//                    }
//
//                    3 -> {
//                        UserGuideUtils.showGuide(this, tool.guideId!!)
//                    }
//                }
//                true
//            }
//            true
//        }
//
//        return button
//    }

}