/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.rgm32715340.ui.item

// Importações necessárias para layouts, UI e view models
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rgm32715340.InventoryTopAppBar
import com.example.rgm32715340.R
import com.example.rgm32715340.ui.AppViewModelProvider
import com.example.rgm32715340.ui.navigation.NavigationDestination
import com.example.rgm32715340.ui.theme.InventoryTheme
import kotlinx.coroutines.launch

// Definição de um objeto para gerenciar a navegação no modo de edição do item
object ItemEditDestination : NavigationDestination {
    override val route = "item_edit" // Rota para edição
    override val titleRes = R.string.edit_item_title // Título da tela
    const val itemIdArg = "itemId" // Argumento necessário na rota
    val routeWithArgs = "$route/{$itemIdArg}" // Formato da rota com argumentos
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEditScreen(
    navigateBack: () -> Unit, // Função chamada ao voltar
    onNavigateUp: () -> Unit, // Função para navegação para cima
    modifier: Modifier = Modifier, // Modificadores de layout
    viewModel: ItemEditViewModel = viewModel(factory = AppViewModelProvider.Factory) // ViewModel injetado
) {
    val coroutineScope = rememberCoroutineScope() // Gerenciamento de corrotinas

    // Scaffold cria a estrutura básica da tela, incluindo app bar e corpo
    Scaffold(
        topBar = {
            // Barra superior com título e botão de navegação
            InventoryTopAppBar(
                title = stringResource(ItemEditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        modifier = modifier // Aplicação de modificadores externos
    ) { innerPadding ->
        // Corpo principal da tela
        ItemEntryBody(
            itemUiState = viewModel.itemUiState, // Estado do UI gerenciado pelo ViewModel
            onItemValueChange = viewModel::updateUiState, // Callback para atualizar o estado do item
            onSaveClick = {
                // Salva as alterações no banco de dados dentro de uma corrotina
                coroutineScope.launch {
                    viewModel.updateItem() // Chama a função para atualizar o item
                    navigateBack() // Retorna para a tela anterior
                }
            },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                )
                .verticalScroll(rememberScrollState()) // Permite rolagem vertical
        )
    }
}

// Função para pré-visualizar a tela no Android Studio
@Preview(showBackground = true)
@Composable
fun ItemEditScreenPreview() {
    InventoryTheme {
        // Chamada da tela de edição com ações simuladas
        ItemEditScreen(navigateBack = { /*Do nothing*/ }, onNavigateUp = { /*Do nothing*/ })
    }
}
