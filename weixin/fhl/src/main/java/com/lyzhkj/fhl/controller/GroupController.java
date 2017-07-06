/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.controller;

import com.lyzhkj.fhl.dto.GroupMember;
import com.lyzhkj.fhl.dto.GroupMemberOutput;
import com.lyzhkj.fhl.dto.GroupOutput;
import com.lyzhkj.fhl.dto.SearchMemberResult;
import com.lyzhkj.fhl.pojo.GarUser;
import com.lyzhkj.fhl.service.GroupService;
import com.lyzhkj.fhl.service.UserService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author breeze
 */
@Controller
public class GroupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    /**
     * 查询用户所属的群组列表信息
     *
     * @param openId
     * @return
     */
    @RequestMapping(value = "group/list", method = RequestMethod.GET)
    @ResponseBody
    public List<GroupOutput> listGroups(@RequestParam String openId) {

        LOGGER.info("-------------- group/list ---------------------openId:" + openId);

        GarUser user = userService.findUserByOpenId(openId);
        if (user == null) {
            return null;
        }

        return groupService.listIntro(user.getUserId());
    }

    /**
     * 查询指定群组的成员列表信息
     *
     * @param openId
     * @param groupId
     * @return
     */
    @RequestMapping(value = "group/listMember", method = RequestMethod.GET)
    @ResponseBody
    public GroupMemberOutput listMember(@RequestParam String openId, @RequestParam String groupId) {

        LOGGER.info("-------------- group/listMember ---------------------groupId:" + groupId);

        return groupService.listMemberOfGroup(groupId);
    }

    /**
     * 删除指定群组中的单个成员
     *
     * @param openId
     * @param groupId
     * @param id
     * @return
     */
    @RequestMapping(value = "group/deleteMember", method = RequestMethod.GET)
    @ResponseBody
    public int deleteMember(@RequestParam String openId, @RequestParam String groupId, @RequestParam String id) {

        LOGGER.info("-------------- group/deleteMember ---------------------id:" + id);

        return groupService.deleteMemberOfGroup(openId, groupId, id);
    }

    /**
     * 根据手机号码搜索指定群组中的成员
     *
     * @param openId
     * @param groupId
     * @param phoneno
     * @return
     */
    @RequestMapping(value = "group/searchMember", method = RequestMethod.GET)
    @ResponseBody
    public SearchMemberResult searchMember(@RequestParam String openId, @RequestParam String groupId, @RequestParam String phoneno) {

        LOGGER.info("-------------- group/searchMember ---------------------phoneno:" + phoneno);

        GroupMember data = groupService.searchMember(groupId, phoneno);

        SearchMemberResult result = new SearchMemberResult();
        result.setCode(data == null ? 0 : 1);
        result.setData(data);

        return result;
    }

    /**
     * 添加成员到指定群组
     *
     * @param openId
     * @param groupId
     * @param phoneno
     * @return
     */
    @RequestMapping(value = "group/addMember", method = RequestMethod.GET)
    @ResponseBody
    public int addMember(@RequestParam String openId, @RequestParam String groupId, @RequestParam String phoneno) {

        LOGGER.info("-------------- group/addMember ---------------------phoneno:" + phoneno);

        return groupService.addMemberOfGroup(openId, groupId, phoneno);
    }

}
