package com.hundun.mirai.bot.cp.penguin.db;

import com.hundun.mirai.bot.cp.penguin.domain.report.MatrixReport;
import com.hundun.mirai.bot.db.BaseRepositoryImplement;
import com.hundun.mirai.bot.db.CollectionSettings;

/**
 * @author hundun
 * Created on 2021/05/31
 */
public class MatrixReportRepositoryImplement extends BaseRepositoryImplement<MatrixReport> implements MatrixReportRepository {

    public MatrixReportRepositoryImplement(CollectionSettings<MatrixReport> mongoSettings) {
        super(mongoSettings);
    }

}
