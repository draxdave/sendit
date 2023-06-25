package com.drax.sendit.view.connections.unpair

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import app.siamak.sendit.R
import com.drax.sendit.data.model.ModalMessage
import com.drax.sendit.data.model.toStringId
import com.drax.sendit.domain.network.model.UnpairRequest
import com.drax.sendit.view.base.BaseComposeBottomSheet
import com.drax.sendit.view.login.center
import com.drax.sendit.view.util.modal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UnpairFragment : BaseComposeBottomSheet() {
    val viewModel: UnpairVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                UnpairScreen(viewModel)
            }
        }
    }

    @Composable
    fun UnpairScreen(
        viewModel: UnpairVM = viewModel(),
    ) {
        val request by remember { mutableStateOf(arguments?.getSerializable(FRAGMENT_KEY) as? UnpairRequest) }
        val uiState by viewModel.uiState
        if (uiState == UnpairUiState.Done) {
            parentFragment?.modal(ModalMessage.Success(R.string.unpair_done))
            setResultAndDismiss(TAG, bundleOf())
            return
        }

        UnpairContent(
            uiStateProvider = viewModel.uiState,
            cancelClicked = { dismissAllowingStateLoss() },
            unpairClicked = {
                viewModel.unpairDevice(request ?: return@UnpairContent)
            },
        )
    }

    @Composable
    fun UnpairContent(
        uiStateProvider: MutableState<UnpairUiState> = mutableStateOf(UnpairUiState.Neutral),
        cancelClicked: () -> Unit = {},
        unpairClicked: () -> Unit = {},
    ) {
        val uiState by uiStateProvider
        val errorCode = (uiState as? UnpairUiState.Failed)?.reason?.errorCode
        val isLoading = uiState == UnpairUiState.Loading

        Card(
            elevation = 7.dp,
        ) {
            ConstraintLayout(
                modifier = Modifier
            ) {
                val (column, overlappingContent) = createRefs()

                Column(
                    modifier = Modifier
                        .constrainAs(column) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .background(Color.White)
                        .padding(vertical = 8.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = stringResource(id = R.string.unpair),
                            style = MaterialTheme.typography.h6,
                        )
                        Image(
                            modifier = Modifier.padding(8.dp),
                            painter = painterResource(id = R.drawable.ic_baseline_delete_outline_24),
                            contentDescription = null
                        )
                    }
                    Text(
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.caption,
                        text = stringResource(id = R.string.unpair_modal_description),
                    )

                    if (errorCode != null)
                        Text(
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.caption,
                            text = stringResource(id = errorCode.toStringId()),
                            color = MaterialTheme.colors.error,
                        )

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        onClick = unpairClicked,
                        enabled = uiState != UnpairUiState.Loading || uiState != UnpairUiState.Done,
                    ) {
                        Text(
                            style = MaterialTheme.typography.button,
                            text = stringResource(id = R.string.unpair)
                        )
                    }

                    Button(
                        modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White,
                        ),
                        onClick = cancelClicked,
                        enabled = uiState != UnpairUiState.Loading || uiState != UnpairUiState.Done,
                    ) {
                        Text(
                            style = MaterialTheme.typography.button,
                            text = stringResource(id = R.string.cancel)
                        )
                    }

                }

                if (isLoading)
                    Box(
                        modifier = Modifier
                            .background(Color.LightGray.copy(alpha = 0.5f))
                            .constrainAs(overlappingContent) {
                                top.linkTo(column.top)
                                bottom.linkTo(column.bottom)
                                start.linkTo(column.start)
                                end.linkTo(column.end)
                                width = Dimension.fillToConstraints
                                height = Dimension.fillToConstraints
                            }
                            .clickable {
                                // to prevent clicks on the content behind
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .center()
                                .size(48.dp),

                            )
                    }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewUnpairContent() {
        UnpairContent()
    }

    companion object {
        const val FRAGMENT_KEY = "UNPAIR_CONTENT_KEY"
        const val TAG = "UnpairContentFragment"
    }
}
