/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.dao;

import com.lyzhkj.fhl.dto.GroupMember;
import com.lyzhkj.fhl.dto.GroupOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author breeze
 */
@Repository
public class GarUserCitizenDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(GarUserCitizenDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<GroupOutput> listIntro(String userId) {

        List<GroupOutput> result = new ArrayList<>();
        try {
            String sql = "SELECT uc.citizenId,uc.isadmin,c.hostNo,c.intCurrency FROM gar_user_citizen uc, gar_Citizen c WHERE uc.citizenId =c.citizenId AND uc.userId=?";
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, userId);

            for (Map<String, Object> row : rows) {
                GroupOutput item = new GroupOutput();
                item.setId((String) row.get("citizenId"));
                item.setName((String) row.get("hostNo"));
                item.setScore((int) row.get("intCurrency"));
                item.setAsAdmin((boolean) row.get("isadmin"));

                result.add(item);
            }
        } catch (Exception e) {
            LOGGER.error("", e);
        }

        return result;
    }

    public List<GroupMember> listMemberOfGroup(String groupId) {

        String sql = "SELECT uc.id,uc.isadmin,uc.score,u.mobilephone,u.openId FROM gar_user_citizen uc, gar_UserCloud u WHERE uc.userId=u.userId AND uc.citizenId=?";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, groupId);

        List<GroupMember> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            GroupMember item = new GroupMember();
            item.setId((String) row.get("id"));
            item.setPhoneno((String) row.get("mobilephone"));
            item.setScore(row.get("score") == null ? 0 : (int) row.get("score"));
            item.setAsAdmin((boolean) row.get("isadmin"));
            item.setOpenId((String) row.get("openId"));

            result.add(item);
        }

        return result;
    }

    public boolean checkUserIsAdmin(String groupId, String userId) {

        return jdbcTemplate.queryForRowSet("SELECT userId FROM gar_user_citizen WHERE isadmin=1 AND citizenId=? AND userId=?", groupId, userId).first();
    }

    @Transactional
    public boolean deleteMemberOfGroup(String id) {

        String sql = "DELETE FROM gar_user_citizen WHERE id=?";
        int n = jdbcTemplate.update(sql, id);

        return n > 0;
    }

    public GroupMember findMemberOfGroupByPhone(String groupId, String phoneno) {

        String sql = "SELECT uc.id,uc.isadmin,uc.score,u.mobilephone,u.openId FROM gar_user_citizen uc, gar_UserCloud u WHERE uc.userId=u.userId AND uc.citizenId=? AND u.mobilephone=?";

        try {
            Map<String, Object> row = jdbcTemplate.queryForMap(sql, groupId, phoneno);

            GroupMember result = new GroupMember();
            result.setId((String) row.get("id"));
            result.setPhoneno((String) row.get("mobilephone"));
            result.setScore(row.get("score") == null ? 0 : (int) row.get("score"));
            result.setAsAdmin((boolean) row.get("isadmin"));
            result.setOpenId((String) row.get("openId"));

            return result;
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }
    }

    public boolean checkUserIsAdded(String groupId, String userId) {

        return jdbcTemplate.queryForRowSet("SELECT userId FROM gar_user_citizen WHERE citizenId=? AND userId=?", groupId, userId).first();
    }

    public boolean addMemberOfGroup(String groupId, String userId) {

        return addMemberOfGroup(groupId, userId, 0);
    }

    public boolean addMemberOfGroup(String groupId, String userId, int isAdmin) {

        String sql = "insert into gar_user_citizen(id,userId,citizenId,isadmin,score,joinGroupTime) values (?,?,?,?,0,getdate())";

        int n = jdbcTemplate.update(sql, UUID.randomUUID().toString(), userId, groupId, isAdmin);

        return n > 0;
    }

    public int getTotalCurrencyScoreInGroupsOfUser(String userId) {

        int score = 0;

        String sql = "SELECT c.intCurrency FROM gar_user_citizen uc, gar_Citizen c WHERE uc.citizenId =c.citizenId AND uc.userId=?";
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, userId);

            for (Map<String, Object> row : rows) {
                score += row.get("intCurrency") == null ? 0 : (int) row.get("intCurrency");
            }

        } catch (Exception e) {
            LOGGER.error("", e);
            return 0;
        }

        return score;
    }

}
