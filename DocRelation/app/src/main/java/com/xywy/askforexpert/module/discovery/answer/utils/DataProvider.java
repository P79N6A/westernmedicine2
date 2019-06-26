/******************************************************************************
 * 2016 (C) Copyright Open-RnD Sp. z o.o.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.xywy.askforexpert.module.discovery.answer.utils;

import com.xywy.askforexpert.model.answer.show.BaseItem;
import com.xywy.askforexpert.model.answer.show.GroupItem;
import com.xywy.askforexpert.model.answer.show.PaperItem;

import java.util.List;


/**
 * 多级列表数据转换工具类
 */
public class DataProvider {

    /**
     * Do not confuse with MultiLevelListView levels.
     * Following variables refer only to data generation process.
     * For instance, if ITEMS_PER_LEVEL = 2 and MAX_LEVELS = 3,
     * list should look like this:
     * + 1
     * | + 1.1
     * | - - 1.1.1
     * | - - 1.1.2
     * | + 1.2
     * | - - 1.2.1
     * | - - 1.2.2
     * | - - 1.2.3
     * + 2
     * | + 2.1
     * | - - 2.1.1
     * | - - 2.1.2
     * | + 2.2
     * | - - 2.2.1
     * | - - 2.2.2
     */
    private static final int MAX_LEVELS = 2;


    /**
     * 获取子条目
     *
     * @param baseItem
     * @return
     */
    public static List<PaperItem> getSubItems(BaseItem baseItem) {
        if (!(baseItem instanceof GroupItem)) {
            throw new IllegalArgumentException("GroupItem required");
        }

        GroupItem groupItem = (GroupItem) baseItem;
        if (groupItem.getLevel() >= MAX_LEVELS) {
            return null;
        }

        return baseItem.getSubList();
    }

    /**
     * 判断条目是否为组条目 是否可展开
     *
     * @param baseItem
     * @return
     */
    public static boolean isExpandable(BaseItem baseItem) {
        return baseItem instanceof GroupItem;
    }


}
