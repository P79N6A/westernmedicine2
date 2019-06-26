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

package com.xywy.askforexpert.model.answer.show;

import java.util.List;

public class BaseItem {
    private String id;
    private String mName;
    private List<PaperItem> subList;

    public BaseItem(String name) {
        mName = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return mName;
    }

    public List<PaperItem> getSubList() {
        return subList;
    }

    public void setSubList(List<PaperItem> subList) {
        this.subList = subList;
    }


}
