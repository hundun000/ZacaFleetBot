package com.hundun.mirai.plugin.cp.penguin.db;

import com.hundun.mirai.plugin.cp.penguin.domain.report.StageInfoReport;
import com.hundun.mirai.plugin.db.BaseRepositoryImplement;
import com.hundun.mirai.plugin.db.CollectionSettings;

/**
 * @author hundun
 * Created on 2021/05/31
 */
public class StageInfoReportRepositoryImplement extends BaseRepositoryImplement<StageInfoReport>
        implements StageInfoReportRepository {

    public StageInfoReportRepositoryImplement(CollectionSettings<StageInfoReport> mongoSettings) {
        super(mongoSettings);
    }

}
