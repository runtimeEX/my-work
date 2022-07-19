package com.sharding.web.controller;

import com.sharding.web.service.ExcelInfoWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-sharding
 * @Package com.sharding.web.controller
 * @Description: TODO
 * @date Date : 2022年07月18日 下午2:41
 */
@RestController
@RequestMapping("/excel")
public class ExcelInfoWebController {
    @Autowired
    private ExcelInfoWebService excelInfoWebService;

    @GetMapping("/insert")
    public String insert() {
        excelInfoWebService.add();
        return "success";
    }

    @GetMapping("/excel")
    public void excel(HttpServletResponse httpServletResponse) throws IOException {
        excelInfoWebService.sheetExport(httpServletResponse);
    }
}
