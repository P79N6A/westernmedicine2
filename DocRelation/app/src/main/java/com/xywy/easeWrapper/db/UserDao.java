/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xywy.easeWrapper.db;

import android.content.Context;

import com.xywy.easeWrapper.domain.EaseUser;
import com.xywy.easeWrapper.domain.RobotUser;

import java.util.List;
import java.util.Map;

public class UserDao {
	public static final String TABLE_NAME = "uers";
	public static final String COLUMN_NAME_ID = "username";
	public static final String COLUMN_NAME_NICK = "nick";
	public static final String COLUMN_NAME_AVATAR = "avatar";
	
	public static final String PREF_TABLE_NAME = "pref";
	public static final String COLUMN_NAME_DISABLED_GROUPS = "disabled_groups";
	public static final String COLUMN_NAME_DISABLED_IDS = "disabled_ids";

	public static final String ROBOT_TABLE_NAME = "robots";
	public static final String ROBOT_COLUMN_NAME_ID = "username";
	public static final String ROBOT_COLUMN_NAME_NICK = "nick";
	public static final String ROBOT_COLUMN_NAME_AVATAR = "avatar";
	
	
	public UserDao(Context context) {
	}

	/**
	 * save contact list
	 * 
	 * @param contactList
	 */
	public void saveContactList(List<EaseUser> contactList) {
	    HXDBManager.getInstance().saveContactList(contactList);
	}

	/**
	 * get contact list
	 * 
	 * @return
	 */
	public Map<String, EaseUser> getContactList() {
		
	    return HXDBManager.getInstance().getContactList();
	}
	
	/**
	 * delete a contact
	 * @param username
	 */
	public void deleteContact(String username){
	    HXDBManager.getInstance().deleteContact(username);
	}
	
	/**
	 * save a contact
	 * @param user
	 */
	public void saveContact(EaseUser user){
	    HXDBManager.getInstance().saveContact(user);
	}
	
	public void setDisabledGroups(List<String> groups){
	    HXDBManager.getInstance().setDisabledGroups(groups);
    }
    
    public List<String>  getDisabledGroups(){       
        return HXDBManager.getInstance().getDisabledGroups();
    }
    
    public void setDisabledIds(List<String> ids){
        HXDBManager.getInstance().setDisabledIds(ids);
    }
    
    public List<String> getDisabledIds(){
        return HXDBManager.getInstance().getDisabledIds();
    }
    
    public Map<String, RobotUser> getRobotUser(){
    	return HXDBManager.getInstance().getRobotList();
    }

    public void saveRobotUser(List<RobotUser> robotList){
    	HXDBManager.getInstance().saveRobotList(robotList);
    }
}
