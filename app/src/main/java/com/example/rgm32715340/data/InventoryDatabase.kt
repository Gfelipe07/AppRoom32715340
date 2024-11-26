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

package com.example.rgm32715340.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Classe do banco de dados com um objeto Singleton para garantir uma única instância do banco.
 */
@Database(entities = [Item::class], version = 1, exportSchema = false) // Define a entidade e a versão do banco
abstract class InventoryDatabase : RoomDatabase() {

    // Declaração da função abstrata que retorna o DAO
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var Instance: InventoryDatabase? = null

        /**
         * Função que retorna a instância única do banco de dados.
         * Se não existir, ela cria uma nova instância sincronizada.
         */
        fun getDatabase(context: Context): InventoryDatabase {
            return Instance ?: synchronized(this) { // Bloqueia outras threads para evitar disputas
                Room.databaseBuilder(
                    context.applicationContext,           // Contexto do aplicativo
                    InventoryDatabase::class.java,       // Classe do banco de dados
                    "item_database"                      // Nome do banco
                )
                    .fallbackToDestructiveMigration()   // Define estratégia para migração
                    .build()                            // Constrói a instância do banco
                    .also { Instance = it }             // Armazena a instância criada na variável Instance
            }
        }
    }
}
