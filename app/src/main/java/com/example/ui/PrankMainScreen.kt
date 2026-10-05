package com.example.ui

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.PrankRecord
import com.example.ui.components.CyberGridBackground
import com.example.ui.components.FunnyEmojiRainOverlay
import com.example.ui.components.GlassmorphismCard
import com.example.ui.components.GoldenParticleBurstOverlay
import com.example.ui.components.TacticalUcLoader
import com.example.ui.components.UcRewardGraphicDisplay
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonYellow
import com.example.ui.theme.NeonYellowBright
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.PrankMagenta
import com.example.ui.theme.TacticalAmber
import com.example.ui.theme.TextMutedCyan
import com.example.ui.theme.TextWhite
import com.example.viewmodel.PrankStage
import com.example.viewmodel.PrankUiState
import com.example.viewmodel.PrankViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrankMainScreen(
    viewModel: PrankViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val prankHistory by viewModel.prankHistory.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Handle system back press on non-home stages
    if (uiState.stage != PrankStage.HOME) {
        BackHandler {
            viewModel.onRetryClicked()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ObsidianBlack,
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TacticalTopHeader(
                isStealthMode = uiState.isStealthMode,
                isSoundEnabled = uiState.isSoundEnabled,
                prankCount = prankHistory.size,
                onToggleStealth = viewModel::toggleStealthMode,
                onToggleSound = viewModel::toggleSound,
                onOpenHistory = { viewModel.setShowHistorySheet(true) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Animated Cyberpunk / PUBG Grid Background
            CyberGridBackground(
                accentColor = when (uiState.stage) {
                    PrankStage.HOME -> NeonYellow
                    PrankStage.LOADING -> ElectricCyan
                    PrankStage.UC_DROPPED -> CyberGreen
                    PrankStage.PRANK_REVEAL -> PrankMagenta
                },
                secondaryColor = if (uiState.stage == PrankStage.PRANK_REVEAL) NeonYellow else ElectricCyan
            )

            // Celebratory golden particles on UC_DROPPED stage
            if (uiState.stage == PrankStage.UC_DROPPED) {
                GoldenParticleBurstOverlay()
            }

            // Funny emoji rain animation on PRANK_REVEAL stage
            if (uiState.stage == PrankStage.PRANK_REVEAL) {
                FunnyEmojiRainOverlay()
            }

            // Responsive container supporting both Mobile and Tablet/Desktop widths
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                val isWideScreen = maxWidth >= 640.dp
                val contentMaxWidth = if (isWideScreen) 600.dp else maxWidth

                AnimatedContent(
                    targetState = uiState.stage,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(260)) +
                            scaleIn(
                                initialScale = 0.92f,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMediumLow
                                )
                            )).togetherWith(fadeOut(animationSpec = tween(180)))
                    },
                    label = "stage_transition",
                    modifier = Modifier
                        .widthIn(max = contentMaxWidth)
                        .fillMaxSize()
                ) { stage ->
                    when (stage) {
                        PrankStage.HOME -> {
                            HomeInputSection(
                                uiState = uiState,
                                recentHistory = prankHistory.take(3),
                                onPlayerIdChange = viewModel::onPlayerIdChanged,
                                onRandomIdClick = viewModel::fillRandomPlayerId,
                                onSelectPack = viewModel::selectUcPackLabel,
                                onNextClick = viewModel::onNextClicked,
                                onOpenFullHistory = { viewModel.setShowHistorySheet(true) }
                            )
                        }

                        PrankStage.LOADING -> {
                            LoadingCalculationSection(
                                uiState = uiState
                            )
                        }

                        PrankStage.UC_DROPPED -> {
                            UcDroppedCelebrationSection(
                                uiState = uiState
                            )
                        }

                        PrankStage.PRANK_REVEAL -> {
                            PrankRevealSection(
                                uiState = uiState,
                                onRetryClick = viewModel::onRetryClicked,
                                onFunnyHornClick = viewModel::triggerFunnyHornManually,
                                onShareClick = {
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            "😂 ALDANDIMMM! Player ID (${uiState.activePlayerId}) uchun 600 UC tushdi deb o'yladim! " +
                                                "Senga tekin UC beradigan jinni yo'q edi 😂😂 Bu shunchaki FREE UC PRANK DEMO edi 😎"
                                        )
                                    }
                                    context.startActivity(
                                        Intent.createChooser(shareIntent, "Do'stlarga ulashish")
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (uiState.showHistorySheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.setShowHistorySheet(false) },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = CyberSurface,
            contentColor = TextWhite
        ) {
            PrankHistoryBottomSheet(
                records = prankHistory,
                onDeleteRecord = viewModel::deleteHistoryRecord,
                onClearAll = viewModel::clearHistory,
                onDismiss = { viewModel.setShowHistorySheet(false) }
            )
        }
    }
}

@Composable
private fun TacticalTopHeader(
    isStealthMode: Boolean,
    isSoundEnabled: Boolean,
    prankCount: Int,
    onToggleStealth: () -> Unit,
    onToggleSound: () -> Unit,
    onOpenHistory: () -> Unit
) {
    Surface(
        color = ObsidianBlack.copy(alpha = 0.88f),
        border = BorderStroke(1.dp, ElectricCyan.copy(alpha = 0.25f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Neon Gaming Emblem
                Surface(
                    shape = CutCornerShape(8.dp),
                    color = NeonYellow.copy(alpha = 0.16f),
                    border = BorderStroke(1.5.dp, NeonYellow),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.SportsEsports,
                            contentDescription = null,
                            tint = NeonYellowBright,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Column {
                    Text(
                        text = if (isStealthMode) {
                            stringResource(R.string.stealth_site_title)
                        } else {
                            stringResource(R.string.site_title)
                        },
                        style = MaterialTheme.typography.titleMedium,
                        color = NeonYellowBright,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.testTag("site_header_title")
                    )
                    Text(
                        text = if (isStealthMode) {
                            "OFFICIAL EVENT SERVER • ONLINE"
                        } else {
                            "PUBG MOBILE STYLE • PRANK DEMO"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = ElectricCyan
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Stealth mode toggle (Hides "PRANK" label when tricking a friend)
                IconButton(
                    onClick = onToggleStealth,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("stealth_mode_button")
                ) {
                    Icon(
                        imageVector = if (isStealthMode) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = stringResource(R.string.stealth_mode_hint),
                        tint = if (isStealthMode) NeonYellowBright else TextMutedCyan
                    )
                }

                // Sound toggle
                IconButton(
                    onClick = onToggleSound,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("sound_toggle_button")
                ) {
                    Icon(
                        imageVector = if (isSoundEnabled) {
                            Icons.AutoMirrored.Filled.VolumeUp
                        } else {
                            Icons.AutoMirrored.Filled.VolumeOff
                        },
                        contentDescription = "Ovoz va vibratsiya",
                        tint = if (isSoundEnabled) ElectricCyan else TextMutedCyan
                    )
                }

                // Prank History button with counter badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CyberSurfaceVariant,
                    border = BorderStroke(1.dp, NeonYellow.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = onOpenHistory)
                        .testTag("history_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = stringResource(R.string.prank_history_title),
                            tint = NeonYellowBright,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = prankCount.toString(),
                            style = MaterialTheme.typography.labelLarge,
                            color = TextWhite
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeInputSection(
    uiState: PrankUiState,
    recentHistory: List<PrankRecord>,
    onPlayerIdChange: (String) -> Unit,
    onRandomIdClick: () -> Unit,
    onSelectPack: (String) -> Unit,
    onNextClick: () -> Unit,
    onOpenFullHistory: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val infiniteTransition = rememberInfiniteTransition(label = "home_btn_pulse")
    val buttonScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "btn_scale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Banner Card with generated PUBG supply drop artwork
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(156.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(
                    width = 1.5.dp,
                    brush = Brush.horizontalGradient(listOf(NeonYellow, ElectricCyan)),
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_hero_banner),
                contentDescription = "PUBG Mobile Supply Drop Banner",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                ObsidianBlack.copy(alpha = 0.25f),
                                ObsidianBlack.copy(alpha = 0.88f)
                            )
                        )
                    )
            )

            // Top badges inside Hero Banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = NeonYellow,
                    shape = CutCornerShape(topStart = 8.dp, bottomEnd = 8.dp)
                ) {
                    Text(
                        text = "ROYAL AIRDROP • 600 UC",
                        style = MaterialTheme.typography.labelSmall,
                        color = ObsidianBlack,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    color = ObsidianBlack.copy(alpha = 0.75f),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, ElectricCyan)
                ) {
                    Text(
                        text = "PAROLSIZ • XAVFSIZ",
                        style = MaterialTheme.typography.labelSmall,
                        color = ElectricCyan,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            // Bottom title overlay inside Hero Banner
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(14.dp)
            ) {
                Text(
                    text = stringResource(R.string.home_headline),
                    style = MaterialTheme.typography.headlineLarge,
                    color = NeonYellowBright,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.testTag("home_headline")
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(R.string.home_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextWhite.copy(alpha = 0.92f),
                    modifier = Modifier.testTag("home_subtitle")
                )
            }
        }

        // Main Glassmorphism Card for Player ID Input & NEXT Button
        GlassmorphismCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("home_glass_card"),
            primaryBorderColor = NeonYellow,
            secondaryBorderColor = ElectricCyan
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Tactical UC Package Selector Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val packs = listOf(
                        "600 UC • STANDART",
                        "600 UC • BONUS ⚡",
                        "600 UC • VIP CRATE"
                    )
                    packs.forEach { pack ->
                        val isSelected = uiState.selectedUcBonusLabel == pack
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .clip(CutCornerShape(8.dp))
                                .clickable { onSelectPack(pack) },
                            shape = CutCornerShape(8.dp),
                            color = if (isSelected) {
                                NeonYellow.copy(alpha = 0.22f)
                            } else {
                                CyberSurfaceVariant.copy(alpha = 0.55f)
                            },
                            border = BorderStroke(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) NeonYellowBright else ElectricCyan.copy(alpha = 0.3f)
                            )
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(horizontal = 6.dp)
                            ) {
                                Text(
                                    text = pack,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) NeonYellowBright else TextWhite,
                                    textAlign = TextAlign.Center,
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }

                // Player ID Header + Random ID Generator Chip
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.player_id_label),
                        style = MaterialTheme.typography.titleMedium,
                        color = ElectricCyan,
                        fontWeight = FontWeight.Bold
                    )

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = ElectricCyan.copy(alpha = 0.14f),
                        border = BorderStroke(1.dp, ElectricCyan.copy(alpha = 0.6f)),
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .clip(RoundedCornerShape(50))
                            .clickable(onClick = onRandomIdClick)
                            .testTag("random_id_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Casino,
                                contentDescription = null,
                                tint = ElectricCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = stringResource(R.string.random_id_chip),
                                style = MaterialTheme.typography.labelSmall,
                                color = ElectricCyan
                            )
                        }
                    }
                }

                // PUBG Player ID Input Field
                OutlinedTextField(
                    value = uiState.playerIdInput,
                    onValueChange = onPlayerIdChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("player_id_input"),
                    placeholder = {
                        Text(
                            text = stringResource(R.string.player_id_placeholder),
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMutedCyan.copy(alpha = 0.7f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.SportsEsports,
                            contentDescription = "PUBG ID Icon",
                            tint = NeonYellowBright
                        )
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        color = NeonYellowBright,
                        fontWeight = FontWeight.Bold
                    ),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonYellowBright,
                        unfocusedBorderColor = ElectricCyan.copy(alpha = 0.6f),
                        focusedContainerColor = ObsidianBlack.copy(alpha = 0.75f),
                        unfocusedContainerColor = ObsidianBlack.copy(alpha = 0.55f),
                        cursorColor = NeonYellowBright
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            onNextClick()
                        }
                    )
                )

                // Quick sample ID pills for fast 1-tap demo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tezkor ID:",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMutedCyan
                    )
                    listOf("5182940172", "5920481635", "5401928374").forEach { sampleId ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = ObsidianBlack.copy(alpha = 0.6f),
                            border = BorderStroke(1.dp, TextMutedCyan.copy(alpha = 0.35f)),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onPlayerIdChange(sampleId) }
                        ) {
                            Text(
                                text = sampleId,
                                style = MaterialTheme.typography.labelSmall,
                                color = TextWhite.copy(alpha = 0.85f),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Big Tactical "NEXT ➜" Button
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        onNextClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .scale(buttonScale)
                        .testTag("next_button"),
                    shape = CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp, topEnd = 6.dp, bottomStart = 6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonYellow,
                        contentColor = ObsidianBlack
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 2.dp
                    )
                ) {
                    Text(
                        text = stringResource(R.string.next_button),
                        style = MaterialTheme.typography.headlineMedium,
                        color = ObsidianBlack,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }

        // Safety Disclaimer Banner (Hidden in Stealth Mode so friends don't suspect the prank!)
        AnimatedVisibility(visible = !uiState.isStealthMode) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("safety_disclaimer_card"),
                shape = RoundedCornerShape(14.dp),
                color = CyberSurface.copy(alpha = 0.85f),
                border = BorderStroke(1.dp, ElectricCyan.copy(alpha = 0.35f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = ElectricCyan,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = stringResource(R.string.safety_disclaimer),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMutedCyan,
                        modifier = Modifier.testTag("safety_disclaimer_text")
                    )
                }
            }
        }

        // Recent Pranked Friends Log Preview
        if (recentHistory.isNotEmpty() && !uiState.isStealthMode) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = CyberSurface.copy(alpha = 0.75f),
                border = BorderStroke(1.dp, NeonYellow.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = NeonYellowBright,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = stringResource(R.string.prank_history_title),
                                style = MaterialTheme.typography.labelLarge,
                                color = NeonYellowBright
                            )
                        }
                        TextButton(onClick = onOpenFullHistory) {
                            Text(
                                text = "Barchasi",
                                style = MaterialTheme.typography.labelSmall,
                                color = ElectricCyan
                            )
                        }
                    }

                    recentHistory.forEach { record ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    ObsidianBlack.copy(alpha = 0.55f),
                                    RoundedCornerShape(10.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "ID: ${record.playerId}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextWhite,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = record.funnyBadge,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ElectricCyan
                                )
                            }
                            Text(
                                text = "ALDANDI 😂",
                                style = MaterialTheme.typography.labelSmall,
                                color = NeonYellowBright,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

/**
 * Step 2a: Loading screen ("UC hisoblanmoqda...")
 */
@Composable
private fun LoadingCalculationSection(
    uiState: PrankUiState
) {
    val animatedProgress by animateFloatAsState(
        targetValue = uiState.loadingProgress,
        animationSpec = tween(durationMillis = 120),
        label = "loading_bar_anim"
    )

    val stepMessages = listOf(
        stringResource(R.string.loading_step_1),
        stringResource(R.string.loading_step_2),
        stringResource(R.string.loading_step_3)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        GlassmorphismCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("loading_card"),
            primaryBorderColor = ElectricCyan,
            secondaryBorderColor = NeonYellow
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Target Player ID Pill
                Surface(
                    color = ElectricCyan.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, ElectricCyan)
                ) {
                    Text(
                        text = "PLAYER ID: ${uiState.activePlayerId}",
                        style = MaterialTheme.typography.labelLarge,
                        color = ElectricCyan,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }

                // Custom Tactical Radar & Hexagonal UC Spinner
                TacticalUcLoader(progress = animatedProgress)

                // Required text: "UC hisoblanmoqda..."
                Text(
                    text = stringResource(R.string.loading_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = NeonYellowBright,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag("loading_title")
                )

                // Animated neon progress bar
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(50)),
                    color = NeonYellowBright,
                    trackColor = ObsidianBlack,
                    strokeCap = StrokeCap.Round
                )

                // Live tactical console output
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = ObsidianBlack.copy(alpha = 0.78f),
                    border = BorderStroke(1.dp, ElectricCyan.copy(alpha = 0.35f))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        stepMessages.forEachIndexed { idx, msg ->
                            val isDone = idx < uiState.loadingStepIndex
                            val isCurrent = idx == uiState.loadingStepIndex
                            if (idx <= uiState.loadingStepIndex) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = if (isDone) "✔" else "▶",
                                        color = if (isDone) CyberGreen else NeonYellowBright,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = msg,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (isCurrent) TextWhite else TextMutedCyan
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Step 2b: 2-3 seconds later -> Big animation "🎉 600 UC TUSHDI!",
 * UC icon graphic, and "UC muvaffaqiyatli qo‘shildi!"
 */
@Composable
private fun UcDroppedCelebrationSection(
    uiState: PrankUiState
) {
    var triggerPop by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        triggerPop = true
    }

    val cardScale by animateFloatAsState(
        targetValue = if (triggerPop) 1f else 0.75f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "uc_drop_scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        GlassmorphismCard(
            modifier = Modifier
                .fillMaxWidth()
                .scale(cardScale)
                .testTag("uc_dropped_card"),
            primaryBorderColor = NeonYellowBright,
            secondaryBorderColor = CyberGreen
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Big headline: “🎉 600 UC TUSHDI!”
                Text(
                    text = stringResource(R.string.uc_dropped_title),
                    style = MaterialTheme.typography.displayMedium,
                    color = NeonYellowBright,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag("uc_dropped_title")
                )

                // Beautiful UC Icon Graphic
                UcRewardGraphicDisplay(
                    modifier = Modifier.testTag("uc_reward_graphic")
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Green verified badge: “UC muvaffaqiyatli qo‘shildi!”
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = CyberGreen.copy(alpha = 0.16f),
                    border = BorderStroke(1.5.dp, CyberGreen)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = CyberGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = stringResource(R.string.uc_dropped_subtitle),
                            style = MaterialTheme.typography.titleLarge,
                            color = CyberGreen,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.testTag("uc_dropped_subtitle")
                        )
                    }
                }

                Text(
                    text = "ID: ${uiState.activePlayerId} • Akkaunt sinxronlanmoqda...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ElectricCyan,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Step 3: 2 seconds after UC drop -> Prank Reveal Screen
 * - “😂 ALDANDIMMM!”
 * - “Senga tekin UC beradigan jinni yo‘q edi 😂😂”
 * - “Bu shunchaki PRANK DEMO edi 😎”
 * - Funny emoji animations
 * - “QAYTADAN SINAB KO‘RISH 🔄” button
 */
@Composable
private fun PrankRevealSection(
    uiState: PrankUiState,
    onRetryClick: () -> Unit,
    onFunnyHornClick: () -> Unit,
    onShareClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    val infiniteTransition = rememberInfiniteTransition(label = "prank_bounce")
    val headlineScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 550, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "prank_title_scale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GlassmorphismCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("prank_reveal_card"),
            primaryBorderColor = PrankMagenta,
            secondaryBorderColor = NeonYellowBright
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Funny 3D Level-3 Helmet Laughing Character Illustration + Floating Emoji Row
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(150.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .size(138.dp)
                            .border(
                                width = 3.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(NeonYellowBright, PrankMagenta, ElectricCyan)
                                ),
                                shape = CircleShape
                            ),
                        shape = CircleShape,
                        color = ObsidianBlack
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_prank_jester),
                            contentDescription = "Laughing PUBG Helmet Mascot",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Corner laughing emoji badge
                    Surface(
                        modifier = Modifier.align(Alignment.BottomEnd),
                        shape = CircleShape,
                        color = NeonYellowBright,
                        border = BorderStroke(2.dp, ObsidianBlack)
                    ) {
                        Text(
                            text = "🤣",
                            fontSize = 24.sp,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                }

                // Big Headline: “😂 ALDANDIMMM!”
                Text(
                    text = stringResource(R.string.prank_title),
                    style = MaterialTheme.typography.displayMedium,
                    color = NeonYellowBright,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .scale(headlineScale)
                        .testTag("prank_title")
                )

                // Subtitle 1: “Senga tekin UC beradigan jinni yo‘q edi 😂😂”
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = ObsidianBlack.copy(alpha = 0.72f),
                    border = BorderStroke(1.dp, NeonYellow.copy(alpha = 0.55f))
                ) {
                    Text(
                        text = stringResource(R.string.prank_subtitle_1),
                        style = MaterialTheme.typography.titleLarge,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(16.dp)
                            .testTag("prank_subtitle_1")
                    )
                }

                // Subtitle 2: “Bu shunchaki PRANK DEMO edi 😎”
                Surface(
                    shape = RoundedCornerShape(50),
                    color = ElectricCyan.copy(alpha = 0.16f),
                    border = BorderStroke(1.dp, ElectricCyan)
                ) {
                    Text(
                        text = stringResource(R.string.prank_subtitle_2),
                        style = MaterialTheme.typography.titleMedium,
                        color = ElectricCyan,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                            .testTag("prank_subtitle_2")
                    )
                }

                // Funny Rank Badge earned by the pranked Player ID
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            CyberSurfaceVariant.copy(alpha = 0.65f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ALDANGAN ID: ${uiState.activePlayerId}",
                            style = MaterialTheme.typography.labelLarge,
                            color = NeonYellowBright
                        )
                        Text(
                            text = "Unvon: ${uiState.funnyRankTitle}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMutedCyan
                        )
                    }
                    Text(
                        text = "0 UC 😜",
                        style = MaterialTheme.typography.titleMedium,
                        color = PrankMagenta,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                // Animated Funny Emoji Bar inside card
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("😂", "🤣", "😈", "🤡", "😎", "🎉").forEach { emoji ->
                        Text(
                            text = emoji,
                            fontSize = 26.sp,
                            modifier = Modifier.scale(headlineScale)
                        )
                    }
                }

                // Primary Retry Button: “QAYTADAN SINAB KO‘RISH 🔄”
                Button(
                    onClick = onRetryClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .testTag("retry_button"),
                    shape = CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp, topEnd = 6.dp, bottomStart = 6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonYellowBright,
                        contentColor = ObsidianBlack
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = ObsidianBlack,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.retry_button),
                        style = MaterialTheme.typography.titleMedium,
                        color = ObsidianBlack,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                // Secondary Actions: Laugh Horn Sound & Share Prank
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onFunnyHornClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("funny_sound_button"),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.5.dp, ElectricCyan),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ElectricCyan),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.funny_sound_button),
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricCyan,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }

                    OutlinedButton(
                        onClick = onShareClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("share_prank_button"),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.5.dp, NeonYellow),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonYellowBright),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = NeonYellowBright,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.share_prank_button),
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonYellowBright,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PrankHistoryBottomSheet(
    records: List<PrankRecord>,
    onDeleteRecord: (Int) -> Unit,
    onClearAll: () -> Unit,
    onDismiss: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("HH:mm • dd.MM", Locale.getDefault()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.prank_history_title),
                style = MaterialTheme.typography.headlineMedium,
                color = NeonYellowBright,
                fontWeight = FontWeight.ExtraBold
            )
            if (records.isNotEmpty()) {
                TextButton(
                    onClick = onClearAll,
                    modifier = Modifier.testTag("clear_history_button")
                ) {
                    Text(
                        text = stringResource(R.string.clear_history),
                        color = PrankMagenta,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        if (records.isEmpty()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                shape = RoundedCornerShape(16.dp),
                color = ObsidianBlack.copy(alpha = 0.6f),
                border = BorderStroke(1.dp, ElectricCyan.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "😈", fontSize = 36.sp)
                    Text(
                        text = stringResource(R.string.prank_history_empty),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextMutedCyan,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(records, key = { it.id }) { item ->
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = ObsidianBlack.copy(alpha = 0.8f),
                        border = BorderStroke(1.dp, ElectricCyan.copy(alpha = 0.35f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Player ID: ${item.playerId}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = NeonYellowBright,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${item.funnyBadge} • ${dateFormat.format(Date(item.timestamp))}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = ElectricCyan
                                )
                            }
                            IconButton(
                                onClick = { onDeleteRecord(item.id) },
                                modifier = Modifier.minimumInteractiveComponentSize()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "O'chirish",
                                    tint = TextMutedCyan
                                )
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ElectricCyan,
                contentColor = ObsidianBlack
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "YOPISH",
                style = MaterialTheme.typography.labelLarge,
                color = ObsidianBlack
            )
        }
    }
}
