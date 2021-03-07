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

package com.ccit19.merdog_client.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * Data Access Object for the users table.
 */
@Dao
public interface ChatListDao {

    /**
     * Get the user from the table. Since for simplicity we only have one user in the database,
     * this query gets all users from the table, but limits the result to just the 1st user.
     *
     * @return the user from the table
     */
    @Query("SELECT * FROM chatlist")
    Flowable<ChatList> getChatList();

    /**
     * Insert a chatList in the database. If the chatList already exists, replace it.
     *
     * @param chatList the chatList to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertUser(ChatList chatList);

    /**
     * Delete all users.
     */
    @Query("DELETE FROM chatlist")
    void deleteAllUsers();
}
