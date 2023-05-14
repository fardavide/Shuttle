package shuttle.permissions.presentation.viewmodel

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import kotlinx.coroutines.flow.Flow
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam
import shuttle.permissions.presentation.action.PermissionsAction
import shuttle.permissions.presentation.presenter.PermissionsPresenter
import shuttle.permissions.presentation.state.PermissionsState
import shuttle.utils.compose.MoleculeViewModel

@KoinViewModel
@SuppressLint("StaticFieldLeak")
internal class PermissionsViewModel(
    @InjectedParam private val providedValues: Array<ProvidedValue<out Any>>,
    private val presenter: PermissionsPresenter
) : MoleculeViewModel<PermissionsAction, PermissionsState>() {

    @Composable
    override fun models(actions: Flow<PermissionsAction>): PermissionsState =
        presenter.models(actions, providedValues)
}
