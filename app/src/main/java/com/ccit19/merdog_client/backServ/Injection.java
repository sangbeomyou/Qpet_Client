/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ccit19.merdog_client.backServ;

import android.content.Context;

import com.ccit19.merdog_client.persistence.ChatListDatabase;
import com.ccit19.merdog_client.persistence.LocalChatListDataSource;
import com.ccit19.merdog_client.ui.dashboard.ViewModelFactory;

/**
 * Enables injection of data sources.
 */
public class Injection {

    public static ChatListDataSource provideUserDataSource(Context context) {
        ChatListDatabase database = ChatListDatabase.getInstance(context);
        return new LocalChatListDataSource(database.chatListDao());
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        ChatListDataSource dataSource = provideUserDataSource(context);
        return new ViewModelFactory(dataSource);
    }
}
