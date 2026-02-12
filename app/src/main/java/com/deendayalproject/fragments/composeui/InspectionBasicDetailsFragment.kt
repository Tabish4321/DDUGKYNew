package com.deendayalproject.fragments.composeui

import android.os.Bundle
import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.core.view.WindowCompat
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.InspectionBasicFragmentBinding
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController


class InspectionBasicDetailsFragment : BaseFragment<InspectionBasicFragmentBinding>(
    bindingInflater = InspectionBasicFragmentBinding::inflate
) {






    override fun initializeViews() {
        binding.composeView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                InspectionModernScreen()
            }
        }
    }


    override fun setupObservers() {
    }

    override fun setupClickListeners() {
    }

    override fun loadInitialData() {
    }


}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionModernScreen() {

    var currentStep by remember { mutableStateOf(1) }
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current

    /*val systemUiController = rememberSystemUiController()
    val statusBarColor =  Color.Transparent

    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = false // false because background is dark gradient
        )
    }*/


    Scaffold(
        topBar = {
            PremiumTopBar(
                currentStep = currentStep,
                onBackClick = {
                    backDispatcher?.onBackPressedDispatcher?.onBackPressed()
                }
            )
        },

        bottomBar = {
            Surface(tonalElevation = 8.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    if (currentStep > 1) {
                        OutlinedButton(
                            onClick = { currentStep-- },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text("Previous")
                        }
                    }

                    Button(
                        onClick = {
                            if (currentStep < 3) currentStep++
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(if (currentStep < 3) "Continue" else "Submit")
                    }
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            // 🔹 FIXED HEADER (Not Scrollable)
            InspectionProgressHeader(currentStep)

            // 🔹 SCROLLABLE CONTENT
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                when (currentStep) {

                    1 -> {
                        item { TrainingCenterDetails() }
                        item { ExpandablePIASection() }
                        item { TrainingCenterDetails() }
                        item { ExpandablePIASection() }
                        item { TrainingCenterDetails() }
                        item { ExpandablePIASection() }

                    }

                    2 -> {
                        item { Text("TC Details Screen Coming Here") }

                    }

                    3 -> {
                        item { Text("Final Review Screen") }
                    }
                }
            }
        }
    }
}




@Composable
fun ExpandablePIASection() {

    var expanded by remember { mutableStateOf(true) }

    ElevatedCard(
        shape = RoundedCornerShape(24.dp)
    ) {

        Column(Modifier.padding(16.dp)) {

            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("PIA Information", style = MaterialTheme.typography.titleMedium)
                Icon(
                    if (expanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = "Auto populated",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("PRN Number") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    DropdownField(
                        label = "Sanction Letter No.",
                        options = listOf("SL-001", "SL-002")
                    )
                }
            }
        }
    }
}
