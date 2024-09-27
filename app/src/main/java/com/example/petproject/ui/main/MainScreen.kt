package com.example.petproject.ui.main

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.petproject.R
import com.example.petproject.model.NoteUi
import com.example.petproject.model.TagUi
import com.example.petproject.ui.theme.PetProjectTheme

@Composable
fun MainScreen(
    notes: List<NoteUi>
) {
//
//    val nestedScrollConnection = remember {
//        object : NestedScrollConnection {
//            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
//
//            }
//
//
//        }
//    }

    Scaffold(
        bottomBar = { BottomBar() }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            Box(modifier = Modifier) {

                LazyColumn(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    item {
                        SearchBar(
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                        )
                    }
                    item {
                        NotesCategoryName(
                            name = "Закрепленные",modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                        )
                    }

                    categoryNotesBlock(notes.filter { it.pinned })

                    item {
                        NotesCategoryName(
                            name = "Другие",modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                        )
                    }
                    categoryNotesBlock(notes.filter { !it.pinned })
                }
            }

        }
    }
}

inline fun LazyListScope.categoryNotesBlock(noteUis: List<NoteUi>) {
    items(noteUis) { note ->
        Note(modifier = Modifier.padding(bottom = 8.dp),note)
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    Row(modifier = modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(50))
        .background(Color(0xFF142229)),
    verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(tint = Color(0xFFC0CBD1),
                imageVector = ImageVector.vectorResource(id = R.drawable.menu_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                contentDescription = "open side-bar"
            )
        }

        Text(text = "Искать в заметках", color = Color(0xFFC0CBD1))

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = { /*TODO*/ }) {
            Icon(tint = Color(0xFFC0CBD1),
                imageVector = ImageVector.vectorResource(id = R.drawable.splitscreen_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                contentDescription = "change note display type"
            )

        }

        IconButton(onClick = { /*TODO*/ }) {
            Icon(tint = Color(0xFFC0CBD1),
                imageVector = ImageVector.vectorResource(id = R.drawable.account_circle_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                contentDescription = "google profiles"
            )

        }
    }
}

@Composable
fun NotesCategoryName(
    modifier: Modifier,
    name: String
) {
    Box(modifier = modifier) {
        Text(text = name)
    }
}

@Composable
fun Note(
    modifier: Modifier = Modifier,
    noteUi: NoteUi
) {
    BoxWithConstraints {
        val parentWidth = maxWidth
        Column(
            modifier = modifier
                .width(parentWidth)
                .clip(RoundedCornerShape(5.dp))
                .border(
                    width = 1.dp,
                    color = Color(0xFF40464A),
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(vertical = 8.dp, horizontal = 8.dp)
        ) {
            Text(
                text = noteUi.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFDBE1E5)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = noteUi.content,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFDBE1E5)
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (noteUi.tags.size != 0) {
                TagsLayout(tags = noteUi.tags)
            }
        }
    }
}

@Composable
fun TagsLayout(tags: List<TagUi>) {
    // currentX and width
    val lastPlaceableValues = remember {
        mutableStateOf(Pair(0, 0))
    }

    SubcomposeLayout { constraints ->

        var currentX = 0
        var currentY= 0

        val tagPlaceables = tags.map { tag ->
            subcompose(tag) { Tag(name = tag.name) }.first().measure(constraints)
        }

        // all has one height
        val height = tagPlaceables[0].height

        var visibleTags = 0

        layout(constraints.maxWidth, height) {
            tagPlaceables.forEach { placeable ->

                if (currentX + placeable.width <= constraints.maxWidth) {

                    placeable.placeRelative(currentX, currentY)
                    lastPlaceableValues.value = Pair(currentX, placeable.width)
                    currentX += placeable.width + 8.dp.roundToPx()
                    visibleTags++

                } else {
                    if (tags.size > visibleTags) {
                        val andMore = subcompose(lastPlaceableValues) {
                            Tag(modifier = Modifier
                                .width((constraints.maxWidth - lastPlaceableValues.value.first).toDp()),
                                name = "and ${visibleTags - 1} more")
                        }.first().measure(constraints)

                        andMore.placeRelative(lastPlaceableValues.value.first, currentY)
                    }
                    return@forEach
                }
            }
        }
    }
}

@Composable
fun BottomBar() {
    BottomAppBar(
        floatingActionButton = { FAB() },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        id = R.drawable.check_box_24dp_e8eaed_fill0_wght400_grad0_opsz24
                    ),
                    contentDescription = "Add new list"
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        id = R.drawable.brush_24dp_e8eaed_fill0_wght400_grad0_opsz24
                    ),
                    contentDescription = "Create a painting"
                )            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        id = R.drawable.mic_24dp_e8eaed_fill0_wght400_grad0_opsz24
                    ),
                    contentDescription = "Create an audio-note"
                )            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        id = R.drawable.image_24dp_e8eaed_fill0_wght400_grad0_opsz24
                    ),
                    contentDescription = "Create a photo-note"
                )
            }
        }
    )
}

@Composable
fun Tag(
    modifier: Modifier = Modifier,
    name: String
) {
    Text(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = Color(0xFF40464A),
                shape = RoundedCornerShape(12.dp)
            )
            .background(Color(0xFF3D4548))
            .padding(vertical = 4.dp, horizontal = 8.dp),
        text = name,
        color = Color(0xFFBAC0C7)
    )
}

@Composable
fun FAB() {
    FloatingActionButton(onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "create note")
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchBarPreview() {
    PetProjectTheme {
        SearchBar()
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BottomBarPreview() {
    PetProjectTheme {
        BottomBar()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FABPreview() {
    PetProjectTheme {
        FAB()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NotePreview() {
    PetProjectTheme {
        Note(
            noteUi = NoteUi(
                0,
                "title",
                "content",
                listOf(
                    TagUi(0, "taddkdg"),
                    TagUi(1, "taxxxg"),
                    TagUi(2, "tagammmaappaffffffffffffff"),
                    TagUi(3, "jkkkk")
                ),
                false
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TagPreview() {
    PetProjectTheme {
        Tag(name = "hah")
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainScreenPreview() {
    PetProjectTheme {
        MainScreen(
            notes = listOf(
                NoteUi(0, "a", "bc",
                    listOf(
                        TagUi(0, "taddkdg"),
                        TagUi(1, "taxxxg")
                    ), true),
                NoteUi(1, "b", "bddc", listOf(
                    TagUi(0, "taddkdg"),
                    TagUi(1, "taxxxg"),
                    TagUi(2, "ss"),
                    TagUi(3, "a")
                ), true),
                NoteUi(2, "c", "bsxadcc", listOf(
                    TagUi(0, "taddkdg"),
                    TagUi(1, "taxxxg"),
                    TagUi(2, "ssssss"),
                    TagUi(3, "avdvdvdvqqqv")
                ), false),
                NoteUi(3, "d", "bsxac", listOf(), true),
                NoteUi(4, "e", "bdxcxdc", listOf(), false),
                NoteUi(5, "f", "bsxadcc", listOf(), false),
                NoteUi(6, "f", "bsxadcc", listOf(), false)
            )
        )
    }
}