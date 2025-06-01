package project.fix.skripsi.presentation.ui.screen.home.components.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.BookmarkAdd
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.FolderOpen
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Scale
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import project.fix.skripsi.domain.model.AnswerKeyItem
import project.fix.skripsi.domain.model.SavedAnswerKey

@Composable
fun AnswerKeyDialog(
    answerKeyItems: List<AnswerKeyItem>,
    savedAnswerKeyTemplate: List<SavedAnswerKey>,
    onDismiss: () -> Unit,
    onAnswerKeySaved: (List<AnswerKeyItem>) -> Unit,
    onLoadTemplate: ((Int) -> Unit)? = null,
    onSaveAsTemplate: ((String, List<AnswerKeyItem>) -> Unit)? = null,
    onDeletedTemplate: (Int) -> Unit = {}
) {
    val answerKeyList = remember { mutableStateListOf<AnswerKeyItem>() }
    var showScoreWeightInfo by remember { mutableStateOf(false) }
    var showTemplateSelector by remember { mutableStateOf(false) }
    var templateName by remember { mutableStateOf("") }
    var saveAsTemplate by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf<Int?>(null) }

    val animatedProgress by animateFloatAsState(
        targetValue = if (answerKeyList.any { it.answer.isNotBlank() }) 1f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    // Initialize with existing items
    LaunchedEffect(answerKeyItems) {
        answerKeyList.clear()
        answerKeyList.addAll(answerKeyItems)
        if (answerKeyList.isEmpty()) {
            answerKeyList.add(AnswerKeyItem(1, "", 10))
        }
    }

    // Calculate total weight and progress
    val totalWeight = answerKeyList.sumOf { it.scoreWeight }
    val completedItems = answerKeyList.count { it.answer.isNotBlank() }
    val completionPercentage = if (answerKeyList.isNotEmpty()) {
        (completedItems.toFloat() / answerKeyList.size * 100).toInt()
    } else 0

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(0.95f),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 12.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Enhanced Header Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "🔑 Kunci Jawaban",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "📊 Total: $totalWeight poin",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "✅ $completedItems/${answerKeyList.size} soal",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Animated Info Button
                            IconButton(
                                onClick = { showScoreWeightInfo = !showScoreWeightInfo },
                                modifier = Modifier
                                    .background(
                                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f),
                                        CircleShape
                                    )
                                    .animateContentSize()
                            ) {
                                Icon(
                                    imageVector = if (showScoreWeightInfo) Icons.Outlined.Info else Icons.Rounded.Info,
                                    contentDescription = "Info Bobot",
                                    tint = MaterialTheme.colorScheme.secondary,
                                )
                            }

                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier
                                    .background(
                                        MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f),
                                        CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = "Tutup",
                                    tint = MaterialTheme.colorScheme.error,
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Enhanced Progress Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Progress Pengisian",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$completionPercentage%",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Template Actions with Enhanced Design
                if (savedAnswerKeyTemplate.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.2f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { showTemplateSelector = true },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(
                                        alpha = 0.4f
                                    )
                                ),
                                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.secondary)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.FolderOpen,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "📋 Gunakan Template",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Enhanced Info Section
                AnimatedVisibility(
                    visible = showScoreWeightInfo,
                    enter = expandVertically(spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeIn(),
                    exit = shrinkVertically(spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeOut()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "💡",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Tentang Bobot Nilai",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Bobot digunakan untuk menentukan kontribusi setiap soal terhadap nilai total. " +
                                        "Soal dengan bobot tinggi akan memiliki pengaruh lebih besar pada nilai akhir. " +
                                        "Pastikan total bobot sesuai dengan sistem penilaian yang diinginkan.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                // Enhanced Description
                Text(
                    text = "📝 Masukkan kunci jawaban dan atur bobot untuk setiap soal essay",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Enhanced Answer key items
                answerKeyList.forEachIndexed { index, item ->
                    val isCompleted = item.answer.isNotBlank()

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .animateContentSize(spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCompleted)
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                            else
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        border = if (isCompleted) BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                        ) else null
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            // Enhanced Header
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(
                                                if (isCompleted) MaterialTheme.colorScheme.primary
                                                else MaterialTheme.colorScheme.outline,
                                                CircleShape
                                            )
                                            .animateContentSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isCompleted) {
                                            Icon(
                                                imageVector = Icons.Rounded.Check,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onPrimary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        } else {
                                            Text(
                                                text = "${item.number}",
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column {
                                        Text(
                                            text = "📄 Soal ${item.number}",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        if (isCompleted) {
                                            Text(
                                                text = "✅ Sudah diisi",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.primary,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }

                                // Enhanced Delete Button
                                IconButton(
                                    onClick = {
                                        if (answerKeyList.size > 1) {
                                            answerKeyList.removeAt(index)
                                            // Renumber the remaining items
                                            for (i in index until answerKeyList.size) {
                                                answerKeyList[i] = answerKeyList[i].copy(number = i + 1)
                                            }
                                        }
                                    },
                                    enabled = answerKeyList.size > 1,
                                    modifier = Modifier
                                        .background(
                                            if (answerKeyList.size > 1)
                                                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f)
                                            else MaterialTheme.colorScheme.surface,
                                            CircleShape
                                        )
                                        .animateContentSize()
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Delete,
                                        contentDescription = "Hapus",
                                        modifier = Modifier.size(18.dp),
                                        tint = if (answerKeyList.size > 1)
                                            MaterialTheme.colorScheme.error
                                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Enhanced Answer input field
                            TextField(
                                value = item.answer,
                                onValueChange = { newAnswer ->
                                    answerKeyList[index] = item.copy(answer = newAnswer)
                                },
                                placeholder = {
                                    Text(
                                        "💭 Masukkan kunci jawaban untuk soal ${item.number}...",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                            alpha = 0.6f
                                        )
                                    )
                                },
                                singleLine = false,
                                maxLines = 4,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                                ),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Edit,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp),
                                        tint = if (isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                                    )
                                }
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Enhanced Weight section
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(
                                        alpha = 0.2f
                                    )
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Text(
                                            text = "⚖️",
                                            style = MaterialTheme.typography.titleMedium
                                        )

                                        Text(
                                            text = "Bobot Nilai :",
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        Spacer(modifier = Modifier.weight(1f))

                                        var scoreWeightText by remember { mutableStateOf(item.scoreWeight.toString()) }

                                        LaunchedEffect(item.scoreWeight) {
                                            scoreWeightText = item.scoreWeight.toString()
                                        }

                                        TextField(
                                            value = scoreWeightText,
                                            onValueChange = { newWeight ->
                                                if (newWeight.isEmpty()) {
                                                    scoreWeightText = ""
                                                    return@TextField
                                                }

                                                val filteredWeight =
                                                    newWeight.filter { it.isDigit() }.take(3)
                                                val weight = filteredWeight.toIntOrNull()

                                                if (weight != null && weight <= 100) {
                                                    scoreWeightText = filteredWeight
                                                    answerKeyList[index] =
                                                        item.copy(scoreWeight = maxOf(1, weight))
                                                }
                                            },
                                            modifier = Modifier
                                                .width(100.dp)
                                                .height(56.dp)
                                                .onFocusChanged { focusState ->
                                                    if (!focusState.isFocused && scoreWeightText.isEmpty()) {
                                                        scoreWeightText = "1"
                                                        answerKeyList[index] =
                                                            item.copy(scoreWeight = 1)
                                                    }
                                                },
                                            shape = RoundedCornerShape(12.dp),
                                            colors = TextFieldDefaults.colors(
                                                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                                                    alpha = 0.4f
                                                ),
                                                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                                                    alpha = 0.2f
                                                ),
                                                focusedIndicatorColor = Color.Transparent,
                                                unfocusedIndicatorColor = Color.Transparent,
                                                focusedTextColor = MaterialTheme.colorScheme.primary,
                                                unfocusedTextColor = MaterialTheme.colorScheme.primary
                                            ),
                                            textStyle = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center
                                            ),
                                            singleLine = true,
                                            keyboardOptions = KeyboardOptions(
                                                keyboardType = KeyboardType.Number,
                                                imeAction = ImeAction.Next
                                            ),
                                            placeholder = {
                                                Text(
                                                    text = "10",
                                                    color = MaterialTheme.colorScheme.primary.copy(
                                                        alpha = 0.5f
                                                    ),
                                                    style = MaterialTheme.typography.titleMedium.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        textAlign = TextAlign.Center
                                                    ),
                                                    modifier = Modifier.fillMaxWidth(),
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                        )
                                    }

                                    // Enhanced percentage indicator
                                    if (totalWeight > 0) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        val percentage =
                                            (item.scoreWeight.toFloat() / totalWeight * 100).toInt()
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            LinearProgressIndicator(
                                                progress = { item.scoreWeight.toFloat() / totalWeight },
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .height(6.dp)
                                                    .clip(RoundedCornerShape(3.dp)),
                                                color = MaterialTheme.colorScheme.secondary,
                                                trackColor = MaterialTheme.colorScheme.secondaryContainer.copy(
                                                    alpha = 0.3f
                                                )
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "$percentage%",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.secondary,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Enhanced Add button
                OutlinedButton(
                    onClick = {
                        val nextNumber = answerKeyList.size + 1
                        answerKeyList.add(AnswerKeyItem(nextNumber, "", 10))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "➕ Tambah Soal Baru",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Enhanced Save as Template Section
                if (onSaveAsTemplate != null && answerKeyList.any { it.answer.isNotBlank() }) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "💾",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Simpan sebagai Template",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Switch(
                                    checked = saveAsTemplate,
                                    onCheckedChange = { saveAsTemplate = it },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = MaterialTheme.colorScheme.tertiary,
                                        checkedTrackColor = MaterialTheme.colorScheme.tertiaryContainer
                                    )
                                )
                            }

                            AnimatedVisibility(
                                visible = saveAsTemplate,
                                enter = expandVertically(spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeIn(),
                                exit = shrinkVertically(spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeOut()
                            ) {
                                Column {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    TextField(
                                        value = templateName,
                                        onValueChange = { templateName = it },
                                        placeholder = {
                                            Text(
                                                "🏷️ Contoh: Ujian Matematika Kelas 10",
                                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                                    alpha = 0.6f
                                                )
                                            )
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        singleLine = true,
                                        shape = RoundedCornerShape(12.dp),
                                        colors = TextFieldDefaults.colors(
                                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent
                                        ),
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Rounded.BookmarkAdd,
                                                contentDescription = null,
                                                modifier = Modifier.size(20.dp),
                                                tint = MaterialTheme.colorScheme.tertiary
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Save button
                Button(
                    onClick = {
                        val validAnswers = answerKeyList
                            .filter { it.answer.isNotBlank() }
                            .mapIndexed { index, item -> item.copy(number = index + 1) }

                        // Save as template if enabled
                        if (saveAsTemplate && templateName.isNotBlank() && onSaveAsTemplate != null) {
                            onSaveAsTemplate.invoke(templateName.trim(), validAnswers)
                        }

                        onAnswerKeySaved(validAnswers)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "🚀 Terapkan Kunci Jawaban",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

        // Template Selector Dialog
    if (showTemplateSelector) {
        TemplateSelectorDialog(
            templates = savedAnswerKeyTemplate,
            onDismiss = { showTemplateSelector = false },
            onTemplateSelected = { template ->
                answerKeyList.clear()
                answerKeyList.addAll(template.answerKeys)
                showTemplateSelector = false
                onLoadTemplate?.invoke(template.id)
            },
            onTemplateDeleted = { template ->
                onDeletedTemplate(template.id)
            }
        )
    }
}

//@Composable
//fun AnswerKeyDialog(
//    answerKeyItems: List<AnswerKeyItem>,
//    savedAnswerKeyTemplate: List<SavedAnswerKey>,
//    onDismiss: () -> Unit,
//    onAnswerKeySaved: (List<AnswerKeyItem>) -> Unit,
//    onLoadTemplate: ((Int) -> Unit)? = null,
//    onSaveAsTemplate: ((String, List<AnswerKeyItem>) -> Unit)? = null,
//    onDeletedTemplate: (Int) -> Unit = {}
//) {
//    val answerKeyList = remember { mutableStateListOf<AnswerKeyItem>() }
//    var showScoreWeightInfo by remember { mutableStateOf(false) }
//    var showTemplateSelector by remember { mutableStateOf(false) }
//    var showSaveTemplateDialog by remember { mutableStateOf(false) }
//
//    val animatedProgress by animateFloatAsState(
//        targetValue = if (answerKeyList.any { it.answer.isNotBlank() }) 1f else 0f,
//        animationSpec = tween(300)
//    )
//
//    // Initialize with existing items
//    LaunchedEffect(answerKeyItems) {
//        answerKeyList.clear()
//        answerKeyList.addAll(answerKeyItems)
//        if (answerKeyList.isEmpty()) {
//            answerKeyList.add(AnswerKeyItem(1, "", 10))
//        }
//    }
//
//    // Calculate total weight
//    val totalWeight = answerKeyList.sumOf { it.scoreWeight }
//
//    Dialog(
//        onDismissRequest = onDismiss,
//        properties = DialogProperties(
//            dismissOnBackPress = true,
//            dismissOnClickOutside = false,
//            usePlatformDefaultWidth = false
//        )
//    ) {
//        Surface(
//            modifier = Modifier.fillMaxWidth(0.95f),
//            shape = RoundedCornerShape(20.dp),
//            color = MaterialTheme.colorScheme.surface,
//            shadowElevation = 8.dp
//        ) {
//            Column(
//                modifier = Modifier
//                    .padding(24.dp)
//                    .verticalScroll(rememberScrollState())
//            ) {
//                // Header Section
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Column(
//                        modifier = Modifier.weight(2f)
//                    ) {
//                        Text(
//                            text = "Kunci Jawaban",
//                            style = MaterialTheme.typography.headlineSmall,
//                            fontWeight = FontWeight.Bold,
//                            color = MaterialTheme.colorScheme.primary
//                        )
//                        Text(
//                            text = "Total Nilai : $totalWeight",
//                            style = MaterialTheme.typography.bodyMedium,
//                            color = MaterialTheme.colorScheme.onSurfaceVariant,
//                            modifier = Modifier.padding(top = 2.dp)
//                        )
//                    }
//
//                    Row(
//                        modifier = Modifier.weight(1f),
//                        horizontalArrangement = Arrangement.spacedBy(4.dp)
//                    ) {
//                        IconButton(
//                            onClick = { showScoreWeightInfo = !showScoreWeightInfo },
//                            modifier = Modifier
//                                .background(
//                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
//                                    CircleShape
//                                )
//                        ) {
//                            Icon(
//                                imageVector = Icons.Rounded.Info,
//                                contentDescription = "Info Bobot",
//                                tint = MaterialTheme.colorScheme.primary,
//                            )
//                        }
//
//                        IconButton(
//                            onClick = onDismiss,
//                            modifier = Modifier
//                                .background(
//                                    MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f),
//                                    CircleShape
//                                )
//                        ) {
//                            Icon(
//                                imageVector = Icons.Rounded.Close,
//                                contentDescription = "Tutup",
//                                tint = MaterialTheme.colorScheme.error,
//                            )
//                        }
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Template Action Buttons
//                if (savedAnswerKeyTemplate.isNotEmpty() || onSaveAsTemplate != null) {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        // Load Template Button
//                        if (savedAnswerKeyTemplate.isNotEmpty()) {
//                            OutlinedButton(
//                                onClick = { showTemplateSelector = true },
//                                modifier = Modifier.weight(1f),
//                                shape = RoundedCornerShape(12.dp),
//                                colors = ButtonDefaults.outlinedButtonColors(
//                                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
//                                ),
//                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Rounded.FolderOpen,
//                                    contentDescription = null,
//                                    modifier = Modifier.size(16.dp),
//                                    tint = MaterialTheme.colorScheme.secondary
//                                )
//                                Spacer(modifier = Modifier.width(4.dp))
//                                Text(
//                                    text = "Gunakan Skema Jawaban",
//                                    style = MaterialTheme.typography.labelSmall,
//                                    color = MaterialTheme.colorScheme.secondary
//                                )
//                            }
//                        }
//
//                        // Save as Template Button
//                        if (onSaveAsTemplate != null && answerKeyList.any { it.answer.isNotBlank() }) {
//                            OutlinedButton(
//                                onClick = { showSaveTemplateDialog = true },
//                                modifier = Modifier.weight(1f),
//                                shape = RoundedCornerShape(12.dp),
//                                colors = ButtonDefaults.outlinedButtonColors(
//                                    containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
//                                ),
//                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary)
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Rounded.BookmarkAdd,
//                                    contentDescription = null,
//                                    modifier = Modifier.size(16.dp),
//                                    tint = MaterialTheme.colorScheme.tertiary
//                                )
//                                Spacer(modifier = Modifier.width(4.dp))
//                                Text(
//                                    text = "Simpan Skema Jawaban",
//                                    style = MaterialTheme.typography.labelSmall,
//                                    color = MaterialTheme.colorScheme.tertiary
//                                )
//                            }
//                        }
//                    }
//
//                    Spacer(modifier = Modifier.height(16.dp))
//                }
//
//                // Info Section
//                AnimatedVisibility(
//                    visible = showScoreWeightInfo,
//                    enter = expandVertically() + fadeIn(),
//                    exit = shrinkVertically() + fadeOut()
//                ) {
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 16.dp),
//                        colors = CardDefaults.cardColors(
//                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
//                        ),
//                        shape = RoundedCornerShape(12.dp)
//                    ) {
//                        Column(
//                            modifier = Modifier.padding(16.dp)
//                        ) {
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Rounded.Lightbulb,
//                                    contentDescription = null,
//                                    tint = MaterialTheme.colorScheme.primary,
//                                    modifier = Modifier.size(20.dp)
//                                )
//                                Spacer(modifier = Modifier.width(8.dp))
//                                Text(
//                                    text = "Tentang Bobot Nilai",
//                                    style = MaterialTheme.typography.titleSmall,
//                                    fontWeight = FontWeight.Bold,
//                                    color = MaterialTheme.colorScheme.primary
//                                )
//                            }
//                            Spacer(modifier = Modifier.height(8.dp))
//                            Text(
//                                text = "Bobot digunakan untuk menentukan kontribusi setiap soal terhadap nilai total. " +
//                                  "Soal dengan bobot tinggi akan memiliki pengaruh lebih besar pada nilai akhir.",
//                                style = MaterialTheme.typography.bodySmall,
//                                color = MaterialTheme.colorScheme.onSurfaceVariant,
//                                lineHeight = 18.sp
//                            )
//                        }
//                    }
//                }
//
//                // Description
//                Text(
//                    text = "Masukkan kunci jawaban dan bobot untuk setiap soal essay",
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                // Progress Indicator
//                LinearProgressIndicator(
//                    progress = { animatedProgress },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(4.dp)
//                        .clip(RoundedCornerShape(2.dp)),
//                    color = MaterialTheme.colorScheme.primary,
//                    trackColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
//                )
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                // Answer key items
//                answerKeyList.forEachIndexed { index, item ->
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 6.dp)
//                            .animateContentSize(),
//                        colors = CardDefaults.cardColors(
//                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
//                        ),
//                        shape = RoundedCornerShape(16.dp)
//                    ) {
//                        Column(
//                            modifier = Modifier.padding(16.dp)
//                        ) {
//                            // Header with question number and delete button
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically,
//                                horizontalArrangement = Arrangement.SpaceBetween,
//                                modifier = Modifier.fillMaxWidth()
//                            ) {
//                                Row(
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//                                    Box(
//                                        modifier = Modifier
//                                            .size(32.dp)
//                                            .background(
//                                                MaterialTheme.colorScheme.primary,
//                                                CircleShape
//                                            ),
//                                        contentAlignment = Alignment.Center
//                                    ) {
//                                        Text(
//                                            text = "${item.number}",
//                                            style = MaterialTheme.typography.labelLarge,
//                                            fontWeight = FontWeight.Bold,
//                                            color = MaterialTheme.colorScheme.onPrimary
//                                        )
//                                    }
//
//                                    Spacer(modifier = Modifier.width(12.dp))
//
//                                    Text(
//                                        text = "Soal ${item.number}",
//                                        style = MaterialTheme.typography.titleMedium,
//                                        fontWeight = FontWeight.SemiBold,
//                                        color = MaterialTheme.colorScheme.onSurface
//                                    )
//                                }
//
//                                IconButton(
//                                    onClick = {
//                                        if (answerKeyList.size > 1) {
//                                            answerKeyList.removeAt(index)
//                                            // Renumber the remaining items
//                                            for (i in index until answerKeyList.size) {
//                                                answerKeyList[i] = answerKeyList[i].copy(number = i + 1)
//                                            }
//                                        }
//                                    },
//                                    enabled = answerKeyList.size > 1,
//                                    modifier = Modifier
//                                        .background(
//                                            if (answerKeyList.size > 1)
//                                                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f)
//                                            else MaterialTheme.colorScheme.surface,
//                                            CircleShape
//                                        )
//                                ) {
//                                    Icon(
//                                        imageVector = Icons.Rounded.Delete,
//                                        contentDescription = "Hapus",
//                                        modifier = Modifier.size(18.dp),
//                                        tint = if (answerKeyList.size > 1)
//                                            MaterialTheme.colorScheme.error
//                                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
//                                    )
//                                }
//                            }
//
//                            Spacer(modifier = Modifier.height(12.dp))
//
//                            // Answer input field
//                            TextField(
//                                value = item.answer,
//                                onValueChange = { newAnswer ->
//                                    answerKeyList[index] = item.copy(answer = newAnswer)
//                                },
//                                placeholder = {
//                                    Text(
//                                        "Masukkan kunci jawaban untuk soal ${item.number}...",
//                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
//                                    )
//                                },
//                                singleLine = false,
//                                maxLines = 4,
//                                modifier = Modifier.fillMaxWidth(),
//                                shape = RoundedCornerShape(12.dp),
//                                colors = TextFieldDefaults.colors(
//                                    focusedContainerColor = MaterialTheme.colorScheme.surface,
//                                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
//                                    focusedIndicatorColor = Color.Transparent,
//                                    unfocusedIndicatorColor = Color.Transparent,
//                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
//                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
//                                ),
//                                leadingIcon = {
//                                    Icon(
//                                        imageVector = Icons.Rounded.Edit,
//                                        contentDescription = null,
//                                        modifier = Modifier.size(20.dp),
//                                        tint = MaterialTheme.colorScheme.primary
//                                    )
//                                }
//                            )
//
//                            Spacer(modifier = Modifier.height(12.dp))
//
//                            // Weight input section
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically,
//                                horizontalArrangement = Arrangement.spacedBy(12.dp)
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Rounded.Scale,
//                                    contentDescription = null,
//                                    modifier = Modifier.size(20.dp),
//                                    tint = MaterialTheme.colorScheme.secondary
//                                )
//
//                                Text(
//                                    text = "Bobot Nilai :",
//                                    style = MaterialTheme.typography.labelLarge,
//                                    fontWeight = FontWeight.Medium,
//                                    color = MaterialTheme.colorScheme.onSurface
//                                )
//
//                                Spacer(modifier = Modifier.weight(1f))
//
//                                // Weight input field
//
//                                var scoreWeightText by remember { mutableStateOf(item.scoreWeight.toString()) }
//
//                                // Update text when item changes
//                                LaunchedEffect(item.scoreWeight) {
//                                    scoreWeightText = item.scoreWeight.toString()
//                                }
//
//                                TextField(
//                                    value = scoreWeightText,
//                                    onValueChange = { newWeight ->
//                                        // Allow empty string temporarily
//                                        if (newWeight.isEmpty()) {
//                                            scoreWeightText = ""
//                                            return@TextField
//                                        }
//
//                                        // Filter and validate input
//                                        val filteredWeight = newWeight.filter { it.isDigit() }.take(3)
//                                        val weight = filteredWeight.toIntOrNull()
//
//                                        if (weight != null && weight <= 100) {
//                                            scoreWeightText = filteredWeight
//                                            answerKeyList[index] = item.copy(scoreWeight = maxOf(1, weight))
//                                        }
//                                    },
//                                    modifier = Modifier
//                                        .width(100.dp)
//                                        .height(56.dp)
//                                        .onFocusChanged { focusState ->
//                                            // When focus is lost, ensure we have a valid value
//                                            if (!focusState.isFocused && scoreWeightText.isEmpty()) {
//                                                scoreWeightText = "1"
//                                                answerKeyList[index] = item.copy(scoreWeight = 1)
//                                            }
//
//                                        },
//                                    shape = RoundedCornerShape(12.dp),
//                                    colors = TextFieldDefaults.colors(
//                                        focusedContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
//                                        unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
//                                        focusedIndicatorColor = Color.Transparent,
//                                        unfocusedIndicatorColor = Color.Transparent,
//                                        focusedTextColor = MaterialTheme.colorScheme.primary,
//                                        unfocusedTextColor = MaterialTheme.colorScheme.primary
//                                    ),
//                                    textStyle = MaterialTheme.typography.titleMedium.copy(
//                                        fontWeight = FontWeight.Bold,
//                                        textAlign = TextAlign.Center
//                                    ),
//                                    singleLine = true,
//                                    keyboardOptions = KeyboardOptions(
//                                        keyboardType = KeyboardType.Number,
//                                        imeAction = ImeAction.Next
//                                    ),
//                                    placeholder = {
//                                        Text(
//                                            text = "10",
//                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
//                                            style = MaterialTheme.typography.titleMedium.copy(
//                                                fontWeight = FontWeight.Bold,
//                                                textAlign = TextAlign.Center
//                                            ),
//                                            modifier = Modifier.fillMaxWidth(),
//                                            textAlign = TextAlign.Center
//                                        )
//                                    }
//                                )
//                            }
//
//                            // Weight percentage indicator
//                            if (totalWeight > 0) {
//                                Spacer(modifier = Modifier.height(8.dp))
//                                val percentage = (item.scoreWeight.toFloat() / totalWeight * 100).toInt()
//                                Row(
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//                                    LinearProgressIndicator(
//                                        progress = { item.scoreWeight.toFloat() / totalWeight },
//                                        modifier = Modifier
//                                            .weight(1f)
//                                            .height(6.dp)
//                                            .clip(RoundedCornerShape(3.dp)),
//                                        color = MaterialTheme.colorScheme.secondary,
//                                        trackColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
//                                    )
//                                    Spacer(modifier = Modifier.width(8.dp))
//                                    Text(
//                                        text = "$percentage%",
//                                        style = MaterialTheme.typography.labelSmall,
//                                        color = MaterialTheme.colorScheme.secondary,
//                                        fontWeight = FontWeight.Medium
//                                    )
//                                }
//                            }
//                        }
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                // Add new item button
//                OutlinedButton(
//                    onClick = {
//                        val nextNumber = answerKeyList.size + 1
//                        answerKeyList.add(AnswerKeyItem(nextNumber, "", 10))
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(56.dp),
//                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
//                    shape = RoundedCornerShape(16.dp),
//                    colors = ButtonDefaults.outlinedButtonColors(
//                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
//                    )
//                ) {
//                    Icon(
//                        imageVector = Icons.Rounded.Add,
//                        contentDescription = null,
//                        modifier = Modifier.size(20.dp)
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text(
//                        text = "Tambah Soal Baru",
//                        style = MaterialTheme.typography.labelLarge,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//                // Save button
//                Button(
//                    onClick = {
//                        // Filter out empty answers before saving
//                        val validAnswers = answerKeyList
//                            .filter { it.answer.isNotBlank() }
//                            .mapIndexed { index, item -> item.copy(number = index + 1) }
//                        onAnswerKeySaved(validAnswers)
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(56.dp),
//                    shape = RoundedCornerShape(16.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = MaterialTheme.colorScheme.primary
//                    ),
//                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
//                ) {
//                    Icon(
//                        imageVector = Icons.Rounded.CheckCircle,
//                        contentDescription = null,
//                        modifier = Modifier.size(20.dp)
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text(
//                        text = "Terapkan Kunci Jawaban",
//                        style = MaterialTheme.typography.titleMedium,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//            }
//        }
//    }
//
//    // Template Selector Dialog
//    if (showTemplateSelector) {
//        TemplateSelectorDialog(
//            templates = savedAnswerKeyTemplate,
//            onDismiss = { showTemplateSelector = false },
//            onTemplateSelected = { template ->
//                answerKeyList.clear()
//                answerKeyList.addAll(template.answerKeys)
//                showTemplateSelector = false
//                onLoadTemplate?.invoke(template.id)
//            },
//            onTemplateDeleted = { template ->
//                onDeletedTemplate(template.id)
//            }
//        )
//    }
//
//    // Save Template Dialog
//    if (showSaveTemplateDialog) {
//        SaveTemplateDialog(
//            onDismiss = { showSaveTemplateDialog = false },
//            onSave = { templateName ->
//                val validAnswers = answerKeyList.filter { it.answer.isNotBlank() }
//                onSaveAsTemplate?.invoke(templateName, validAnswers)
//                showSaveTemplateDialog = false
//            },
//        )
//    }
//}

@Composable
fun TemplateSelectorDialog(
    templates: List<SavedAnswerKey>,
    onDismiss: () -> Unit,
    onTemplateSelected: (SavedAnswerKey) -> Unit,
    onTemplateDeleted: (SavedAnswerKey) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(0.9f),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Pilih Template",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Tutup"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.heightIn(max = 400.dp)
                ) {
                    items(templates) { template ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onTemplateSelected(template) },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = template.title,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.SemiBold
                                        )

                                        Text(
                                            text = "${template.answerKeys.size} soal",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "Dibuat: ${java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale("id", "ID")).format(java.util.Date(template.createdAt))}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "Total Bobot: ${template.answerKeys.sumOf { it.scoreWeight }}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.secondary,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                // Tombol Delete di kanan bawah
                                IconButton(
                                    onClick = { onTemplateDeleted(template) },
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Hapus Template",
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(20.dp)
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

@Composable
fun SaveTemplateDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var templateName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Simpan Template",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = "Berikan nama untuk template kunci jawaban ini:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(12.dp))
                TextField(
                    value = templateName,
                    onValueChange = { templateName = it },
                    placeholder = { Text("Contoh: Ujian Matematika Kelas 10") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (templateName.isNotBlank()) {
                        onSave(templateName.trim())
                    }
                },
                enabled = templateName.isNotBlank()
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}