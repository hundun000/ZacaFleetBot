package com.hundun.mirai.plugin.cp.penguin.db;

import com.hundun.mirai.plugin.cp.penguin.domain.report.MatrixReport;

/**
 * @author hundun
 * Created on 2021/04/26
 */
public interface MatrixReportRepository {

    void deleteAll();

    MatrixReport findById(String reportId);

    void save(MatrixReport report);
    
}