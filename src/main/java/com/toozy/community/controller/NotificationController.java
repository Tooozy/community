package com.toozy.community.controller;

import com.toozy.community.dto.NotificationDTO;
import com.toozy.community.entity.User;
import com.toozy.community.enums.NotificationTypeEnum;
import com.toozy.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2022-03-26
 */
@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "id") Long id) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        NotificationDTO notificationDTO = notificationService.read(id, user);


        if (notificationDTO.getType() == 1) {
            return "redirect:/question/" + notificationDTO.getOuterid();
        } else {
            Integer i = notificationService.getQuestionId(id);
            return "redirect:/question/" +i +"#" + notificationDTO.getOuterid();
        }

    }


}
