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

import com.google.gson.annotations.SerializedName;

public class PaperItem extends BaseItem {

    private String paper_id;
    private String paper_name;
    private String class_id;
    private int pass_score;
    private int total_score;
    @SerializedName("update_time")
    private String version;
    private String paper_status;
    private String isdel;
    private String audit_man_id;

    public PaperItem(String name) {
        super(name);
    }


    public String getPaper_id() {
        return paper_id;
    }

    public void setPaper_id(String paper_id) {
        this.paper_id = paper_id;
    }

    public String getPaper_name() {
        return paper_name;
    }

    public void setPaper_name(String paper_name) {
        this.paper_name = paper_name;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public int getPass_score() {
        return pass_score;
    }

    public void setPass_score(int pass_score) {
        this.pass_score = pass_score;
    }

    public int getTotal_score() {
        return total_score;
    }

    public void setTotal_score(int total_score) {
        this.total_score = total_score;
    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPaper_status() {
        return paper_status;
    }

    public void setPaper_status(String paper_status) {
        this.paper_status = paper_status;
    }

    public String getIsdel() {
        return isdel;
    }

    public void setIsdel(String isdel) {
        this.isdel = isdel;
    }

    public String getAudit_man_id() {
        return audit_man_id;
    }

    public void setAudit_man_id(String audit_man_id) {
        this.audit_man_id = audit_man_id;
    }
}
