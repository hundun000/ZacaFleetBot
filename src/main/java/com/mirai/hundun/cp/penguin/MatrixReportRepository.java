package com.mirai.hundun.cp.penguin;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mirai.hundun.cp.penguin.domain.Item;
import com.mirai.hundun.cp.penguin.domain.MatrixReport;
import com.mirai.hundun.cp.penguin.domain.MatrixReportNode;
import com.mirai.hundun.cp.penguin.domain.ResultMatrixResponse;
import com.mirai.hundun.cp.weibo.domain.WeiboCardCache;

/**
 * @author hundun
 * Created on 2021/04/26
 */
@Repository
public interface MatrixReportRepository extends MongoRepository<MatrixReport, String> {
    
}
