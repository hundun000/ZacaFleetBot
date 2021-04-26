package com.mirai.hundun.cp.penguin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mirai.hundun.cp.penguin.domain.Item;
import com.mirai.hundun.cp.penguin.domain.MatrixReport;
import com.mirai.hundun.cp.penguin.domain.MatrixReportNode;
import com.mirai.hundun.cp.penguin.domain.ResultMatrixNode;
import com.mirai.hundun.cp.penguin.domain.ResultMatrixResponse;
import com.mirai.hundun.cp.penguin.domain.Stage;
import com.mirai.hundun.cp.penguin.feign.PenguinApiService;
import com.mirai.hundun.cp.quiz.QuizService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/04/26
 */
@Slf4j
@Service
public class PenguinService {
    
    @Autowired
    StageRepository stageRepository;
    
    @Autowired
    ItemRepository itemRepository;
    
    @Autowired
    PenguinApiService penguinApiService;
    
    @Autowired
    MatrixReportRepository matrixReportRepository; 
    
    public void updateStages() {
        List<Stage> items = penguinApiService.stages();
        stageRepository.saveAll(items);
        log.info("updateStages items size = {}", items.size());
    }
    
    public void updateItems() {
        List<Item> items = penguinApiService.items();
        itemRepository.saveAll(items);
        log.info("updateItems items size = {}", items.size());
    }
    
    private MatrixReport getResultMatrixReport(Item item) {
        String reportId = item.getItemId();
        MatrixReport report = matrixReportRepository.findById(reportId).orElse(null);
        if (report == null) {
            ResultMatrixResponse response = penguinApiService.resultMatrix(item.getItemId());
            if (response != null) {
                List<MatrixReportNode> reportNodes = new ArrayList<>();
                for (ResultMatrixNode matrixNode : response.getMatrix()) {
                    Stage stage = stageRepository.findById(matrixNode.getStageId()).get();
                    int quantity = matrixNode.getQuantity();
                    int times = matrixNode.getTimes();
                    double gainRate = 1.0 * matrixNode.getQuantity() / matrixNode.getTimes();
                    int stageCost = stage.getApCost();
                    double costExpectation = stageCost / gainRate;
                    String stageCode = stage.getCode();
                    
                    MatrixReportNode reportNode = new MatrixReportNode();
                    reportNode.setQuantity(quantity);
                    reportNode.setTimes(times);
                    reportNode.setGainRate(gainRate);
                    reportNode.setGainRateString(String.format("%.2f", gainRate));
                    reportNode.setCostExpectation(costExpectation);
                    reportNode.setCostExpectationString(String.format("%.2f", costExpectation));
                    reportNode.setStageCode(stageCode);
                    reportNode.setStageCost(stageCost);
                    reportNodes.add(reportNode);
                }
                report = new MatrixReport();
                report.setId(reportId);
                report.setItemName(item.getName());
                report.setNodes(reportNodes);
                matrixReportRepository.save(report);
            }
        }
        return report;
    }
    
    
    public Item queryItem(String fuzzyName) {
        Item item = itemRepository.findTopByName(fuzzyName);
        return item;
    }
    
    
    public MatrixReport getTopResultNode(String fuzzyName, int topSize) {
        Item item = itemRepository.findTopByName(fuzzyName);
        
        if (item == null || item.getItemId() == null) {
            return null;
        }
        
        MatrixReport report = getResultMatrixReport(item);
        Collections.sort(report.getNodes(), new Comparator<MatrixReportNode>() {
            public int compare(MatrixReportNode o1, MatrixReportNode o2) {
                return 1 * (int)(o1.getCostExpectation() - o2.getCostExpectation());
            }
        });
        List<MatrixReportNode> rusult = report.getNodes().subList(0, Math.min(topSize, report.getNodes().size()));
        report.setNodes(rusult);
        return report;
    }
}
