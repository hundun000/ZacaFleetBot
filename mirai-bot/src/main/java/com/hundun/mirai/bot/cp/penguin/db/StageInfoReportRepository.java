package com.hundun.mirai.bot.cp.penguin.db;


import com.hundun.mirai.bot.cp.penguin.domain.report.StageInfoReport;

/**
 * @author hundun
 * Created on 2021/04/26
 */
public interface StageInfoReportRepository {

    void deleteAll();

    StageInfoReport findById(String reportId);

    void save(StageInfoReport report);
    
}
