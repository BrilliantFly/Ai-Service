package com.know.knowboot.service.plan;

import com.know.knowboot.entity.plan.PlanFocusSession;

import java.util.List;
import java.util.Map;

/**
 * 番茄专注记录服务接口
 */
public interface IPlanFocusSessionService {

    boolean addSession(PlanFocusSession session, Long userId);

    List<PlanFocusSession> listToday(Long userId);

    Map<String, Object> getTodayStats(Long userId);
}
