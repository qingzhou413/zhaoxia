package cn.zhaoblog.zhaoxia.controller.excel;

import cn.zhaoblog.zhaoxia.entity.GoodsSpecDet;
import com.joysuch.core.util.StringUtil;
import com.joysuch.core.web.ReturnMsg;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class ExcelGoodsReader {

    private static final Logger logger = LoggerFactory.getLogger(ExcelGoodsReader.class);

    private static final int version2003 = 2003;
    private static final int version2007 = 2007;

    public List<GoodsSpecDet> read(ReturnMsg msg, File excel) {
        String fileName = excel.getName();
        int version;
        if (fileName.endsWith(".xls")) {
            version = version2003;
        } else if (fileName.endsWith(".xlsx")) {
            version = version2007;
        } else {
            msg.addErrorMsg("上传文件格式只能为xls或xlsx");
            return null;
        }

        List<GoodsSpecDet> list = new ArrayList<>();
        Workbook wb = null;
        InputStream stream = null;
        try {
            if (version == version2003) {
                stream = new FileInputStream(excel);
                wb = (Workbook) new HSSFWorkbook(stream);
            } else if (version == version2007) {
                wb = (Workbook) new XSSFWorkbook(excel);
            }
            Sheet sheet = wb.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            System.out.println(lastRowNum);
            Iterator<Row> rowIterator = sheet.rowIterator();

            int skip = 0;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (skip++ < 1) {
                    continue;
                }
                GoodsSpecDet spec = readRow(row, msg);
                if (spec != null) {
                    list.add(spec);
                }
            }
        } catch (Exception e) {
            logger.info("parse excel exception:", e);
            return null;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        }

        return list;
    }

    private GoodsSpecDet readRow(Row row, ReturnMsg msg) {
        int rowNum = row.getRowNum() + 1;
        StringBuilder sb = new StringBuilder();
        sb.append("第" + rowNum + "行：");
        boolean valid = true;

        int colInd = 0;
        Cell barCodeCell = row.getCell(colInd++);
        //条码
        String barCode = null;
        if (barCodeCell == null || StringUtil.isEmpty((barCode = getCellValString(barCodeCell)))) {
            valid = false;
            sb.append("条形码不能为空");
        }
        //名称
        colInd++;
        //规格
        colInd++;
        //单位
        colInd++;
        //零售价
        Cell priceCell = row.getCell(colInd++);
        String price = null;
        if (priceCell == null || StringUtil.isEmpty((price = getCellValString(priceCell)))) {
            valid = false;
            sb.append("零售价不能为空");
        }
        //销售数量
        colInd++;
        //库存
        Cell leftCell = row.getCell(colInd++);
        String left = null;
        if (leftCell == null || StringUtil.isEmpty((left = getCellValString(leftCell)))) {
            valid = false;
            sb.append("库存不能为空");
        }

        if (valid) {
            GoodsSpecDet spec = new GoodsSpecDet();
            spec.setBarCode(barCode);
            spec.setPrice(new BigDecimal(price));
            spec.setLeftCnt(Double.valueOf(left).intValue());
            return spec;
        } else {
            msg.addErrorMsg(sb.toString());
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    private String getCellValString(Cell cell) {
        String val = null;
        CellType cellTypeEnum = cell.getCellTypeEnum();
        switch (cellTypeEnum) {
            case STRING:
                val = cell.getStringCellValue();
                break;
            case BLANK:
                val = "";
                break;
            case NUMERIC:
                val = String.valueOf(cell.getNumericCellValue());
                break;
            case BOOLEAN:
                val = Boolean.valueOf(cell.getBooleanCellValue()).toString();
                break;
            default:
                break;
        }
        return val;
    }

}
