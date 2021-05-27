package com.hundun.mirai.plugin.cp.penguin.db;

import com.hundun.mirai.plugin.cp.penguin.domain.report.MatrixReport;
import com.hundun.mirai.plugin.db.BaseRepositoryImplement;
import com.hundun.mirai.plugin.db.CollectionSettings;

/**
 * @author hundun
 * Created on 2021/05/31
 */
public class MatrixReportRepositoryImplement extends BaseRepositoryImplement<MatrixReport> implements MatrixReportRepository {

    public MatrixReportRepositoryImplement(CollectionSettings<MatrixReport> mongoSettings) {
        super(mongoSettings);
    }

}
