package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.PrankRecord
import com.example.data.PrankRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class PrankStage {
    HOME,
    LOADING,
    UC_DROPPED,
    PRANK_REVEAL
}

data class PrankUiState(
    val stage: PrankStage = PrankStage.HOME,
    val playerIdInput: String = "",
    val activePlayerId: String = "",
    val selectedUcBonusLabel: String = "600 UC • ROYAL PASS PACK",
    val loadingProgress: Float = 0f,
    val loadingStepIndex: Int = 0,
    val isStealthMode: Boolean = false,
    val isSoundEnabled: Boolean = true,
    val showHistorySheet: Boolean = false,
    val funnyRankTitle: String = "Tekinga O‘ch Sniper 🎯"
)

class PrankViewModel(
    private val repository: PrankRepository,
    private val onPlayClick: (Boolean) -> Unit = {},
    private val onPlayUcDrop: (Boolean) -> Unit = {},
    private val onPlayPrankHorn: (Boolean) -> Unit = {}
) : ViewModel() {

    private val _uiState = MutableStateFlow(PrankUiState())
    val uiState: StateFlow<PrankUiState> = _uiState.asStateFlow()

    val prankHistory: StateFlow<List<PrankRecord>> = repository.allRecords
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private var prankFlowJob: Job? = null

    private val funnyBadges = listOf(
        "Pochinki Tekinchisi 😂",
        "AirDrop Ovchisi 🪂",
        "M416 Muzlik Orzusi ❄️",
        "600 UC Ishonuvchisi 🤡",
        "Sosnovka Qiroli 😎",
        "Tekin Royal Pass Shaydosi 🏆"
    )

    private val samplePubgIds = listOf(
        "5182940172",
        "5920481635",
        "5401928374",
        "5738291046",
        "5294018563",
        "5849201739"
    )

    fun onPlayerIdChanged(newId: String) {
        // Allow digits and alphanumeric gamer tags up to 16 chars
        if (newId.length <= 16) {
            _uiState.update { it.copy(playerIdInput = newId) }
        }
    }

    fun fillRandomPlayerId() {
        onPlayClick(_uiState.value.isSoundEnabled)
        val randomId = samplePubgIds.random()
        _uiState.update { it.copy(playerIdInput = randomId) }
    }

    fun selectUcPackLabel(label: String) {
        onPlayClick(_uiState.value.isSoundEnabled)
        _uiState.update { it.copy(selectedUcBonusLabel = label) }
    }

    fun toggleStealthMode() {
        onPlayClick(_uiState.value.isSoundEnabled)
        _uiState.update { it.copy(isStealthMode = !it.isStealthMode) }
    }

    fun toggleSound() {
        val nextSound = !_uiState.value.isSoundEnabled
        _uiState.update { it.copy(isSoundEnabled = nextSound) }
        if (nextSound) {
            onPlayClick(true)
        }
    }

    fun setShowHistorySheet(show: Boolean) {
        onPlayClick(_uiState.value.isSoundEnabled)
        _uiState.update { it.copy(showHistorySheet = show) }
    }

    fun triggerFunnyHornManually() {
        onPlayPrankHorn(true)
    }

    fun onNextClicked() {
        val currentState = _uiState.value
        onPlayClick(currentState.isSoundEnabled)

        val effectiveId = currentState.playerIdInput.trim().ifEmpty {
            "5${Random.nextInt(100000000, 999999999)}"
        }
        val chosenBadge = funnyBadges.random()

        prankFlowJob?.cancel()
        prankFlowJob = viewModelScope.launch {
            // Step 2a: Loading animation ("UC hisoblanmoqda...") for ~2.3 seconds
            _uiState.update {
                it.copy(
                    stage = PrankStage.LOADING,
                    playerIdInput = effectiveId,
                    activePlayerId = effectiveId,
                    loadingProgress = 0.05f,
                    loadingStepIndex = 0,
                    funnyRankTitle = chosenBadge
                )
            }

            val totalLoadingSteps = 23
            for (step in 1..totalLoadingSteps) {
                delay(100L) // 23 * 100ms = 2300ms (2.3 seconds)
                val progress = (step.toFloat() / totalLoadingSteps).coerceIn(0f, 1f)
                val stepIdx = when {
                    progress < 0.38f -> 0
                    progress < 0.75f -> 1
                    else -> 2
                }
                _uiState.update {
                    it.copy(
                        loadingProgress = progress,
                        loadingStepIndex = stepIdx
                    )
                }
            }

            // Step 2b: "🎉 600 UC TUSHDI!" & "UC muvaffaqiyatli qo‘shildi!"
            onPlayUcDrop(_uiState.value.isSoundEnabled)
            _uiState.update {
                it.copy(
                    stage = PrankStage.UC_DROPPED,
                    loadingProgress = 1f
                )
            }

            // Wait 2 seconds ("Keyin 2 soniyadan so‘ng prank ekrani chiqsin")
            delay(2000L)

            // Step 3: Prank Reveal ("😂 ALDANDIMMM!")
            onPlayPrankHorn(_uiState.value.isSoundEnabled)
            _uiState.update {
                it.copy(
                    stage = PrankStage.PRANK_REVEAL,
                    isStealthMode = false // Reveal prank header too
                )
            }

            // Save to Room database history
            repository.insert(
                PrankRecord(
                    playerId = effectiveId,
                    ucAmount = 600,
                    funnyBadge = chosenBadge
                )
            )
        }
    }

    fun onRetryClicked() {
        prankFlowJob?.cancel()
        onPlayClick(_uiState.value.isSoundEnabled)
        _uiState.update {
            it.copy(
                stage = PrankStage.HOME,
                loadingProgress = 0f,
                loadingStepIndex = 0
            )
        }
    }

    fun deleteHistoryRecord(id: Int) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }
}

class PrankViewModelFactory(
    private val repository: PrankRepository,
    private val onPlayClick: (Boolean) -> Unit = {},
    private val onPlayUcDrop: (Boolean) -> Unit = {},
    private val onPlayPrankHorn: (Boolean) -> Unit = {}
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrankViewModel::class.java)) {
            return PrankViewModel(
                repository = repository,
                onPlayClick = onPlayClick,
                onPlayUcDrop = onPlayUcDrop,
                onPlayPrankHorn = onPlayPrankHorn
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
