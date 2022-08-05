package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateServiceImpl implements UpdateService {
    @Autowired
    private BotMapper botMapper;
    @Override
    public Map<String, String> update(Map<String, String> data) {
        Map<String, String> map = new HashMap<>();
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        int botId = Integer.parseInt(data.get("bot_id"));
        Bot bot = botMapper.selectById(botId);
        if(bot == null){
            map.put("error_message","Bot不存在或已被删除");
            return map;
        }
        if(!user.getId().equals(bot.getUserId())){
            map.put("error_message","没有权限修改该Bot");
            return map;
        }
        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");
        if (title == null) {
            map.put("error_message", "标题不能为空！");
            return map;
        }
        title = title.trim();
        if (title.length() == 0) {
            map.put("error_message", "标题不能为空！");
            return map;
        }
        if (title.length() > 100) {
            map.put("error_message", "标题长度过长！");
            return map;
        }
        if (description == null || description.length() == 0) {
            description = "这个用户很懒，什么也没留下";
        }
        if (description.length() > 300) {
            map.put("error_message", "描述长度过长！");
            return map;
        }
        if (content == null) {
            map.put("error_message", "代码不能为空！");
            return map;
        }
        if (content.length() > 10000) {
            map.put("error_message", "代码长度过长！");
            return map;
        }
        bot.setTitle(title);
        bot.setContent(content);
        bot.setDescription(description);
        Date date = new Date();
        bot.setModifyTime(date);
        botMapper.updateById(bot);
        map.put("error_message", "success");
        return map;
    }
}
