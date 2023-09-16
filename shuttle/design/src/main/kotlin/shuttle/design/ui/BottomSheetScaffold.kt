package shuttle.design.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * A [Scaffold] that is placed inside a `BottomSheet
 */
@Composable
fun BottomSheetScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    elevation: CardElevation = BottomSheetScaffoldDefaults.bottomSheetElevation(),
    shape: Shape = BottomSheetScaffoldDefaults.bottomSheetShape(),
    content: @Composable (PaddingValues) -> Unit
) {
    Card(
        modifier = modifier,
        elevation = elevation,
        shape = shape
    ) {
        Scaffold(
            modifier = Modifier,
            topBar = topBar,
            bottomBar = bottomBar,
            snackbarHost = snackbarHost,
            floatingActionButton = floatingActionButton,
            floatingActionButtonPosition = floatingActionButtonPosition,
            containerColor = containerColor,
            contentColor = contentColor,
            content = content
        )
    }
}

object BottomSheetScaffoldDefaults {

    @Composable
    fun bottomSheetShape() = MaterialTheme.shapes.extraLarge.copy(
        bottomStart = CornerSize(0),
        bottomEnd = CornerSize(0)
    )

    @Composable
    fun bottomSheetElevation() = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
}
